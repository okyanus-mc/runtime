package club.issizler.okyanus.api.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EventManager {
    INSTANCE;

    private Map<String, List<EventHandler>> handlers = new HashMap<>();

    public void register(EventHandler eventClass) {
        for (Type type : eventClass.getClass().getGenericInterfaces()) {
            if (!(type instanceof ParameterizedType))
                break;

            if(!type.getTypeName().contains(EventHandler.class.getTypeName()))
                return;

            Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
            for (Type genericType : genericTypes) {
                String eventKey = genericType.getTypeName().replace("class ", "");

                if (!handlers.containsKey(eventKey))
                    handlers.put(eventKey, new ArrayList<>());

                handlers.get(eventKey).add(eventClass);
            }
        }
    }

    public Event trigger(Event e) {
        System.out.println("Triggering event [" + e.getClass().getTypeName() + "]");

        System.out.println("Handlers are " + handlers);
        List<EventHandler> handlerList = handlers.get(e.getClass().getTypeName());

        if (handlerList == null)
            return e;

        handlerList.forEach(handler -> handler.handle(e));

        return e;
    }

}
