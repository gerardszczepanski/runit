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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import lombok.Value;
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

    private RunitComboBox<SearchResult> innerSelect;
    private final SearchAutocompleteSelectProperties properties;
    private final SelectKeyFilter selectKeyFilter;

    @Setter
    private SearchService searchService;

    private SearchAutocompleteSelect(SearchAutocompleteSelectProperties properties) {
        this.properties = properties;
        this.selectKeyFilter = new SelectKeyFilter();
        this.innerSelect = new RunitComboBox<>();
        initializeRunitComboBox();
    }

    /**
     * Factory method for create SearchAutocomplete Select.
     *
     * @param currentPane - pane for SearchAutocompleteSelect object after create.
     * @param dimension  - dimension for SearchAutocompleteSelect object.
     * @param position    - position for SearchAutocompleteSelect object.
     */
    public static SearchAutocompleteSelect createSearchAutocompleteSelect(Pane currentPane, Dimension dimension, Position position) {
        final SearchAutocompleteSelectProperties properties = new SearchAutocompleteSelectProperties(currentPane, dimension, position);
        return new SearchAutocompleteSelect(properties);
    }

    private void triggerSearchAction() {
        if (StringUtils.isNotEmpty(innerSelect.text())) {
            LOG.debug("--SEARCH START");
            search();
            LOG.debug("--SEARCH FINISHED");
        }
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
        String searchTerm = innerSelect.text();
        return searchService.searchFor(searchTerm);
    }

    private void displaySearchResults(List<SearchResult> searchResults) {
        innerSelect.applyNewOptions(searchResults);

        if (!searchResults.isEmpty()) {
            innerSelect.show();
        }
    }

    private void hideWithEditorAndOptionsClear() {
        innerSelect.clearEditorAndOptions();
        innerSelect.hide();
    }

    private void hideWithOptionsClear() {
        innerSelect.clearCurrentOptions();
        innerSelect.hide();
    }

    private void initializeRunitComboBox() {
        innerSelect.setPrefWidth(properties.dimension.width);
        innerSelect.setPrefHeight(properties.dimension.height);
        innerSelect.setLayoutX(properties.position.x);
        innerSelect.setLayoutY(properties.position.y);
        innerSelect.setEditable(true);
        innerSelect.setCellFactory(new InnerSelectCallback());
        innerSelect.setConverter(new InnerSelectStringConverter());
        innerSelect.setCache(false);
        innerSelect.setMouseTransparent(true);
        innerSelect.setVisibleRowCount(10);

        innerSelect.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (selectKeyFilter.isKeyAllowedForRun(e.getCode())) {
                triggerRun();
            }
        });

        innerSelect.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (selectKeyFilter.isKeyAllowedForTotalClear(e.getCode())) {
                hideWithEditorAndOptionsClear();
            }
        });

        innerSelect.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (selectKeyFilter.isKeyAllowedForOptionsClear(e.getCode())) {
                hideWithOptionsClear();
            }
        });

        innerSelect.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (selectKeyFilter.isKeyAllowedForOptionsClear(e.getCode())) {
                hideWithOptionsClear();
            }
        });

        initSelectTypeDelaySubscriber();

        Platform.runLater(() -> {
            innerSelect.getEditor().requestFocus();
            innerSelect.requestFocus();
        });

        properties.pane.getChildren().add(innerSelect);
    }

    private void initSelectTypeDelaySubscriber() {
        JavaFxObservable.eventsOf(innerSelect, KeyEvent.KEY_RELEASED).filter(e -> selectKeyFilter.isKeyAllowedForSearch(e.getCode()))
                .debounce(DELAY_TYPE_TIME_FOR_TRIGGER_SEARCH_IN_MS, TimeUnit.MILLISECONDS).subscribe(e -> {
            Platform.runLater(this::triggerSearchAction);
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
        private final Set<KeyCode> keysIneligibleForOptionsClear;

        private SelectKeyFilter() {
            this.allowedKeys = new HashSet<>();
            this.keysIneligibleForOptionsClear = new HashSet<>();
            registerAllowedKeys();
            registerKeysIneligibleForOptionsClear();
        }

        private void registerAllowedKeys() {
            allowedKeys.addAll(KeyboardSettings.getAllowedKeyCodes());
        }
        private void registerKeysIneligibleForOptionsClear() {
            keysIneligibleForOptionsClear.addAll(KeyboardSettings.getKeycodesIneligibleForOptionsClear());
        }

        public boolean isKeyAllowedForSearch(KeyCode keyCode) {
            return allowedKeys.contains(keyCode);
        }

        public boolean isKeyAllowedForRun(KeyCode keyCode) {
            return keyCode == KeyCode.ENTER;
        }

        public boolean isKeyAllowedForTotalClear(KeyCode keyCode) {
            return keyCode == KeyCode.ESCAPE;
        }

        public boolean isKeyAllowedForOptionsClear(KeyCode keyCode) {
            return !keysIneligibleForOptionsClear.contains(keyCode);
        }
    }

    private static class InnerSelectCallback implements Callback<ListView<SearchResult>, ListCell<SearchResult>> {
        @Override
        public ListCell<SearchResult> call(ListView<SearchResult> l) {
            return new ListCell<SearchResult>() {

                private static final double ICON_SIZE = 16;

                @Override
                protected void updateItem(SearchResult item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    }

                    if (!isEmpty()) {
                        SearchResultRepresentation searchResultRepresentation = item.getSearchResultRepresentation();
                        ImageView imgView = new ImageView(searchResultRepresentation.getImage());
                        imgView.setFitWidth(ICON_SIZE);
                        imgView.setFitHeight(ICON_SIZE);
                        setGraphic(imgView);
                        setText(searchResultRepresentation.getSearchResultTitle());
                    }
                }
            };
        }
    }

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

    @Value
    private static class SearchAutocompleteSelectProperties {

        Pane pane;
        Dimension dimension;
        Position position;

    }

}
