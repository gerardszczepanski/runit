package szczepanski.gerard.runnit.view.controller;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.validator.Validator;
import szczepanski.gerard.runit.settings.service.writer.SettingsWriter;

@RequiredArgsConstructor
public class SettingsWebPaneTabController extends AbstractController {
	private static final Logger LOG = Logger.getLogger(SettingsWebPaneTabController.class);

	private final SettingsLoader settingsLoader;
	private final SettingsWriter settingsWriter;
	private final Validator<Alias> webAliasValidator;
	@FXML
	private TableView<Alias> webAliasTableView;

	@FXML
	public void initialize() {
		LOG.debug("Initialize");
		Settings settings = settingsLoader.loadSettings();
		updateWebAliases(settings);
	}

	private void updateWebAliases(Settings settings) {
		
	}

	@FXML
	public void handleAddWebAlias() {
		LOG.debug("handleAddWebAlias");
	}

	@FXML
	public void handleRemoveWebAlias() {
		LOG.debug("handleRemoveWebAlias");
		
	}

	@FXML
	public void handleSaveGeneralTab() {
		LOG.debug("handleSaveGeneralTab");
	}
	
	@FXML
	public void handleCancelButton() {
		LOG.debug("handleCancelButton");
		Stage settingsStage = getStage(webAliasTableView);
		settingsStage.close();
	}

}
