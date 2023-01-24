package szczepanski.gerard.runit.search.service.cache.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class CachedSearchTermInfo {

    String searchTerm;
    Integer results;
    String cachedDate;

}
