package szczepanski.gerard.runit.search.service.cache.report;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CacheReport {
	
	private final List<CachedSearchTermInfo> cachedSearchTerms;
	private final Integer cachedSearchTermsNumber;
	private final Integer cacheOccupancyInPercent;
	
}
