package club.issizler.okyanus.runtime.utils;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nonnull;
import java.util.*;

public class PaperWorldMap extends HashMap<DimensionType, ServerWorld> {
    private final List<ServerWorld> worlds = new ArrayList<>();
    private final List<ServerWorld> worldsIterable = new ArrayList<ServerWorld>() {
        @Override
        public Iterator<ServerWorld> iterator() {
            Iterator<ServerWorld> iterator = super.iterator();
            return new Iterator<ServerWorld>() {
                private ServerWorld last;

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public ServerWorld next() {
                    this.last = iterator.next();
                    return last;
                }

                @Override
                public void remove() {
                    worlds.set(last.getWorld().dimension.getType().getRawId() + 1, null);
                }
            };
        }
    };

    @Override
    public int size() {
        return worldsIterable.size();
    }

    @Override
    public boolean isEmpty() {
        return worldsIterable.isEmpty();
    }

    @Override
    public ServerWorld get(Object key) {
        // Will hit the below method
        return key instanceof DimensionType ? get((DimensionType) key) : null;
    }

    public ServerWorld get(DimensionType key) {
        int id = key.getRawId() + 1;
        return worlds.size() > id ? worlds.get(id) : null;
    }

    @Override
    public boolean containsKey(Object key) {
        // will hit below method
        return key instanceof DimensionType && containsKey((DimensionType) key);
    }

    public boolean containsKey(DimensionType key) {
        return get(key) != null;
    }

    @Override
    public ServerWorld put(DimensionType key, ServerWorld value) {
        while (worlds.size() <= key.getRawId() + 1) {
            worlds.add(null);
        }
        ServerWorld old = worlds.set(key.getRawId() + 1, value);
        if (old != null) {
            worldsIterable.remove(old);
        }
        worldsIterable.add(value);
        return old;
    }

    @Override
    public void putAll(Map<? extends DimensionType, ? extends ServerWorld> m) {
        for (Entry<? extends DimensionType, ? extends ServerWorld> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public ServerWorld remove(Object key) {
        return key instanceof DimensionType ? remove((DimensionType) key) : null;
    }

    public ServerWorld remove(DimensionType key) {
        ServerWorld old;
        if (key.getRawId() + 1 == worlds.size() - 1) {
            old = worlds.remove(key.getRawId() + 1);
        } else {
            old = worlds.set(key.getRawId() + 1, null);
        }
        if (old != null) {
            worldsIterable.remove(old);
        }
        return old;
    }

    @Override
    public void clear() {
        throw new RuntimeException("What the hell are you doing?");
    }

    @Override
    public boolean containsValue(Object value) {
        return value instanceof ServerWorld && get(((ServerWorld) value).getWorld().dimension.getType()) != null;
    }

    @Nonnull
    @Override
    public Set<DimensionType> keySet() {
        return new AbstractSet<DimensionType>() {
            @Override
            public Iterator<DimensionType> iterator() {
                Iterator<ServerWorld> iterator = worldsIterable.iterator();
                return new Iterator<DimensionType>() {

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public DimensionType next() {
                        return iterator.next().getWorld().dimension.getType();
                    }

                    @Override
                    public void remove() {
                        iterator.remove();
                    }
                };
            }

            @Override
            public int size() {
                return worlds.size();
            }
        };
    }

    @Override
    public Collection<ServerWorld> values() {
        return worldsIterable;
    }

    @Override
    public Set<Entry<DimensionType, ServerWorld>> entrySet() {
        return new AbstractSet<Entry<DimensionType, ServerWorld>>() {
            @Override
            public Iterator<Entry<DimensionType, ServerWorld>> iterator() {
                Iterator<ServerWorld> iterator = worldsIterable.iterator();
                return new Iterator<Entry<DimensionType, ServerWorld>>() {

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public Entry<DimensionType, ServerWorld> next() {
                        ServerWorld entry = iterator.next();
                        return new SimpleEntry<>(entry.getWorld().dimension.getType(), entry);
                    }

                    @Override
                    public void remove() {
                        iterator.remove();
                    }
                };
            }

            @Override
            public int size() {
                return worldsIterable.size();
            }
        };
    }
}
