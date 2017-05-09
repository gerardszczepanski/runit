package szczepanski.gerard.runit.search.service.algorithm.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.advanced.collection.facade.AdvancedCollectionFactory;
import szczepanski.gerard.runit.search.service.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.impl.DirectoryResult;
import szczepanski.gerard.runit.search.service.service.SearchTermMatcher;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;

@RequiredArgsConstructor
public class DirectoryAliasSearchAlgorithm implements SearchAlgorithm {

	private final SearchTermMatcher searchtermMatcher;

	@Override
	public List<SearchResult> search(String searchTerm, Settings settings) {
		List<Alias> aliases = settings.getDirAliases();
		return convertFromAliases(searchTerm, aliases);
	}

	private List<SearchResult> convertFromAliases(String searchTerm, List<Alias> aliases) {
		List<SearchResult> searchResults = AdvancedCollectionFactory.list();

		aliases.forEach(a -> {
			if (isAliasContainsSearchTerm(a.getName(), searchTerm)) {
				searchResults.add(DirectoryResult.fromDirectoryAlias(a));
			}
		});

		return searchResults;
	}

	private boolean isAliasContainsSearchTerm(String alias, String searchTerm) {
		return searchtermMatcher.isMatch(searchTerm, alias);
	}
}
