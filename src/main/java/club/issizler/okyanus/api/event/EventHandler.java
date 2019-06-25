package club.issizler.okyanus.api.event;

public interface EventHandler<T extends Event> {

    public void handle(T event);

}
