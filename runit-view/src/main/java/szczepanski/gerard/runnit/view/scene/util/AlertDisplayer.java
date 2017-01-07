package szczepanski.gerard.runnit.view.scene.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.config.ProgramConfig;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertDisplayer {
	
	private static final String TITLE = "Runit Error";
	private static final Image ALERT_ICON = new Image(AlertDisplayer.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH));
	
	public static void showAlert(String code, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(TITLE);
		alert.setGraphic(new ImageView(ALERT_ICON));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ALERT_ICON);

		alert.setHeaderText(code);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
}
