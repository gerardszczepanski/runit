package szczepanski.gerard.runit.search.service.result;

import lombok.Getter;

import java.util.Objects;

/**
 * Represents search result to run.
 * <p>
 * This class follows Command pattern, and represents any result returned by
 * {@code SearchAlgorithm}. Because SearchResult derivered class its only known
 * by concrete SearchAlgorithm, this class follows OCP principle.
 * <p>
 * I can define any SearchAlgorithm implementation, and define any SearchResult
 * returned by that algorithm, and surrounding code, that uses SearchResult wont
 * be affected.
 */
public abstract class SearchResult {

    @Getter
    private final SearchResultRepresentation searchResultRepresentation;

    protected SearchResult(SearchResultRepresentation searchResultRepresentation) {
        this.searchResultRepresentation = searchResultRepresentation;
    }

    /**
     * Command pattern method. Execution of this method should run (open)
     * SearchResult.
     */
    public abstract void run();

    @Override
    public String toString() {
        return searchResultRepresentation.getSearchResultTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        return Objects.equals(searchResultRepresentation, that.searchResultRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchResultRepresentation);
    }
}
