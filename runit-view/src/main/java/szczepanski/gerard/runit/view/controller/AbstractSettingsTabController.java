package szczepanski.gerard.runit.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.view.tray.ProgramTrayManager;

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
