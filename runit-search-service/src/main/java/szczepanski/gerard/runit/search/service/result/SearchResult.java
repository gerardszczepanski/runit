package szczepanski.gerard.runit.search.service.result;

import lombok.Getter;

/**
 * Represents search result to run.
 * 
 * This class follows Command pattern, and represents any result returned by 
 * {@code SearchAlgorithm}. Because SearchResult derivered class its only known by 
 * concrete SearchAlgorithm, this class follows OCP principle.
 * 
 * I can define any SearchAlgorithm implementation, and define any SearchResult returned by that
 * algorithm, and surrounding code, that uses SearchResult wont be affected.
 * 
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((searchResultRepresentation == null) ? 0 : searchResultRepresentation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchResult other = (SearchResult) obj;
		if (searchResultRepresentation == null) {
			if (other.searchResultRepresentation != null)
				return false;
		} else if (!searchResultRepresentation.equals(other.searchResultRepresentation))
			return false;
		return true;
	}
	
}
