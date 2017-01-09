package szczepanski.gerard.runit.search.service.result;

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
	
	/**
	 * Command pattern method. Execution of this method should run (open) SearchResult.
	 */
	public abstract void run();

	@Override
	public String toString() {
		return searchResultRepresentation.getSearchresultTitle();
	}
	
	
}
