package szczepanski.gerard.runnit.view.controller.mediator;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runnit.view.controller.AbstractSettingsTabController;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TabsMediator {

	private static final List<AbstractSettingsTabController> settingsTabsContollers = new ObjectArrayList<>();

	public static void registerController(AbstractSettingsTabController controller) {
		settingsTabsContollers.add(controller);
	}

	public static void updateTabs() {
		settingsTabsContollers.forEach(AbstractSettingsTabController::reloadTab);
	}

}
