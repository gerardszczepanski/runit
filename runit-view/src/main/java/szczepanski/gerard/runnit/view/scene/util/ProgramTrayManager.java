package szczepanski.gerard.runnit.view.scene.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramTrayManager {
	private static final Logger LOG = Logger.getLogger(ProgramTrayManager.class);

	public static void installAsTrayIcon(Stage stage) {
		try {
			installStageAsTray(stage);
		} catch (java.awt.AWTException e) {
			throw new RunitRuntimeException("Unable to install tray icon", e);
		}
	}
	
	private static void installStageAsTray(Stage stage) throws AWTException {
		checkIfTraySupported();
		initComponents();

		SystemTray tray = SystemTray.getSystemTray();
		TrayIcon trayIcon = createTrayIcon(tray, stage);
		PopupMenu trayPopupMenu = crateTrayPopupMenu(tray, trayIcon);
		trayIcon.setPopupMenu(trayPopupMenu);
		tray.add(trayIcon);
	}
	
	private static void checkIfTraySupported() {
		if (!SystemTray.isSupported()) {
			LOG.debug("System tray is not supported. Application is being closed");
			Platform.exit();
		}
	}
	
	private static void initComponents() {
		Platform.setImplicitExit(false);
		Toolkit.getDefaultToolkit();
	}
	
	private static TrayIcon createTrayIcon(SystemTray tray, Stage stage) {
		Dimension trayDim = tray.getTrayIconSize();
		Image trayImage = loadTrayImage(trayDim);
		TrayIcon trayIcon = new TrayIcon(trayImage, ProgramConfig.PROGRAM_TITLE);

		trayIcon.addActionListener(event -> {
			Platform.runLater(() -> {
				LOG.debug("Show Program from Tray");
				showStage(stage);
			});
		});
		
		return trayIcon;
	}
	
	private static Image loadTrayImage(Dimension trayDim) {
		InputStream trayImageInputStream = ProgramTrayManager.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH);
		Image trayImage = tryToLoadTrayImageFromInputStream(trayImageInputStream);
		trayImage = trayImage.getScaledInstance(trayDim.width, trayDim.height, Image.SCALE_SMOOTH);
		return trayImage;
	}
	
	private static Image tryToLoadTrayImageFromInputStream(InputStream in) {
		try {
			return ImageIO.read(in);
		} catch (IOException e) {
			throw new RunitRuntimeException("Unable to load TrayImage");
		}
	}
	
	private static PopupMenu crateTrayPopupMenu(SystemTray tray, TrayIcon trayIcon) {
		MenuItem closeProgramItem = createCloseProgramItem(tray, trayIcon);

		PopupMenu popupMenu = new PopupMenu();
		popupMenu.add(closeProgramItem);
		
		return popupMenu;
	}
	
	private static MenuItem createCloseProgramItem(SystemTray tray, TrayIcon trayIcon) {
		MenuItem closeProgramItem = new MenuItem("Close");
		closeProgramItem.addActionListener(event -> {
			LOG.debug("Close application");
			Platform.exit();
			tray.remove(trayIcon);
		});
		
		return closeProgramItem;
	}

	private static void showStage(Stage stage) {
		stage.show();
		stage.toFront();
	}
}
