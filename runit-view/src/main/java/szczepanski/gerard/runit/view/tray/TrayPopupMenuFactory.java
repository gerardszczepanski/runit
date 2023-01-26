package szczepanski.gerard.runit.view.tray;

import javafx.application.Platform;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.search.util.WebPageRunner;

import java.awt.*;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class TrayPopupMenuFactory {
    private static final Logger LOG = Logger.getLogger(TrayPopupMenuFactory.class);

    public static PopupMenu crateTrayPopupMenu(SystemTray tray, TrayIcon trayIcon) {
        MenuItem showProgramItem = createShowProgramItem();
        MenuItem aboutProgramItem = createAboutProgramItem();
        MenuItem closeProgramItem = createExitProgramItem(tray, trayIcon);

        PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(showProgramItem);
        popupMenu.add(aboutProgramItem);
        popupMenu.addSeparator();
        popupMenu.add(closeProgramItem);

        return popupMenu;
    }

    private static MenuItem createShowProgramItem() {
        MenuItem showProgramItem = new MenuItem("Show/Hide (Alt + Q)");
        showProgramItem.addActionListener(event -> Platform.runLater(ProgramTrayManager::showProgramStage));

        return showProgramItem;
    }

    private static MenuItem createAboutProgramItem() {
        MenuItem aboutProgramItem = new MenuItem("About");
        aboutProgramItem.addActionListener(event -> {
            WebPageRunner.browsePage(ProgramConfig.PROGRAM_ABOUT_PAGE);
        });

        return aboutProgramItem;
    }

    private static MenuItem createExitProgramItem(SystemTray tray, TrayIcon trayIcon) {
        MenuItem exitProgramItem = new MenuItem("Exit");
        exitProgramItem.addActionListener(event -> {
            LOG.debug("Exit application");
            Platform.exit();
            tray.remove(trayIcon);
            System.exit(0);
        });

        return exitProgramItem;
    }

}
