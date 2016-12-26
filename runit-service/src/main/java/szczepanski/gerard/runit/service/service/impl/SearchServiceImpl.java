package szczepanski.gerard.runit.service.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.service.SearchService;

public class SearchServiceImpl implements SearchService {
	private static final Logger LOG = Logger.getLogger(SearchServiceImpl.class);
	
	@Override
	public List<SearchResult> searchFor(String searchTerm) {
		LOG.debug("Fire search for searchTerm: " + searchTerm);
		return new ArrayList<>();
	}

}
