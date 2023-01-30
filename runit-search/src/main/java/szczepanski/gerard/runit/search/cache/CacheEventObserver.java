package szczepanski.gerard.runit.search.cache;

import org.apache.log4j.Logger;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.common.event.RunitEvent;
import szczepanski.gerard.runit.common.event.RunitEventBus;
import szczepanski.gerard.runit.common.event.RunitEventObserver;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class CacheEventObserver implements RunitEventObserver {
    private static final Logger LOG = Logger.getLogger(CacheEventObserver.class);

    private final Cache cache;

    public static CacheEventObserver createFor(Cache cache) {
        LOG.debug("Create CacheEventObserver for cache: " + cache);
        final CacheEventObserver cacheEventObserver = new CacheEventObserver(cache);
        RunitEventBus.register("settings-updated", cacheEventObserver);
        return cacheEventObserver;
    }

    @Override
    public void handleRunitEvent(RunitEvent event) {
        LOG.debug("Clear cache on settings update");
        cache.clear();
    }
}
