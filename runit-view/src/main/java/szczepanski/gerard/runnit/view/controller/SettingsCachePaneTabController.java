package szczepanski.gerard.runnit.view.controller;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runnit.view.util.DialogDisplayer;

@RequiredArgsConstructor
public class SettingsCachePaneTabController extends AbstractSettingsTabController {
	private static final Logger LOG = Logger.getLogger(SettingsCachePaneTabController.class);

	private final Cache cache;

	@FXML
	private TableView<Alias> webAliasTableView;

	@FXML
	private TableColumn<Alias, String> aliasColumn;

	@FXML
	private TableColumn<Alias, String> webAddressColumn;

	@FXML
	public void handleClearCache() {
		LOG.debug("Clear cache");
		cache.clear();
		DialogDisplayer.showConfirmationMessageDialog("Cache has been cleared");
	}

	@Override
	public void reloadTab() {
		// Do nothing (ISP violation :< )
	}

}
