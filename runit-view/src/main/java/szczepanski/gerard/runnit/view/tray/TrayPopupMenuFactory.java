package szczepanski.gerard.runnit.view.tray;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.search.service.util.WebPageRunner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
		showProgramItem.addActionListener(event -> {
			Platform.runLater(() -> {
				ProgramTrayManager.showProgramStage();
			});
		});

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
