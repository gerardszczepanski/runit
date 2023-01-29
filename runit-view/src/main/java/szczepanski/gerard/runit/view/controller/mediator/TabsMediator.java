package szczepanski.gerard.runit.view.controller.mediator;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.view.controller.AbstractSettingsTabController;

import java.util.List;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class TabsMediator {

    private static final List<AbstractSettingsTabController> settingsTabsControllers = new ObjectArrayList<>();

    public static void registerController(AbstractSettingsTabController controller) {
        settingsTabsControllers.add(controller);
    }

    public static void updateTabs() {
        settingsTabsControllers.forEach(AbstractSettingsTabController::reloadTab);
    }

}
