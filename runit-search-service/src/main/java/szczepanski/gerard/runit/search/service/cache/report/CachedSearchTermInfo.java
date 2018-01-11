package szczepanski.gerard.runit.search.service.cache.report;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CachedSearchTermInfo {

    private final String searchTerm;
    private final Integer results;
    private final String cachedDate;

}
