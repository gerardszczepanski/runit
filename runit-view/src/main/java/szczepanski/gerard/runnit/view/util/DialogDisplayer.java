package szczepanski.gerard.runnit.view.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.config.ProgramConfig;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DialogDisplayer {

	private static final Image ICON = new Image(DialogDisplayer.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH));
	
	public static Optional<String> showInputDialog(Window parent, String title) {
		TextInputDialog inputDialog = new TextInputDialog("");
		inputDialog.initOwner(parent);
		inputDialog.setTitle(title);
		inputDialog.setContentText(title);
		inputDialog.setHeaderText(null);
		inputDialog.setGraphic(null);
		
		return inputDialog.showAndWait();
	}
	
	public static void showValidationMessageDialog(String message) {
		Alert validationDialogAlert = new Alert(AlertType.WARNING);
		validationDialogAlert.setTitle("Validation failure");
		validationDialogAlert.setHeaderText(message);
		
		Stage stage = (Stage) validationDialogAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ICON);

		validationDialogAlert.showAndWait();
	}
	
	public static void showConfirmationMessageDialog(String message) {
		Alert validationDialogAlert = new Alert(AlertType.CONFIRMATION);
		validationDialogAlert.setTitle("Confirmation");
		validationDialogAlert.setHeaderText(message);
		
		Stage stage = (Stage) validationDialogAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ICON);

		validationDialogAlert.showAndWait();
	}

}
