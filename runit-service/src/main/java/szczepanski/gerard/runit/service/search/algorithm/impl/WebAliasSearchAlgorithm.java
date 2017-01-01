package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.util.List;
import java.util.stream.Collectors;

import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.result.WebPageResult;
import szczepanski.gerard.runit.service.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.service.search.loader.Settings;
import szczepanski.gerard.runit.service.search.loader.WebAlias;

public class WebAliasSearchAlgorithm implements SearchAlgorithm{

	@Override
	public List<SearchResult> search(String searchTerm, Settings settings) {
		List<WebAlias> webAliases = settings.getWebAliases();
		return webAliases.stream()
					.filter(a -> isAliasContainsSearchTerm(a.getAlias(), searchTerm))
					.map(WebPageResult::fromWebAlias)
					.collect(Collectors.toList());
	}
	
	/**
	 * Temporary implementation. Will be changed in future to Regex patterns.
	 * TODO Gerard Szczepanski 01.01.2017
	 */
	private boolean isAliasContainsSearchTerm(String alias, String searchTerm) {
		return alias.contains(searchTerm);
	}
}
