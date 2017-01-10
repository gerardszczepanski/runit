package szczepanski.gerard.runnit.view.tab.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runnit.view.factory.FxmlComponentFactory;

public class SettingsGeneralTabFactory extends FxmlComponentFactory<Tab>{
	
	private static final String PATH = "/templates/panes/SettingsGeneralTabPane.fxml";
	private static final String TAB_TITLE = "General";
	private final Object settingsGeneralTabController;
	
	public SettingsGeneralTabFactory(Object settingsGeneralTabController) {
		super(PATH);
		this.settingsGeneralTabController = settingsGeneralTabController;
	}

	@Override
	protected Tab generateFxmlComponent(FXMLLoader loader) {
		loader.setController(settingsGeneralTabController);
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
