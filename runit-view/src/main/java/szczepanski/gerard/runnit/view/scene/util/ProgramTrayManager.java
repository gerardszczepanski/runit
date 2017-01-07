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
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramTrayManager {
	private static final Logger LOG = Logger.getLogger(ProgramTrayManager.class);

	private static Stage programStage;
	private static boolean isProgramMinimizedInTray;

	public static void installInSystemTray(Stage stage) {
		try {
			LOG.debug("Setting Stage in System Tray");
			programStage = stage;
			installStageAsTray();
		} catch (java.awt.AWTException e) {
			throw new RunitRuntimeException(ExceptionCode.R_002, e);
		}
	}

	private static void installStageAsTray() throws AWTException {
		checkIfTraySupported();
		initComponents();

		SystemTray tray = SystemTray.getSystemTray();
		TrayIcon trayIcon = createTrayIcon(tray);
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

	private static TrayIcon createTrayIcon(SystemTray tray) {
		Dimension trayDim = tray.getTrayIconSize();
		Image trayImage = loadTrayImage(trayDim);
		TrayIcon trayIcon = new TrayIcon(trayImage, ProgramConfig.PROGRAM_TITLE);

		trayIcon.addActionListener(event -> {
			Platform.runLater(() -> {
				LOG.debug("Show Program from Tray");
				showProgramStage();
			});
		});

		return trayIcon;
	}

	private static Image loadTrayImage(Dimension trayDim) {
		InputStream trayImageInputStream = ProgramTrayManager.class
				.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH);
		Image trayImage = tryToLoadTrayImageFromInputStream(trayImageInputStream);
		trayImage = trayImage.getScaledInstance(trayDim.width, trayDim.height, Image.SCALE_SMOOTH);
		return trayImage;
	}

	private static Image tryToLoadTrayImageFromInputStream(InputStream in) {
		try {
			return ImageIO.read(in);
		} catch (IOException e) {
			throw new RunitRuntimeException(ExceptionCode.R_003, e);
		}
	}

	private static PopupMenu crateTrayPopupMenu(SystemTray tray, TrayIcon trayIcon) {
		MenuItem closeProgramItem = createExitProgramItem(tray, trayIcon);

		PopupMenu popupMenu = new PopupMenu();
		popupMenu.add(closeProgramItem);

		return popupMenu;
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

	public static void showProgramStage() {
		isProgramMinimizedInTray = false;
		programStage.show();
		programStage.toFront();
	}

	public static void hideProgramStage() {
		isProgramMinimizedInTray = true;
		programStage.hide();
	}

	public static void triggerTrayAction() {
		LOG.debug("Trigger Tray action");
		Platform.runLater(() -> {
			if (isProgramMinimizedInTray) {
				showProgramStage();
			} else {
				hideProgramStage();
			}
		});
	}
}
