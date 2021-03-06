package szczepanski.gerard.runit.view.component;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.Setter;
import org.apache.log4j.Logger;
import rx.observables.JavaFxObservable;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.SearchResultRepresentation;
import szczepanski.gerard.runit.search.service.service.SearchService;
import szczepanski.gerard.runit.view.tray.ProgramTrayManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This is Autocomplete Select component responsible for handle typed text, and
 * display typed text results.
 * <p>
 * This object requires {@code SearchService} type object, to fire search and
 * return result.
 *
 * @author Gerard Szczepanski
 */
public class SearchAutocompleteSelect {
    private static final Logger LOG = Logger.getLogger(SearchAutocompleteSelect.class);

    private static final int DELAY_TYPE_TIME_FOR_TRIGGER_SEARCH_IN_MS = ProgramConfig.DELAY_TYPE_TIME_FOR_TRIGGER_SEARCH_IN_MS;

    private final RunitComboBox<SearchResult> innerSelect;
    private final SelectKeyFilter selectKeyFilter;

    @Setter
    private SearchService searchService;

    private SearchAutocompleteSelect(Pane currentPane, Dimension dimenstion, Position position) {
        this.innerSelect = new RunitComboBox<>();
        this.selectKeyFilter = new SelectKeyFilter();
        initInnerTextField(currentPane, dimenstion, position);
    }

    /**
     * Factory method for create SearchAutocomplete Select.
     *
     * @param currentPane - pane for SearchAutocompleteSelect object after create.
     * @param dimenstion  - dimension for SearchAutocompleteSelect object.
     * @param position    - position for SearchAutocompleteSelect object.
     */
    public static SearchAutocompleteSelect createSearchAutocompleteSelect(Pane currentPane, Dimension dimenstion, Position position) {
        return new SearchAutocompleteSelect(currentPane, dimenstion, position);
    }

    private void search() {
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
        innerSelect.applyNewOptions(searchResults);

        if (!searchResults.isEmpty()) {
            innerSelect.show();
        }
    }

    private void hideOptions() {
        innerSelect.clear();
        innerSelect.hide();
    }

    private void initInnerTextField(Pane pane, Dimension dimenstion, Position position) {
        innerSelect.setPrefWidth(dimenstion.width);
        innerSelect.setPrefHeight(dimenstion.height);
        innerSelect.setLayoutX(position.x);
        innerSelect.setLayoutY(position.y);
        innerSelect.setEditable(true);
        innerSelect.setCellFactory(new InnerSelectCallback());
        innerSelect.setConverter(new InnerSelectStringConverter());
        innerSelect.setCache(false);
        innerSelect.setVisibleRowCount(10);

        innerSelect.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (selectKeyFilter.isKeyAllowedForRun(e.getCode())) {
                triggerRun();
            }
        });

        innerSelect.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (selectKeyFilter.isKeyAllowedForClear(e.getCode())) {
                hideOptions();
            }
        });

        initSelectTypeDelaySubscriber();

        Platform.runLater(() -> {
            innerSelect.getEditor().requestFocus();
            innerSelect.requestFocus();
        });

        pane.getChildren().add(innerSelect);
    }

    private void initSelectTypeDelaySubscriber() {
        JavaFxObservable.eventsOf(innerSelect, KeyEvent.KEY_RELEASED).filter(e -> selectKeyFilter.isKeyAllowedForSearch(e.getCode()))
                .debounce(DELAY_TYPE_TIME_FOR_TRIGGER_SEARCH_IN_MS, TimeUnit.MILLISECONDS).subscribe(e -> {
            LOG.debug("SEARCH START");
            Platform.runLater(this::search);
            LOG.debug("SEARCH FINISHED");
        });
    }

    /**
     * Inner static class for SearchAutocompleteSelect.
     * <p>
     * This class is responsible for filter search sensitive keys.
     *
     * @author Gerard Szczepanski
     */
    private static class SelectKeyFilter {

        private final Set<KeyCode> allowedKeys;

        private SelectKeyFilter() {
            this.allowedKeys = new HashSet<>();
            registerAllowedkKeys();
        }

        private void registerAllowedkKeys() {
            allowedKeys.addAll(AllowedTypedKeysProvider.getAllowedKeyCodes());
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

                private static final double PREFERED_ICON_SIZE = 16;

                @Override
                protected void updateItem(SearchResult item, boolean empty) {
                    super.updateItem(item, empty);

                    if (!isEmpty()) {
                        SearchResultRepresentation searchResultRepresentation = item.getSearchResultRepresentation();
                        ImageView imgView = new ImageView(searchResultRepresentation.getImage());
                        imgView.setFitWidth(PREFERED_ICON_SIZE);
                        imgView.setFitHeight(PREFERED_ICON_SIZE);
                        setGraphic(imgView);
                        setText(searchResultRepresentation.getSearchResultTitle());
                    }
                }
            };
        }
    }

    ;

    private class InnerSelectStringConverter extends StringConverter<SearchResult> {

        @Override
        public String toString(SearchResult object) {
            return object == null ? null : object.toString();
        }

        @Override
        public SearchResult fromString(String string) {
            return innerSelect.getItems().stream().filter(item -> item.getSearchResultRepresentation().getSearchResultTitle().equals(string)).findFirst()
                    .orElse(null);
        }
    }

}
