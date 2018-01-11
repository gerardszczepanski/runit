package szczepanski.gerard.runnit.view.tab.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runnit.view.controller.AbstractSettingsTabController;
import szczepanski.gerard.runnit.view.controller.mediator.TabsMediator;
import szczepanski.gerard.runnit.view.factory.FxmlComponentFactory;

import java.io.IOException;

public class SettingsDirTabFactory extends FxmlComponentFactory<Tab> {

    private static final String PATH = "/templates/panes/SettingsDirectoriesTabPane.fxml";
    private static final String TAB_TITLE = "Directory aliases";
    private final AbstractSettingsTabController settingsDirTabController;

    public SettingsDirTabFactory(AbstractSettingsTabController settingsDirTabController) {
        super(PATH);
        this.settingsDirTabController = settingsDirTabController;
    }

    @Override
    protected Tab generateFxmlComponent(FXMLLoader loader) {
        loader.setController(settingsDirTabController);
        TabsMediator.registerController(settingsDirTabController);

        Pane tabContentPane;
        try {
            tabContentPane = (Pane) loader.load();
            Tab tab = new Tab(TAB_TITLE);
            tab.setContent(tabContentPane);
            return tab;
        } catch (IOException e) {
            throw new RunitRuntimeException(ExceptionCode.R_005, e);
        }
    }

}
