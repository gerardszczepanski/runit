package szczepanski.gerard.runnit.view.controller;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.service.SettingsWriter;

@RequiredArgsConstructor
public class SettingsSceneController {
	private static final Logger LOG = Logger.getLogger(SettingsSceneController.class);
	
	private final SettingsLoader settingsLoader; 
	private final SettingsWriter settingsWriter;
	
	@FXML
	private SettingsGeneralPaneTabController settingsGeneralTabPaneController;
	
	@FXML
	public void initialize() {
		LOG.debug("Initialize");
	}
	
}
