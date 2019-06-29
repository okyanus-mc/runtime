// https://github.com/PaperMC/Paper/blob/master/Spigot-Server-Patches/0394-Optimize-redstone-algorithm.patch

package club.issizler.okyanus.runtime.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class FastRedstoneWire {

    /*
     * Okyanus: Give some credits, copied from Paper patch:
     * Many thanks to the following individuals for their help in testing this functionality:
     * - pokechu22, _MethodZz_, WARBEN, NarcolepticFrog, CommandHelper (nessie), ilmango,
     *   OreoLamp, Xcom6000, tryashtar, RedCMD, Smokey95Dog, EDDxample, Rays Works,
     *   Nodnam, BlockyPlays, Grumm, NeunEinser, HelVince.
     */

    private static final boolean[] updateRedstone = {
            true, true, false, false, true, true, // 0 to 5
            false, true, true, false, false, false, // 6 to 11
            true, true, false, false, false, true, // 12 to 17
            true, false, true, true, false, false // 18 to 23
    };
    private static final int north = 0;
    private static final int east = 1;
    private static final int south = 2;
    private static final int west = 3;
    private static final int[] forwardIsNorth = {2, 3, 16, 19, 0, 4, 1, 5, 7, 8, 17, 20, 12, 13, 18, 21, 6, 9, 22, 14, 11, 10, 23, 15};
    private static final int[] forwardIsEast = {2, 3, 16, 19, 4, 1, 5, 0, 17, 20, 12, 13, 18, 21, 7, 8, 22, 14, 11, 15, 23, 9, 6, 10};
    private static final int[] forwardIsSouth = {2, 3, 16, 19, 1, 5, 0, 4, 12, 13, 18, 21, 7, 8, 17, 20, 11, 15, 23, 10, 6, 14, 22, 9};
    private static final int[] forwardIsWest = {2, 3, 16, 19, 5, 0, 4, 1, 18, 21, 7, 8, 17, 20, 12, 13, 23, 10, 6, 9, 22, 15, 11, 14};
    private static final int[][] reordering = {forwardIsNorth, forwardIsEast, forwardIsSouth, forwardIsWest};
    private static final boolean old_current_change = false;
    private static final int[] rs_neighbors = {4, 5, 6, 7};
    private static final int[] rs_neighbors_up = {9, 11, 13, 15};
    private static final int[] rs_neighbors_dn = {8, 10, 12, 14};
    private final RedstoneWireBlock wire;
    private final Map<BlockPos, UpdateNode> nodeCache = Maps.newHashMap();
    private List<UpdateNode> updateQueue0 = Lists.newArrayList();
    private List<UpdateNode> updateQueue1 = Lists.newArrayList();
    private List<UpdateNode> updateQueue2 = Lists.newArrayList();
    private int currentWalkLayer = 0;

    public FastRedstoneWire(RedstoneWireFastable wire) {
        this.wire = (RedstoneWireBlock) wire;
    }

    private static BlockPos[] computeAllNeighbors(final BlockPos pos) {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();
        final BlockPos[] n = new BlockPos[24];

        // Immediate neighbors, in the same order as
        // World.notifyNeighborsOfStateChange, etc.:
        // west, east, down, up, north, south
        n[0] = new BlockPos(x - 1, y, z);
        n[1] = new BlockPos(x + 1, y, z);
        n[2] = new BlockPos(x, y - 1, z);
        n[3] = new BlockPos(x, y + 1, z);
        n[4] = new BlockPos(x, y, z - 1);
        n[5] = new BlockPos(x, y, z + 1);

        // Neighbors of neighbors, in the same order,
        // except that duplicates are not included
        n[6] = new BlockPos(x - 2, y, z);
        n[7] = new BlockPos(x - 1, y - 1, z);
        n[8] = new BlockPos(x - 1, y + 1, z);
        n[9] = new BlockPos(x - 1, y, z - 1);
        n[10] = new BlockPos(x - 1, y, z + 1);
        n[11] = new BlockPos(x + 2, y, z);
        n[12] = new BlockPos(x + 1, y - 1, z);
        n[13] = new BlockPos(x + 1, y + 1, z);
        n[14] = new BlockPos(x + 1, y, z - 1);
        n[15] = new BlockPos(x + 1, y, z + 1);
        n[16] = new BlockPos(x, y - 2, z);
        n[17] = new BlockPos(x, y - 1, z - 1);
        n[18] = new BlockPos(x, y - 1, z + 1);
        n[19] = new BlockPos(x, y + 2, z);
        n[20] = new BlockPos(x, y + 1, z - 1);
        n[21] = new BlockPos(x, y + 1, z + 1);
        n[22] = new BlockPos(x, y, z - 2);
        n[23] = new BlockPos(x, y, z + 2);
        return n;
    }

    private static void orientNeighbors(final UpdateNode[] src, final UpdateNode[] dst, final int heading) {
        final int[] re = reordering[heading];
        for (int i = 0; i < 24; i++) {
            dst[i] = src[re[i]];
        }
    }

    static private int computeHeading(final int rx, final int rz) {
        // rx and rz can only take on values -1, 0, and 1, so we can
        // compute a code number that allows us to use a single switch
        // to determine the heading.
        final int code = (rx + 1) + 3 * (rz + 1);
        switch (code) {
            case 0:
            case 2: {
                // Both rx and rz are -1 (northwest)
                // Randomly choose one to be forward.
                final int j = ThreadLocalRandom.current().nextInt(0, 1);
                return (j == 0) ? north : west;
            }
            case 1: {
                // rx=0, rz=-1
                // Definitively North
                return north;
            }
            case 3: {
                // rx=-1, rz=0
                // Definitively West
                return west;
            }
            case 4: {
                // rx=0, rz=0
                // Heading is completely ambiguous. Choose
                // randomly among the four cardinal directions.
                return ThreadLocalRandom.current().nextInt(0, 4);
            }
            case 5: {
                // rx=1, rz=0
                // Definitively East
                return east;
            }
            case 6: {
                // rx=-1, rz=1 (southwest)
                // Choose randomly between south and west
                final int j = ThreadLocalRandom.current().nextInt(0, 1);
                return (j == 0) ? south : west;
            }
            case 7: {
                // rx=0, rz=1
                // Definitively South
                return south;
            }
            case 8: {
                // rx=1, rz=1 (southeast)
                // Choose randomly between south and east
                final int j = ThreadLocalRandom.current().nextInt(0, 1);
                return (j == 0) ? south : east;
            }
        }

        // We should never get here
        return ThreadLocalRandom.current().nextInt(0, 4);
    }

    private static int getMaxCurrentStrength(final UpdateNode upd, final int strength) {
        if (upd.type != UpdateNode.Type.REDSTONE) return strength;
        final int i = upd.currentState.get(RedstoneWireBlock.POWER);
        return i > strength ? i : strength;
    }

    private void identifyNode(final World worldIn, final UpdateNode upd1) {
        final BlockPos pos = upd1.self;
        final BlockState oldState = worldIn.getBlockState(pos);
        upd1.currentState = oldState;

        // Some neighbors of redstone wire are other kinds of blocks.
        // These need to receive block updates to inform them that
        // redstone wire values have changed.
        final Block block = oldState.getBlock();
        if (block != wire) {
            // Mark this block as not redstone wire and therefore
            // requiring updates
            upd1.type = UpdateNode.Type.OTHER;

            // Non-redstone blocks may propagate updates, but those updates
            // are not handled by this accelerator.  Therefore, we do not
            // expand this position's neighbors.
            return;
        }

        // One job of BlockRedstoneWire.neighborChanged is to convert
        // redstone wires to items if the block beneath was removed.
        // With this accelerator, BlockRedstoneWire.neighborChanged
        // is only typically called for a single wire block, while
        // others are processed internally by the breadth first search
        // algorithm.  To preserve this game behavior, this check must
        // be replicated here.
        if (!wire.canPlaceAt(null, worldIn, pos)) {
            // Pop off the redstone dust
            Block.dropStacks(oldState, worldIn, pos, null); // TODO: supposed to be dropNaturally
            worldIn.breakBlock(pos, false);

            // Mark this position as not being redstone wire
            upd1.type = UpdateNode.Type.OTHER;

            // Note: Sending updates to air blocks leads to an empty method.
            // Testing shows this to be faster than explicitly avoiding updates to
            // air blocks.
            return;
        }

        // If the above conditions fail, then this is a redstone wire block.
        upd1.type = UpdateNode.Type.REDSTONE;
    }

    private void updateNode(final World worldIn, final UpdateNode upd1, final int layer) {
        final BlockPos pos = upd1.self;

        // Mark this redstone wire as having been visited so that it can be used
        // to calculate direction of information flow.
        upd1.visited = true;

        // Look up the last known state.
        // Due to the way other redstone components are updated, we do not
        // have to worry about a state changing behind our backs.  The rare
        // exception is handled by scheduleReentrantNeighborChanged.
        final BlockState oldState = upd1.currentState;

        // Ask the wire block to compute its power level from its neighbors.
        // This will also update the wire's power level and return a new
        // state if it has changed.  When a wire power level is changed,
        // calculateCurrentChanges will immediately update the block state in the world
        // and return the same value here to be cached in the corresponding
        // UpdateNode object.
        BlockState newState;
        if (old_current_change) {
            newState = ((RedstoneWireFastable) wire).calculateCurrentChanges(worldIn, pos, pos, oldState); // TODO: implement this as mixin
        } else {
            // Looking up block state is slow.  This accelerator includes a version of
            // calculateCurrentChanges that uses cahed wire values for a
            // significant performance boost.
            newState = this.calculateCurrentChanges(worldIn, upd1);
        }

        // Only inform neighbors if the state has changed
        if (newState != oldState) {
            // Store the new state
            upd1.currentState = newState;

            // Inform neighbors of the change
            propagateChanges(worldIn, upd1, layer);
        }
    }

    private void findNeighbors(final World worldIn, final UpdateNode upd1) {
        final BlockPos pos = upd1.self;

        // Get the list of neighbor coordinates
        final BlockPos[] neighbors = computeAllNeighbors(pos);

        // Temporary array of neighbors in cardinal ordering
        final UpdateNode[] neighborNodes = new UpdateNode[24];

        // Target array of neighbors sorted left-to-right
        upd1.neighborNodes = new UpdateNode[24];

        for (int i = 0; i < 24; i++) {
            // Look up each neighbor in the node cache
            final BlockPos pos2 = neighbors[i];
            UpdateNode upd2 = nodeCache.get(pos2);
            if (upd2 == null) {
                // If this is a previously unreached position, create
                // a new update node, add it to the cache, and identify what it is.
                upd2 = new UpdateNode();
                upd2.self = pos2;
                upd2.parent = pos;
                nodeCache.put(pos2, upd2);
                identifyNode(worldIn, upd2);
            }

            // For non-redstone blocks, any of the 24 neighboring positions
            // should receive a block update.  However, some block coordinates
            // may contain a redstone wire that does not directly connect to the
            // one being expanded.  To avoid redundant calculations and confusing
            // cross-talk, those neighboring positions are not included.
            if (updateRedstone[i] || upd2.type != UpdateNode.Type.REDSTONE) {
                neighborNodes[i] = upd2;
            }
        }

        // Determine the directions from which the redstone signal may have come from.  This
        // checks for redstone wire at the same Y level and also Y+1 and Y-1, relative to the
        // block being expanded.
        final boolean fromWest = (neighborNodes[0].visited || neighborNodes[7].visited || neighborNodes[8].visited);
        final boolean fromEast = (neighborNodes[1].visited || neighborNodes[12].visited || neighborNodes[13].visited);
        final boolean fromNorth = (neighborNodes[4].visited || neighborNodes[17].visited || neighborNodes[20].visited);
        final boolean fromSouth = (neighborNodes[5].visited || neighborNodes[18].visited || neighborNodes[21].visited);

        int cx = 0, cz = 0;
        if (fromWest) cx += 1;
        if (fromEast) cx -= 1;
        if (fromNorth) cz += 1;
        if (fromSouth) cz -= 1;

        int heading;
        if (cx == 0 && cz == 0) {
            // If there is no clear direction, try to inherit the heading from ancestor nodes.
            heading = computeHeading(upd1.xbias, upd1.zbias);

            // Propagate that heading to descendant nodes.
            for (int i = 0; i < 24; i++) {
                final UpdateNode nn = neighborNodes[i];
                if (nn != null) {
                    nn.xbias = upd1.xbias;
                    nn.zbias = upd1.zbias;
                }
            }
        } else {
            if (cx != 0 && cz != 0) {
                // If the heading is somewhat ambiguous, try to disambiguate based on
                // ancestor nodes.
                if (upd1.xbias != 0) cz = 0;
                if (upd1.zbias != 0) cx = 0;
            }
            heading = computeHeading(cx, cz);

            // Propagate that heading to descendant nodes.
            for (int i = 0; i < 24; i++) {
                final UpdateNode nn = neighborNodes[i];
                if (nn != null) {
                    nn.xbias = cx;
                    nn.zbias = cz;
                }
            }
        }

        // Reorder neighboring UpdateNode objects according to the forward direction
        // determined above.
        orientNeighbors(neighborNodes, upd1.neighborNodes, heading);
    }

    private void propagateChanges(final World worldIn, final UpdateNode upd1, final int layer) {
        if (upd1.neighborNodes == null) {
            // If this node has not been expanded yet, find its neighbors
            findNeighbors(worldIn, upd1);
        }

        final BlockPos pos = upd1.self;

        // All neighbors may be scheduled for layer N+1
        final int layer1 = layer + 1;

        // If the node being updated (upd1) has already been expanded, then merely
        // schedule updates to its neighbors.
        for (int i = 0; i < 24; i++) {
            final UpdateNode upd2 = upd1.neighborNodes[i];

            // This test ensures that an UpdateNode is never scheduled to the same layer
            // more than once.  Also, skip non-connecting redstone wire blocks
            if (upd2 != null && layer1 > upd2.layer) {
                upd2.layer = layer1;
                updateQueue1.add(upd2);

                // Keep track of which block updated this neighbor
                upd2.parent = pos;
            }
        }

        // Nodes above and below are scheduled ALSO for layer N+2
        final int layer2 = layer + 2;

        // Repeat of the loop above, but only for the first four (above and below) neighbors
        // and for layer N+2;
        for (int i = 0; i < 4; i++) {
            final UpdateNode upd2 = upd1.neighborNodes[i];
            if (upd2 != null && layer2 > upd2.layer) {
                upd2.layer = layer2;
                updateQueue2.add(upd2);
                upd2.parent = pos;
            }
        }
    }

    private void shiftQueue() {
        final List<UpdateNode> t = updateQueue0;
        t.clear();
        updateQueue0 = updateQueue1;
        updateQueue1 = updateQueue2;
        updateQueue2 = t;
    }

    private void breadthFirstWalk(final World worldIn) {
        shiftQueue();
        currentWalkLayer = 1;

        // Loop over all layers
        while (updateQueue0.size() > 0 || updateQueue1.size() > 0) {
            // Get the set of blocks in this layer
            final List<UpdateNode> thisLayer = updateQueue0;

            // Loop over all blocks in the layer.  Recall that
            // this is a List, preserving the insertion order of
            // left-to-right based on direction of information flow.
            for (UpdateNode upd : thisLayer) {
                if (upd.type == UpdateNode.Type.REDSTONE) {
                    // If the node is is redstone wire,
                    // schedule updates to neighbors if its value
                    // has changed.
                    updateNode(worldIn, upd, currentWalkLayer);
                } else {
                    // If this block is not redstone wire, send a block update.
                    // Redstone wire blocks get state updates, but they don't
                    // need block updates.  Only non-redstone neighbors need updates.

                    // World.neighborChanged is called from
                    // World.notifyNeighborsOfStateChange, and
                    // notifyNeighborsOfStateExcept.  We don't use
                    // World.notifyNeighborsOfStateChange here, since we are
                    // already keeping track of all of the neighbor positions
                    // that need to be updated.  All on its own, handling neighbors
                    // this way reduces block updates by 1/3 (24 instead of 36).
                    worldIn.updateNeighbor(upd.self, wire, upd.parent);
                }
            }

            // Move on to the next layer
            shiftQueue();
            currentWalkLayer++;
        }

        currentWalkLayer = 0;
    }

    private BlockState scheduleReentrantNeighborChanged(final World worldIn, final BlockPos pos, final BlockState newState, final BlockPos source) {
        if (source != null) {
            // If the cause of the redstone wire update is known, we can use that to help determine
            // direction of information flow.
            UpdateNode src = nodeCache.get(source);
            if (src == null) {
                src = new UpdateNode();
                src.self = source;
                src.parent = source;
                src.visited = true;
                identifyNode(worldIn, src);
                nodeCache.put(source, src);
            }
        }

        // Find or generate a node for the redstone block position receiving the update
        UpdateNode upd = nodeCache.get(pos);
        if (upd == null) {
            upd = new UpdateNode();
            upd.self = pos;
            upd.parent = pos;
            upd.visited = true;
            identifyNode(worldIn, upd);
            nodeCache.put(pos, upd);
        }
        upd.currentState = newState;

        // Receiving this block update may mean something in the world changed.
        // Therefore we clear the cached block info about all neighbors of
        // the position receiving the update and then re-identify what they are.
        if (upd.neighborNodes != null) {
            for (int i = 0; i < 24; i++) {
                final UpdateNode upd2 = upd.neighborNodes[i];
                if (upd2 == null) continue;
                upd2.type = UpdateNode.Type.UNKNOWN;
                upd2.currentState = null;
                identifyNode(worldIn, upd2);
            }
        }

        // The block at 'pos' is a redstone wire and has been updated already by calling
        // wire.calculateCurrentChanges, so we don't schedule that.  However, we do need
        // to schedule its neighbors.  By passing the current value of 'currentWalkLayer' to
        // propagateChanges, the neighbors of 'pos' are scheduled for layers currentWalkLayer+1
        // and currentWalkLayer+2.
        propagateChanges(worldIn, upd, currentWalkLayer);

        // Return here.  The call stack will unwind back to the first call to
        // updateSurroundingRedstone, whereupon the new updates just scheduled will
        // be propagated.  This also facilitates elimination of superfluous and
        // redundant block updates.
        return newState;
    }

    public BlockState updateSurroundingRedstone(final World worldIn, final BlockPos pos, final BlockState state, final BlockPos source) {
        // Check this block's neighbors and see if its power level needs to change
        // Use the calculateCurrentChanges method in BlockRedstoneWire since we have no
        // cached block states at this point.
        final BlockState newState = ((RedstoneWireFastable) wire).calculateCurrentChanges(worldIn, pos, pos, state);

        // If no change, exit
        if (newState == state) {
            return state;
        }

        // Check to see if this update was received during an on-going breadth first search
        if (currentWalkLayer > 0 || nodeCache.size() > 0) {
            // As breadthFirstWalk progresses, it sends block updates to neighbors.  Some of those
            // neighbors may affect the world so as to cause yet another redstone wire block to receive
            // an update.  If that happens, we need to integrate those redstone wire updates into the
            // already on-going graph walk being performed by breadthFirstWalk.
            return scheduleReentrantNeighborChanged(worldIn, pos, newState, source);
        }
        // If there are no on-going walks through redstone wire, then start a new walk.

        // If the source of the block update to the redstone wire at 'pos' is known, we can use
        // that to help determine the direction of information flow.
        if (source != null) {
            final UpdateNode src = new UpdateNode();
            src.self = source;
            src.parent = source;
            src.visited = true;
            nodeCache.put(source, src);
            identifyNode(worldIn, src);
        }

        // Create a node representing the block at 'pos', and then propagate updates
        // to its neighbors.  As stated above, the call to wire.calculateCurrentChanges
        // already performs the update to the block at 'pos', so it is not added to the schedule.
        final UpdateNode upd = new UpdateNode();
        upd.self = pos;
        upd.parent = source != null ? source : pos;
        upd.currentState = newState;
        upd.type = UpdateNode.Type.REDSTONE;
        upd.visited = true;
        nodeCache.put(pos, upd);
        propagateChanges(worldIn, upd, 0);

        // Perform the walk over all directly reachable redstone wire blocks, propagating wire value
        // updates in a breadth first order out from the initial update received for the block at 'pos'.
        breadthFirstWalk(worldIn);

        // With the whole search completed, clear the list of all known blocks.
        // We do not want to keep around state information that may be changed by other code.
        // In theory, we could cache the neighbor block positions, but that is a separate
        // optimization.
        nodeCache.clear();

        return newState;
    }

    private BlockState calculateCurrentChanges(final World worldIn, final UpdateNode upd) {
        BlockState state = upd.currentState;
        final int i = state.get(RedstoneWireBlock.POWER);
        int j = 0;
        j = getMaxCurrentStrength(upd, j);
        int l = 0;

        ((RedstoneWireFastable) wire).setCanProvidePower(false); // TODO: Mixin thing
        // Unfortunately, World.isBlockIndirectlyGettingPowered is complicated,
        // and I'm not ready to try to replicate even more functionality from
        // elsewhere in Minecraft into this accelerator.  So sadly, we must
        // suffer the performance hit of this very expensive call.  If there
        // is consistency to what this call returns, we may be able to cache it.
        final int k = worldIn.getReceivedRedstonePower(upd.self); // TODO: is this isBlockIndirectlyGettingPowered
        ((RedstoneWireFastable) wire).setCanProvidePower(true);

        // The variable 'k' holds the maximum redstone power value of any adjacent blocks.
        // If 'k' has the highest level of all neighbors, then the power level of this
        // redstone wire will be set to 'k'.  If 'k' is already 15, then nothing inside the
        // following loop can affect the power level of the wire.  Therefore, the loop is
        // skipped if k is already 15.
        if (k < 15) {
            if (upd.neighborNodes == null) {
                // If this node's neighbors are not known, expand the node
                findNeighbors(worldIn, upd);
            }

            // These remain constant, so pull them out of the loop.
            // Regardless of which direction is forward, the UpdateNode for the
            // position directly above the node being calculated is always
            // at index 1.
            UpdateNode center_up = upd.neighborNodes[1];
            boolean center_up_is_cube = center_up.currentState.isOpaque(); // TODO: is this isOccluding

            for (int m = 0; m < 4; m++) {
                // Get the neighbor array index of each of the four cardinal
                // neighbors.
                int n = rs_neighbors[m];

                // Get the max redstone power level of each of the cardinal
                // neighbors
                UpdateNode neighbor = upd.neighborNodes[n];
                l = getMaxCurrentStrength(neighbor, l);

                // Also check the positions above and below the cardinal
                // neighbors
                boolean neighbor_is_cube = neighbor.currentState.isOpaque();
                if (!neighbor_is_cube) {
                    UpdateNode neighbor_down = upd.neighborNodes[rs_neighbors_dn[m]];
                    l = getMaxCurrentStrength(neighbor_down, l);
                } else if (!center_up_is_cube) {
                    UpdateNode neighbor_up = upd.neighborNodes[rs_neighbors_up[m]];
                    l = getMaxCurrentStrength(neighbor_up, l);
                }
            }
        }

        // The new code sets this RedstoneWire block's power level to the highest neighbor
        // minus 1.  This usually results in wire power levels dropping by 2 at a time.
        // This optimization alone has no impact on update order, only the number of updates.
        j = l - 1;

        // If 'l' turns out to be zero, then j will be set to -1, but then since 'k' will
        // always be in the range of 0 to 15, the following if will correct that.
        if (k > j) j = k;

        if (i != j) {
            // If the power level has changed from its previous value, compute a new state
            // and set it in the world.
            // Possible optimization:  Don't commit state changes to the world until they
            // need to be known by some nearby non-redstone-wire block.
            state = state.with(RedstoneWireBlock.POWER, j);
            worldIn.setBlockState(upd.self, state, 2);
        }

        return state;
    }

    private static class UpdateNode {
        BlockState currentState;        // Keep track of redstone wire value
        UpdateNode[] neighborNodes;    // References to neighbors (directed graph edges)
        BlockPos self;                  // UpdateNode's own position
        BlockPos parent;                // Which block pos spawned/updated this node
        Type type = Type.UNKNOWN;       // unknown, redstone wire, other type of block
        int layer;                      // Highest layer this node is scheduled in
        boolean visited;                // To keep track of information flow direction, visited restone wire is marked
        int xbias, zbias;               // Remembers directionality of ancestor nodes; helps eliminate directional ambiguities.
        public enum Type {
            UNKNOWN, REDSTONE, OTHER
        }
    }
}