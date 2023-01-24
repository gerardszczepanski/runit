package szczepanski.gerard.runit.search.service.cache.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class CacheReport {

    List<CachedSearchTermInfo> cachedSearchTerms;
    Integer cachedSearchTermsNumber;
    Integer cacheOccupancyInPercent;

}
