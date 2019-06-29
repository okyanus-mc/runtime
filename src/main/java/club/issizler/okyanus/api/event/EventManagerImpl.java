package club.issizler.okyanus.api.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EventManagerImpl implements EventManager {
    INSTANCE;

    private Map<String, List<EventHandler>> handlers = new HashMap<>();
    private Logger logger = LogManager.getLogger();

    public void register(EventHandler eventClass) {
        for (Type type : eventClass.getClass().getGenericInterfaces()) {
            if (!(type instanceof ParameterizedType))
                continue;

            if(!type.getTypeName().contains(EventHandler.class.getTypeName()))
                continue;

            Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
            for (Type genericType : genericTypes) {
                String eventKey = genericType.getTypeName().replace("class ", "");

                if (!handlers.containsKey(eventKey)) {
                    logger.debug("Okyanus: Creating event " + eventKey);
                    handlers.put(eventKey, new ArrayList<>());
                }

                logger.debug("Okyanus: Registering event class " + eventClass.getClass().getName() + " for event " + eventKey);
                handlers.get(eventKey).add(eventClass);
            }
        }
    }

    public <E> E trigger(E e) {
        List<EventHandler> handlerList = handlers.get(e.getClass().getTypeName());

        if (handlerList == null)
            return e;

        logger.debug("Okyanus: Triggering event " + e.getClass().getName());
        handlerList.forEach(handler -> handler.handle(e));

        return e;
    }

}
