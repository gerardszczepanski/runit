package szczepanski.gerard.runit.service.result;

import lombok.Getter;

/**
 * Represents search result to run.
 * 
 *  It may be any extension file, and web page alias.
 */
public abstract class SearchResult {
		
	@Getter
	private final SearchResultRepresentation searchResultRepresentation;
	
	protected SearchResult(SearchResultRepresentation searchResultRepresentation) {
		this.searchResultRepresentation = searchResultRepresentation;
	}
	
	public abstract void run();

	@Override
	public String toString() {
		return searchResultRepresentation.getSearchresultTitle();
	}
	
	
}
