package club.issizler.okyanus.api.cmdnew;

public abstract class ServerCommandSender implements CommandSender {

    public boolean isPlayer() {
        return false;
    }

}
