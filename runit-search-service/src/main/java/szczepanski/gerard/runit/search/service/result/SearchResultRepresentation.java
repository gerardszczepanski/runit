package szczepanski.gerard.runit.search.service.result;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representation object of SearchResult for display purposes.
 * 
 * @author Gerard Szczepanski
 */
@Getter
@AllArgsConstructor
public class SearchResultRepresentation {

	private final Image image;
	private final String searchresultTitle;

	@Override
	public int hashCode() {
		final int prime = 17;
		int result = 1;
		result = prime * result + ((searchresultTitle == null) ? 0 : searchresultTitle.hashCode());
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
		SearchResultRepresentation other = (SearchResultRepresentation) obj;
		if (searchresultTitle == null) {
			if (other.searchresultTitle != null)
				return false;
		} else if (!searchresultTitle.equals(other.searchresultTitle))
			return false;
		return true;
	}

}
