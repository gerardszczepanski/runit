package szczepanski.gerard.runnit.view.scene.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
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
			Platform.setImplicitExit(false);
			java.awt.Toolkit.getDefaultToolkit();

			if (!java.awt.SystemTray.isSupported()) {
				LOG.debug("System tray is not supported. Application is being closed");
				Platform.exit();
			}

			SystemTray tray = SystemTray.getSystemTray();
			Dimension trayDim = tray.getTrayIconSize();
			InputStream trayImageInputStream = ProgramTrayManager.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH);
			Image trayImage = ImageIO.read(trayImageInputStream);
			trayImage = trayImage.getScaledInstance(trayDim.width, trayDim.height, Image.SCALE_SMOOTH);
			TrayIcon trayIcon = new TrayIcon(trayImage, ProgramConfig.PROGRAM_TITLE);

			trayIcon.addActionListener(event -> {
				Platform.runLater(() -> {
					LOG.debug("Show Program from Tray");
					showStage(stage);
				});
			});

			MenuItem exitItem = new MenuItem("Close");
			exitItem.addActionListener(event -> {
				LOG.debug("Close application");
				Platform.exit();
				tray.remove(trayIcon);
			});

			final PopupMenu popup = new PopupMenu();
			popup.add(exitItem);
			trayIcon.setPopupMenu(popup);

			tray.add(trayIcon);
		} catch (java.awt.AWTException | IOException e) {
			throw new RunitRuntimeException("Unable to install tray icon", e);
		}
	}

	private static void showStage(Stage stage) {
		stage.show();
		stage.toFront();
	}

}
