package szczepanski.gerard.runnit.view.controller;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import szczepanski.gerard.runnit.view.tray.ProgramTrayManager;

public abstract class AbstractSettingsTabController extends AbstractController {
	private static final Logger LOG = Logger.getLogger(AbstractSettingsTabController.class);

	@FXML
	private Button cancelButton;
	
	@FXML
	public void handleCancelButton() {
		LOG.debug("handleCancelButton");
		Stage settingsStage = getStage(cancelButton);
		ProgramTrayManager.setAlwaysOnTop(true);
		settingsStage.close();
	}
	
	public abstract void reloadTab();
}
