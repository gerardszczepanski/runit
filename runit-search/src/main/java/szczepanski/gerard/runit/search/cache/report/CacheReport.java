package szczepanski.gerard.runit.search.cache.report;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class CacheReport {

    List<CachedSearchTermInfo> cachedSearchTerms;
    Integer cachedSearchTermsNumber;
    Integer cacheOccupancyInPercent;

}
