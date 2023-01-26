package szczepanski.gerard.runit.view.component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import lombok.SneakyThrows;
import lombok.Value;
import rx.observables.JavaFxObservable;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.search.result.SearchResult;
import szczepanski.gerard.runit.search.result.SearchResultRepresentation;
import szczepanski.gerard.runit.search.service.SearchService;
import szczepanski.gerard.runit.view.tray.ProgramTrayManager;

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

    private final ComboBox<SearchResult> innerSelect;

    private final SelectKeyFilter selectKeyFilter;

    @Setter
    private SearchService searchService;

    private SearchAutocompleteSelect() {
        this.selectKeyFilter = new SelectKeyFilter();
        this.innerSelect = new ComboBox<>();
    }

    private void initializeInnerSelect(
            SearchAutocompleteSelectProperties properties) {
        innerSelect.setPrefWidth(properties.dimension.width);
        innerSelect.setPrefHeight(properties.dimension.height);
        innerSelect.setLayoutX(properties.position.x);
        innerSelect.setLayoutY(properties.position.y);
        innerSelect.setEditable(true);
        innerSelect.setCellFactory(new InnerSelectCallback());
        innerSelect.setConverter(new InnerSelectStringConverter());
        innerSelect.setCache(false);
        innerSelect.setMouseTransparent(true);

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

    /**
     * Factory method for create SearchAutocomplete Select.
     *
     * @param currentPane - pane for SearchAutocompleteSelect object after create.
     * @param dimension   - dimension for SearchAutocompleteSelect object.
     * @param position    - position for SearchAutocompleteSelect object.
     */
    public static SearchAutocompleteSelect createSearchAutocompleteSelect(Pane currentPane, Dimension dimension, Position position) {
        final SearchAutocompleteSelectProperties properties = new SearchAutocompleteSelectProperties(currentPane, dimension, position);
        final SearchAutocompleteSelect searchAutocompleteSelect = new SearchAutocompleteSelect();
        searchAutocompleteSelect.initializeInnerSelect(properties);
        return searchAutocompleteSelect;
    }

    private void triggerSearchAction() {
        if (StringUtils.isNotEmpty(getSelectText())) {
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
        String searchTerm = getSelectText();
        return searchService.searchFor(searchTerm);
    }

    private void displaySearchResults(List<SearchResult> searchResults) {
        applyNewOptions(searchResults);

        if (!searchResults.isEmpty()) {
            innerSelect.show();
        }
    }

    private void hideWithEditorAndOptionsClear() {
        clearEditorAndOptions();
        innerSelect.hide();
    }

    private void hideWithOptionsClear() {
        clearCurrentOptions();
        innerSelect.hide();
    }

    public String getSelectText() {
        return innerSelect.getEditor().getText();
    }

    public void applyNewOptions(List<SearchResult> options) {
        clearCurrentOptions();
        insertNewOptions(options);
    }

    public void clearCurrentOptions() {
        innerSelect.getSelectionModel().clearSelection();
        innerSelect.getItems().removeAll(innerSelect.getItems());
        innerSelect.itemsProperty().get().clear();
        innerSelect.valueProperty().set(null);
    }

    @SneakyThrows
    private void insertNewOptions(List<SearchResult> options) {
        ObservableList<SearchResult> observableListOptions = FXCollections.observableArrayList(options);
        innerSelect.setItems(observableListOptions);
    }

    public void clearEditorAndOptions() {
        clearCurrentOptions();
        innerSelect.getEditor().clear();
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

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        setWidth(0);
                        setHeight(0);
                        return;
                    }

                    SearchResultRepresentation searchResultRepresentation = item.getSearchResultRepresentation();
                    ImageView imgView = new ImageView(searchResultRepresentation.getImage());
                    imgView.setFitWidth(ICON_SIZE);
                    imgView.setFitHeight(ICON_SIZE);
                    setGraphic(imgView);
                    setText(searchResultRepresentation.getSearchResultTitle());
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
            return innerSelect.getItems().stream().filter(
                    item -> item.getSearchResultRepresentation().getSearchResultTitle().equals(string)).findFirst()
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
