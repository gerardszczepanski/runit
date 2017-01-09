package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.search.service.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.service.result.DirectoryResult;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.service.service.SearchTermMatcher;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;

@RequiredArgsConstructor
public class DirectoryAliasSearchAlgorithm implements SearchAlgorithm {
	
	private final SearchTermMatcher searchtermMatcher;
	
	@Override
	public List<SearchResult> search(String searchTerm, Settings settings) {
		List<Alias> aliases = settings.getDirAliases();
		return aliases.stream()
					.filter(a -> isAliasContainsSearchTerm(a.getName(), searchTerm))
					.map(DirectoryResult::fromDirectoryAlias)
					.collect(Collectors.toList());
	}
	
	private boolean isAliasContainsSearchTerm(String alias, String searchTerm) {
		return searchtermMatcher.isMatch(searchTerm, alias);
	}
}