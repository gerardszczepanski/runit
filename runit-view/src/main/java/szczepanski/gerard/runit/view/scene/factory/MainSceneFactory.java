package szczepanski.gerard.runit.view.scene.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.view.factory.FxmlComponentFactory;

import java.io.IOException;

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
            rootLayout = loader.load();
            return new Scene(rootLayout);
        } catch (IOException e) {
            throw new RunitRuntimeException(ExceptionCode.R_005, e);
        }
    }

}
