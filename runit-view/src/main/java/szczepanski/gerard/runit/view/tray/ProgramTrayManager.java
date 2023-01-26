package szczepanski.gerard.runit.view.tray;

import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
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
        PopupMenu trayPopupMenu = TrayPopupMenuFactory.crateTrayPopupMenu(tray, trayIcon);
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

        trayIcon.addActionListener(event -> Platform.runLater(ProgramTrayManager::showProgramStage));

        return trayIcon;
    }

    private static Image loadTrayImage(Dimension trayDim) {
        InputStream trayImageInputStream = ProgramTrayManager.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH);
        Image trayImage = loadTrayImageFromInputStream(trayImageInputStream);
        trayImage = trayImage.getScaledInstance(trayDim.width, trayDim.height, Image.SCALE_SMOOTH);
        return trayImage;
    }

    private static Image loadTrayImageFromInputStream(InputStream in) {
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new RunitRuntimeException(ExceptionCode.R_003, e);
        }
    }

    public static void showProgramStage() {
        LOG.debug("Show Program from Tray");
        isProgramMinimizedInTray = false;
        programStage.show();
        programStage.toFront();
        setAlwaysOnTop(true);
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

    public static void setAlwaysOnTop(boolean alwaysOnTop) {
        programStage.setAlwaysOnTop(alwaysOnTop);
    }
}
