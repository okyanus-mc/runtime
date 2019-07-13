package club.issizler.okyanus.api.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRegistryImpl implements EventRegistry {

    private Map<String, List<EventHandler>> handlers = new HashMap<>();
    private Logger logger = LogManager.getLogger();

    public void register(@NotNull EventHandler eventHandler) {
        for (Type type : eventHandler.getClass().getGenericInterfaces()) {
            if (!(type instanceof ParameterizedType))
                continue;

            if (!type.getTypeName().contains(EventHandler.class.getTypeName()))
                continue;

            Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
            for (Type genericType : genericTypes) {
                String eventKey = genericType.getTypeName().replace("class ", "");

                if (!handlers.containsKey(eventKey)) {
                    logger.debug("Okyanus: Creating event " + eventKey);
                    handlers.put(eventKey, new ArrayList<>());
                }

                logger.debug("Okyanus: Registering event class " + eventHandler.getClass().getName() + " for event " + eventKey);
                handlers.get(eventKey).add(eventHandler);
            }
        }
    }

    @Override
    public void unregister(@NotNull EventHandler eventHandler) {
        for (Type type : eventHandler.getClass().getGenericInterfaces()) {
            if (!(type instanceof ParameterizedType))
                continue;

            if (!type.getTypeName().contains(EventHandler.class.getTypeName()))
                continue;

            Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
            for (Type genericType : genericTypes) {
                String eventKey = genericType.getTypeName().replace("class ", "");

                handlers.remove(eventKey);
            }
        }
    }

    @NotNull
    public <E extends Event> E trigger(@NotNull E e) {
        String eventName = e.getClass().getInterfaces()[0].getTypeName();
        List<EventHandler> handlerList = handlers.get(eventName);

        if (handlerList == null)
            return e;

        handlerList.forEach(handler -> handler.handle(e));

        return e;
    }

}
