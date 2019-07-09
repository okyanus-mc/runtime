package club.issizler.okyanus.api.cmdnew.mck;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

public class MckArgumentBuilder extends ArgumentBuilder {

    @Override
    protected ArgumentBuilder getThis() {
        return this;
    }

    @Override
    public CommandNode build() {
        return new RootCommandNode();
    }

}
