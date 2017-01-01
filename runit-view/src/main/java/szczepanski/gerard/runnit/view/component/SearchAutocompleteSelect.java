package szczepanski.gerard.runnit.view.component;

import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.Setter;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.result.SearchResultRepresentation;
import szczepanski.gerard.runit.service.service.SearchService;

/**
 * This is Autocomplete Select component responsible for handle typed text, and
 * display typed text results.
 * 
 * This object requires {@code SearchService} type object, to fire search and
 * return result.
 * 
 * @author Gerard Szczepanski
 * 
 */
public class SearchAutocompleteSelect {

	private final ComboBox<SearchResult> innerSelect;

	@Setter
	private SearchService searchService;

	/**
	 * Factory method for create SearchAutocomplete Select.
	 * 
	 * @param currentPane
	 *            - pane for pin SearchAutocompleteSelect object after create.
	 * @param dimenstion
	 *            - dimension for SearchAutocompleteSelect object.
	 * @param position
	 *            - position for SearchAutocompleteSelect object.
	 */
	public static SearchAutocompleteSelect createSearchAutocompleteSelect(Pane currentPane, Dimension dimenstion,
			Position position) {
		return new SearchAutocompleteSelect(currentPane, dimenstion, position);
	}

	private SearchAutocompleteSelect(Pane currentPane, Dimension dimenstion, Position position) {
		this.innerSelect = new ComboBox<>();
		initInnerTextField(currentPane, dimenstion, position);
	}

	private void initInnerTextField(Pane pane, Dimension dimenstion, Position position) {
		innerSelect.setPrefWidth(dimenstion.width);
		innerSelect.setPrefHeight(dimenstion.height);
		innerSelect.setLayoutX(position.x);
		innerSelect.setLayoutY(position.y);
		innerSelect.setEditable(true);
		innerSelect.setCellFactory(configureDisplayRow());
		
		innerSelect.getEditor().addEventFilter(KeyEvent.KEY_RELEASED, e -> {
			handleKeyType();
		});
		
		pane.getChildren().add(innerSelect);
	}

	private Callback<ListView<SearchResult>, ListCell<SearchResult>> configureDisplayRow() {
		return new Callback<ListView<SearchResult>, ListCell<SearchResult>>() {
			@Override
			public ListCell<SearchResult> call(ListView<SearchResult> l) {
				return new ListCell<SearchResult>() {
					@Override
					protected void updateItem(SearchResult item, boolean empty) {
						super.updateItem(item, empty);
						
						if (!isEmpty()) {
							SearchResultRepresentation searchResultRepresentation = item.getSearchResultRepresentation();
							setGraphic(new ImageView(searchResultRepresentation.getImage()));
							setText(searchResultRepresentation.getSearchresultTitle());
						}
					}
				};
			}
		};
	}

	private void handleKeyType() {
		checkSearchService();
		List<SearchResult> searchResults = fireSearch();
		displaySearchResults(searchResults);
	}

	private List<SearchResult> fireSearch() {
		String searchTerm = innerSelect.getEditor().getText();
		return searchService.searchFor(searchTerm);
	}
	
	private void displaySearchResults(List<SearchResult> searchResults) {
		innerSelect.hide();
		innerSelect.getItems().clear();
		innerSelect.getItems().addAll(searchResults);

		if (!searchResults.isEmpty()) {
			innerSelect.show();
		}
	}

	private void checkSearchService() {
		if (searchService == null) {
			throw new RunitRuntimeException("Set SearchService before use SearchAutocompleteSelect object!");
		}
	}

}
