// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0363-MC-145260-Fix-Whitelist-On-Off-inconsistency.patch
// Fixes MC-145260

package club.issizler.okyanus.runtime.mixin.fixes;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.OperatorList;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.Whitelist;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow
    @Final
    private OperatorList ops;

    @Shadow
    @Final
    private Whitelist whitelist;

    /**
     * @author Aikar @ Paper & Okyanus
     * @reason Fix MC-145260
     */
    @Overwrite
    public boolean isWhitelistEnabled() {
        return whitelist.isEnabled();
    }

    /**
     * @author Aikar @ Paper & Okyanus
     * @reason Fix MC-145260
     */
    @Overwrite
    public void setWhitelistEnabled(boolean isEnabled) {
        whitelist.setEnabled(isEnabled);
    }

    /**
     * @author Aikar @ Paper & Okyanus
     * @reason Fix MC-145260
     */
    @Overwrite
    public boolean isWhitelisted(GameProfile gameProfile_1) {
        return !this.isWhitelistEnabled() || this.ops.isOp(gameProfile_1) || this.whitelist.isAllowed(gameProfile_1);
    }

}
