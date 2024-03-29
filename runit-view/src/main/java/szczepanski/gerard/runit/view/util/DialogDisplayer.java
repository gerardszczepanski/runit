package szczepanski.gerard.runit.view.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.common.util.StringUtils;

import java.io.File;
import java.util.Optional;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class DialogDisplayer {

    private static final Image ICON = new Image(DialogDisplayer.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH));

    public static Optional<String> showInputDialog(Window parent, String title) {
        return showInputDialog(parent, title, StringUtils.EMPTY);
    }

    public static Optional<String> showInputDialog(Window parent, String title, String initialText) {
        TextInputDialog inputDialog = new TextInputDialog(initialText);
        inputDialog.initOwner(parent);
        inputDialog.setTitle(title);
        inputDialog.setContentText(title);
        inputDialog.setHeaderText(null);
        inputDialog.setGraphic(null);

        return inputDialog.showAndWait();
    }

    public static Optional<File> showDirectoryChooserDialog(Window parent, String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        File file = directoryChooser.showDialog(parent);
        return Optional.ofNullable(file);
    }

    public static void showValidationMessageDialog(String message) {
        Alert validationDialogAlert = new Alert(AlertType.WARNING);
        validationDialogAlert.setTitle("Validation failure");
        validationDialogAlert.setHeaderText(message);
        validationDialogAlert.setGraphic(null);

        addProgramIconToAlert(validationDialogAlert);
        validationDialogAlert.showAndWait();
    }

    public static void showConfirmationMessageDialog(String message) {
        Alert validationDialogAlert = new Alert(AlertType.CONFIRMATION);
        validationDialogAlert.setTitle("Confirmation");
        validationDialogAlert.setHeaderText(message);
        validationDialogAlert.setGraphic(null);
        validationDialogAlert.getButtonTypes().remove(ButtonType.CANCEL);

        addProgramIconToAlert(validationDialogAlert);
        validationDialogAlert.showAndWait();
    }

    private static void addProgramIconToAlert(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ICON);
    }

}
