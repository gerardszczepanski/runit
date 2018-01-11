package szczepanski.gerard.runit.view.scene.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.view.factory.FxmlComponentFactory;

import java.io.IOException;
import java.util.Collection;

public class SettingsSceneFactory extends FxmlComponentFactory<Scene> {
    private static final String PATH = "/templates/scenes/SettingsScene.fxml";

    private final Collection<FxmlComponentFactory<Tab>> tabFactories;

    public SettingsSceneFactory(Collection<FxmlComponentFactory<Tab>> tabFactories) {
        super(PATH);
        this.tabFactories = tabFactories;
    }

    @Override
    protected Scene generateFxmlComponent(FXMLLoader loader) {
        TabPane rootLayout;
        try {
            rootLayout = (TabPane) loader.load();
            addTabsToTabPane(rootLayout);
            return new Scene(rootLayout);
        } catch (IOException e) {
            throw new RunitRuntimeException(ExceptionCode.R_005, e);
        }
    }

    private void addTabsToTabPane(TabPane rootLayout) {
        tabFactories.forEach(f -> {
            Tab tab = f.createComponent();
            rootLayout.getTabs().add(tab);
        });
    }

}
