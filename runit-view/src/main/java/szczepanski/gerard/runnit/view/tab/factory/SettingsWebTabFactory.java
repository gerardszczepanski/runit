package szczepanski.gerard.runnit.view.tab.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runnit.view.controller.AbstractSettingsTabController;
import szczepanski.gerard.runnit.view.controller.mediator.TabsMediator;
import szczepanski.gerard.runnit.view.factory.FxmlComponentFactory;

public class SettingsWebTabFactory extends FxmlComponentFactory<Tab>{
	
	private static final String PATH = "/templates/panes/SettingsWebTabPane.fxml";
	private static final String TAB_TITLE = "Web aliases";
	private final AbstractSettingsTabController settingsWebTabController;
	
	public SettingsWebTabFactory(AbstractSettingsTabController settingsWebTabController) {
		super(PATH);
		this.settingsWebTabController = settingsWebTabController;
	}

	@Override
	protected Tab generateFxmlComponent(FXMLLoader loader) {
		loader.setController(settingsWebTabController);
		TabsMediator.registerController(settingsWebTabController);
		
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
