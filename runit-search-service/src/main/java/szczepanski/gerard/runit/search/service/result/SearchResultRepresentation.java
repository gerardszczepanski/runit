package szczepanski.gerard.runit.search.service.result;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Representation object of SearchResult for display purposes.
 *
 * @author Gerard Szczepanski
 */
@Getter
@AllArgsConstructor
public class SearchResultRepresentation {

    private final Image image;
    private final String searchResultTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResultRepresentation that = (SearchResultRepresentation) o;
        return Objects.equals(image, that.image) &&
                Objects.equals(searchResultTitle, that.searchResultTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, searchResultTitle);
    }
}
