package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.result.WebPageResult;
import szczepanski.gerard.runit.service.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.service.search.loader.Settings;
import szczepanski.gerard.runit.service.search.loader.Alias;
import szczepanski.gerard.runit.service.service.SearchTermMatcher;

@RequiredArgsConstructor
public class WebAliasSearchAlgorithm implements SearchAlgorithm {
	
	private final SearchTermMatcher searchtermMatcher;
	
	@Override
	public List<SearchResult> search(String searchTerm, Settings settings) {
		List<Alias> aliases = settings.getWebAliases();
		return aliases.stream()
					.filter(a -> isAliasContainsSearchTerm(a.getName(), searchTerm))
					.map(WebPageResult::fromWebAlias)
					.collect(Collectors.toList());
	}
	
	private boolean isAliasContainsSearchTerm(String alias, String searchTerm) {
		return searchtermMatcher.isMatch(searchTerm, alias);
	}
}
