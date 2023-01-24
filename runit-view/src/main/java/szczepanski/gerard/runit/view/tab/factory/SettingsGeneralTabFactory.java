package szczepanski.gerard.runit.view.tab.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.view.controller.AbstractSettingsTabController;
import szczepanski.gerard.runit.view.controller.mediator.TabsMediator;
import szczepanski.gerard.runit.view.factory.FxmlComponentFactory;

import java.io.IOException;

public class SettingsGeneralTabFactory extends FxmlComponentFactory<Tab> {

    private static final String PATH = "/templates/panes/SettingsGeneralTabPane.fxml";
    private static final String TAB_TITLE = "General";
    private final AbstractSettingsTabController settingsGeneralTabController;

    public SettingsGeneralTabFactory(AbstractSettingsTabController settingsGeneralTabController) {
        super(PATH);
        this.settingsGeneralTabController = settingsGeneralTabController;
    }

    @Override
    protected Tab generateFxmlComponent(FXMLLoader loader) {
        loader.setController(settingsGeneralTabController);
        TabsMediator.registerController(settingsGeneralTabController);

        Pane tabContentPane;
        try {
            tabContentPane = loader.load();
            Tab tab = new Tab(TAB_TITLE);
            tab.setContent(tabContentPane);
            return tab;
        } catch (IOException e) {
            throw new RunitRuntimeException(ExceptionCode.R_005, e);
        }
    }

}
