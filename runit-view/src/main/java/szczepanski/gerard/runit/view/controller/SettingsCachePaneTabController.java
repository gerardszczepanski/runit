package szczepanski.gerard.runit.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.search.service.cache.CacheVisitor;
import szczepanski.gerard.runit.search.service.cache.impl.CacheReportVisitor;
import szczepanski.gerard.runit.search.service.cache.report.CacheReport;
import szczepanski.gerard.runit.search.service.cache.report.CachedSearchTermInfo;
import szczepanski.gerard.runit.view.util.DialogDisplayer;

@RequiredArgsConstructor
public class SettingsCachePaneTabController extends AbstractSettingsTabController {
    private static final Logger LOG = Logger.getLogger(SettingsCachePaneTabController.class);

    private static final String PERCENT_SIGN = "%";

    private final Cache cache;

    @FXML
    private Label cachedSearchTermsLabel;
    @FXML
    private Label cacheOccupancyLabel;
    @FXML
    private TableView<CachedSearchTermInfo> cacheTableView;
    @FXML
    private TableColumn<CachedSearchTermInfo, String> searchTermColumn;
    @FXML
    private TableColumn<CachedSearchTermInfo, Integer> resultsFoundColumn;
    @FXML
    private TableColumn<CachedSearchTermInfo, String> cacheDateColumn;

    @FXML
    public void initialize() {
        reloadTab();
    }

    @FXML
    public void handleClearCache() {
        LOG.debug("Clear cache");
        cache.clear();
        reloadTab();
        DialogDisplayer.showConfirmationMessageDialog("Cache has been cleared");
    }

    @Override
    public void reloadTab() {
        CacheVisitor<CacheReport> reportVisitor = new CacheReportVisitor();
        reportVisitor.visit(cache);

        CacheReport cacheReport = reportVisitor.getResults();
        cachedSearchTermsLabel.setText(cacheReport.getCachedSearchTermsNumber().toString());
        cacheOccupancyLabel.setText(cacheReport.getCacheOccupancyInPercent() + PERCENT_SIGN);
        updateCachedTable(cacheReport);
    }

    private void updateCachedTable(CacheReport cacheReport) {
        configureTableView();
        cacheTableView.getItems().clear();
        cacheTableView.getItems().addAll(cacheReport.getCachedSearchTerms());
    }

    private void configureTableView() {
        searchTermColumn.setCellValueFactory(new PropertyValueFactory<CachedSearchTermInfo, String>("searchTerm"));
        resultsFoundColumn.setCellValueFactory(new PropertyValueFactory<CachedSearchTermInfo, Integer>("results"));
        cacheDateColumn.setCellValueFactory(new PropertyValueFactory<CachedSearchTermInfo, String>("cachedDate"));
    }

}
