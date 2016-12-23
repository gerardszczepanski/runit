package szczepanski.gerard.runnit.view.scene.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

public class MainSceneFactory extends FxmlComponentFactory<Scene>{
	private static final String PATH = "/templates/scenes/MainScene.fxml";
	
	private final Object mainSceneController;
	
	public MainSceneFactory(Object mainSceneController) {
		super(PATH);
		this.mainSceneController = mainSceneController;
	}

	@Override
	protected Scene generateFxmlComponent(FXMLLoader loader) {
		try {
			loader.setController(mainSceneController);
			Pane rootLayout = (Pane) loader.load();
			return new Scene(rootLayout);
		} catch (IOException e) {
			throw new RunitRuntimeException("Unable to load MainScene", e);
		}
	}
	
}
