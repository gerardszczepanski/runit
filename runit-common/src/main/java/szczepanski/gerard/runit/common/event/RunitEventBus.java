package szczepanski.gerard.runit.common.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;


import lombok.experimental.UtilityClass;

import static java.lang.String.format;

@UtilityClass
public class RunitEventBus {
    private static final Logger LOG = Logger.getLogger(RunitEventBus.class);

    private static final Map<String, Set<RunitEventObserver>> observersMap = new HashMap<>();

    public static void register(String eventName, RunitEventObserver observer) {
        LOG.debug(format("Registering observer [%s] for event: %s", observer, eventName));
        Set<RunitEventObserver> runitEventObservers = observersMap.get(eventName);
        if (runitEventObservers != null) {
            runitEventObservers.add(observer);
            return;
        } else {
            runitEventObservers = new HashSet<>();
            runitEventObservers.add(observer);
            observersMap.put(eventName, runitEventObservers);
        }
    }

    public static void publish(RunitEvent event) {
        LOG.debug(format("Publishing event: %s", event));
        final Set<RunitEventObserver> setOfInterestedObservers = observersMap.get(event.getName());
        if (setOfInterestedObservers != null) {
            setOfInterestedObservers.forEach(observer -> observer.handleRunitEvent(event));
        }
    }

}
