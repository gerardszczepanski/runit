package szczepanski.gerard.runnit.view.controller;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.service.SettingsWriter;

@RequiredArgsConstructor
public class SettingsSceneController {
	private static final Logger LOG = Logger.getLogger(SettingsSceneController.class);
	
	private final SettingsLoader settingsLoader; 
	private final SettingsWriter settingsWriter;
	
	@FXML
	private ListView<String> rootPathsListView;
	
	@FXML
	private ListView<String> fileExtensonsListView;
	
	
	@FXML
	public void addRootPath() {
		LOG.debug("addRootPath");
	}
	
	@FXML
	public void removeRootPath() {
		LOG.debug("removeRootPath");
		
	}
	
	@FXML
	public void addFileExtension() {
		LOG.debug("addFileExtension");
	}
	
	@FXML
	public void removeFileExtension() {
		LOG.debug("removeFileExtension");
		
	}
	
}
