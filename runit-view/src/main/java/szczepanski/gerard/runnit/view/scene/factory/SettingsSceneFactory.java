
package szczepanski.gerard.runnit.view.scene.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

public class SettingsSceneFactory extends FxmlComponentFactory<Scene> {
	private static final String PATH = "/templates/scenes/SettingsScene.fxml";

	private final Object settingsSceneController;

	public SettingsSceneFactory(Object settingsSceneController) {
		super(PATH);
		this.settingsSceneController = settingsSceneController;
	}

	@Override
	protected Scene generateFxmlComponent(FXMLLoader loader) {
		loader.setController(settingsSceneController);
		TabPane rootLayout;
		try {
			rootLayout = (TabPane) loader.load();
			return new Scene(rootLayout);
		} catch (IOException e) {
			throw new RunitRuntimeException(ExceptionCode.R_005, e);
		}
	}

}
