package szczepanski.gerard.runit.search.service.cache.report;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CacheReport {

    private final List<CachedSearchTermInfo> cachedSearchTerms;
    private final Integer cachedSearchTermsNumber;
    private final Integer cacheOccupancyInPercent;

}
