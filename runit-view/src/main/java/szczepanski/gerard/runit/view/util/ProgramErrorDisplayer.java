package szczepanski.gerard.runit.view.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.view.tray.ProgramTrayManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramErrorDisplayer {

    private static final String TITLE = "Runit Error";
    private static final Image ALERT_ICON = new Image(ProgramErrorDisplayer.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH));

    public static void showError(String code, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(TITLE);
        alert.setGraphic(new ImageView(ALERT_ICON));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ALERT_ICON);

        alert.setHeaderText(code);
        alert.setContentText(message);

        alert.setOnShowing((e) -> ProgramTrayManager.setAlwaysOnTop(false));
        alert.setOnHiding((e) -> ProgramTrayManager.setAlwaysOnTop(true));

        alert.showAndWait();
    }

}
