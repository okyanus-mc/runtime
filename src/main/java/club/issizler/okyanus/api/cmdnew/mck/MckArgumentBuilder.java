package club.issizler.okyanus.api.cmdnew.mck;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

public class MckArgumentBuilder extends ArgumentBuilder {

    @Override
    protected ArgumentBuilder getThis() {
        return this;
    }

    @Override
    public CommandNode build() {
        return null;
    }

}
