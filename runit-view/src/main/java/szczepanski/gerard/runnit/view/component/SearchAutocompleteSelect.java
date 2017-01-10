package szczepanski.gerard.runnit.view.component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.Setter;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.SearchResultRepresentation;
import szczepanski.gerard.runit.service.service.SearchService;
import szczepanski.gerard.runnit.view.util.ProgramTrayManager;

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
	private static final Logger LOG = Logger.getLogger(SearchAutocompleteSelect.class);

	private final ComboBox<SearchResult> innerSelect;
	private final SelectKeyFilter selectKeyFilter;

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
		this.selectKeyFilter = new SelectKeyFilter();
		initInnerTextField(currentPane, dimenstion, position);
	}

	private void search() {
		checkSearchService();
		List<SearchResult> searchResults = fireSearch();
		displaySearchResults(searchResults);
	}

	private void triggerRun() {
		if (isOnlyOneSearchResult()) {
			markOnlyOneMatchSelected();
		}
		runSelected();
	}

	private boolean isOnlyOneSearchResult() {
		return innerSelect.getItems().size() == 1;
	}

	private void markOnlyOneMatchSelected() {
		innerSelect.getSelectionModel().select(0);
	}

	private void runSelected() {
		SearchResult triggeredSearchResult = innerSelect.getSelectionModel().getSelectedItem();
		runSearchResult(triggeredSearchResult);
	}

	private void runSearchResult(SearchResult triggeredSearchResult) {
		if (triggeredSearchResult != null) {
			ProgramTrayManager.hideProgramStage();
			triggeredSearchResult.run();
		}
	}

	private List<SearchResult> fireSearch() {
		String searchTerm = innerSelect.getEditor().getText();
		return searchService.searchFor(searchTerm);
	}

	private void displaySearchResults(List<SearchResult> searchResults) {
		innerSelect.hide();
		innerSelect.getItems().clear();
		innerSelect.getItems().clear(); // This code must be duplicated here
										// (clearSelectOptions()). Otherwise it
										// does not clear list every time.
		innerSelect.getItems().addAll(searchResults);

		if (!searchResults.isEmpty()) {
			innerSelect.show();
		}
	}

	private void clear() {
		clearSelectOptions();
		innerSelect.getEditor().clear();
	}

	private void clearSelectOptions() {
		innerSelect.hide();
		innerSelect.getItems().clear();
		innerSelect.getItems().clear(); // This is required to fix Javafx
										// combobox issue -> List is not always
										// cleared at the first time
	}

	private void checkSearchService() {
		if (searchService == null) {
			throw new RunitRuntimeException(ExceptionCode.R_004);
		}
	}

	private void initInnerTextField(Pane pane, Dimension dimenstion, Position position) {
		innerSelect.setPrefWidth(dimenstion.width);
		innerSelect.setPrefHeight(dimenstion.height);
		innerSelect.setLayoutX(position.x);
		innerSelect.setLayoutY(position.y);
		innerSelect.setEditable(true);
		innerSelect.setCellFactory(new InnerSelectCallback());
		innerSelect.setConverter(new InnerSelectStringConverter());

		innerSelect.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (selectKeyFilter.isKeyAllowedForRun(e.getCode())) {
				triggerRun();
			}
		});

		innerSelect.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (selectKeyFilter.isKeyAllowedForClear(e.getCode())) {
				clear();
			}
		});

		innerSelect.getEditor().addEventFilter(KeyEvent.KEY_RELEASED, e -> {
			if (selectKeyFilter.isKeyAllowedForSearch(e.getCode())) {
				LOG.debug("SEARCH START");
				search();
				LOG.debug("SEARCH FINISHED");
			}
		});

		pane.getChildren().add(innerSelect);
	}

	/**
	 * Inner static class for SearchAutocompleteSelect.
	 * 
	 * This class is responsible for filter search sensitive keys.
	 * 
	 * @author Gerard Szczepanski
	 *
	 */
	private static class SelectKeyFilter {

		private final Set<KeyCode> allowedKeys;

		private SelectKeyFilter() {
			this.allowedKeys = new HashSet<>();
			registerAllowedkKeys();
		}

		private void registerAllowedkKeys() {
			allowedKeys.add(KeyCode.DIGIT0);
			allowedKeys.add(KeyCode.DIGIT1);
			allowedKeys.add(KeyCode.DIGIT2);
			allowedKeys.add(KeyCode.DIGIT3);
			allowedKeys.add(KeyCode.DIGIT4);
			allowedKeys.add(KeyCode.DIGIT5);
			allowedKeys.add(KeyCode.DIGIT6);
			allowedKeys.add(KeyCode.DIGIT7);
			allowedKeys.add(KeyCode.DIGIT8);
			allowedKeys.add(KeyCode.DIGIT9);
			allowedKeys.add(KeyCode.Q);
			allowedKeys.add(KeyCode.W);
			allowedKeys.add(KeyCode.E);
			allowedKeys.add(KeyCode.R);
			allowedKeys.add(KeyCode.T);
			allowedKeys.add(KeyCode.Y);
			allowedKeys.add(KeyCode.U);
			allowedKeys.add(KeyCode.I);
			allowedKeys.add(KeyCode.O);
			allowedKeys.add(KeyCode.P);
			allowedKeys.add(KeyCode.A);
			allowedKeys.add(KeyCode.S);
			allowedKeys.add(KeyCode.D);
			allowedKeys.add(KeyCode.F);
			allowedKeys.add(KeyCode.G);
			allowedKeys.add(KeyCode.H);
			allowedKeys.add(KeyCode.J);
			allowedKeys.add(KeyCode.K);
			allowedKeys.add(KeyCode.L);
			allowedKeys.add(KeyCode.Z);
			allowedKeys.add(KeyCode.X);
			allowedKeys.add(KeyCode.C);
			allowedKeys.add(KeyCode.V);
			allowedKeys.add(KeyCode.B);
			allowedKeys.add(KeyCode.N);
			allowedKeys.add(KeyCode.M);
			allowedKeys.add(KeyCode.SPACE);
			allowedKeys.add(KeyCode.BACK_SPACE);
		}

		public boolean isKeyAllowedForSearch(KeyCode keyCode) {
			return allowedKeys.contains(keyCode);
		}

		public boolean isKeyAllowedForRun(KeyCode keyCode) {
			return keyCode == KeyCode.ENTER;
		}

		public boolean isKeyAllowedForClear(KeyCode keyCode) {
			return keyCode == KeyCode.ESCAPE;
		}
	}

	private static class InnerSelectCallback implements Callback<ListView<SearchResult>, ListCell<SearchResult>> {
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

	private class InnerSelectStringConverter extends StringConverter<SearchResult> {

		@Override
		public String toString(SearchResult object) {
			return object == null ? null : object.toString();
		}

		@Override
		public SearchResult fromString(String string) {
			return innerSelect.getItems().stream()
					.filter(item -> item.getSearchResultRepresentation().getSearchresultTitle().equals(string))
					.findFirst().orElse(null);
		}
	}

}
