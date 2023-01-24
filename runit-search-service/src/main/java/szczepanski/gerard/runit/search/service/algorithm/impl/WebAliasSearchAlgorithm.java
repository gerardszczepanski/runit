package szczepanski.gerard.runit.search.service.algorithm.impl;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.advanced.collection.facade.AdvancedCollectionFactory;
import szczepanski.gerard.runit.search.service.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.impl.WebPageResult;
import szczepanski.gerard.runit.search.service.service.SearchTermMatcher;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;

import java.util.List;

@RequiredArgsConstructor
public class WebAliasSearchAlgorithm implements SearchAlgorithm {

    private final SearchTermMatcher searchtermMatcher;

    @Override
    public List<SearchResult> search(String searchTerm, Settings settings) {
        List<Alias> aliases = settings.getWebAliases();
        return convertFromAliases(searchTerm, aliases);
    }

    private List<SearchResult> convertFromAliases(String searchTerm, List<Alias> aliases) {
        List<SearchResult> searchResults = AdvancedCollectionFactory.list();

        aliases.forEach(a -> {
            if (isAliasContainsSearchTerm(a.getName(), searchTerm)) {
                searchResults.add(WebPageResult.fromWebAlias(a));
            }
        });
        return searchResults;
    }

    private boolean isAliasContainsSearchTerm(String alias, String searchTerm) {
        return searchtermMatcher.isMatch(searchTerm, alias);
    }
}
