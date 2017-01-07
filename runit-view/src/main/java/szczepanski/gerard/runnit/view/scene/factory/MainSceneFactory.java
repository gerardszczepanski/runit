package szczepanski.gerard.runnit.view.scene.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

public class MainSceneFactory extends FxmlComponentFactory<Scene> {
	private static final String PATH = "/templates/scenes/MainScene.fxml";

	private final Object mainSceneController;

	public MainSceneFactory(Object mainSceneController) {
		super(PATH);
		this.mainSceneController = mainSceneController;
	}

	@Override
	protected Scene generateFxmlComponent(FXMLLoader loader) {
		loader.setController(mainSceneController);
		Pane rootLayout;
		try {
			rootLayout = (Pane) loader.load();
			return new Scene(rootLayout);
		} catch (IOException e) {
			throw new RunitRuntimeException(ExceptionCode.R_005, e);
		}
	}

}
