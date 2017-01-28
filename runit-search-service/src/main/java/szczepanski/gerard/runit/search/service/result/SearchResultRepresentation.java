package szczepanski.gerard.runit.search.service.result;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representation object of SearchResult for display purposes.
 * @author Gerard Szczepanski
 */
@Getter
@AllArgsConstructor
public class SearchResultRepresentation {
	
	private final Image image;
	private final String searchresultTitle;
	
}
