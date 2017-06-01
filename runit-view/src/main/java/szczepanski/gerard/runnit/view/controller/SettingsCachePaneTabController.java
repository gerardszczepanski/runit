package szczepanski.gerard.runnit.view.controller;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.search.service.cache.CacheVisitor;
import szczepanski.gerard.runit.search.service.cache.impl.CacheSearchTermsCountingVisitor;
import szczepanski.gerard.runnit.view.util.DialogDisplayer;

@RequiredArgsConstructor
public class SettingsCachePaneTabController extends AbstractSettingsTabController {
	private static final Logger LOG = Logger.getLogger(SettingsCachePaneTabController.class);

	private final Cache cache;

	@FXML
	private Label cachedSearchTermsLabel;

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
		CacheVisitor<Integer> searchTermSizeVisitor = new CacheSearchTermsCountingVisitor();
		searchTermSizeVisitor.visit(cache);
		cachedSearchTermsLabel.setText(searchTermSizeVisitor.getResults().toString());
	}

}
