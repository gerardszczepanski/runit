package szczepanski.gerard.runit.program.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.program.config.DependenciesConfig;
import szczepanski.gerard.runit.program.registry.WindowsStartupInjector;
import szczepanski.gerard.runit.program.util.ProgramExceptionHandler;
import szczepanski.gerard.runit.program.util.ProgramHotKeyListener;
import szczepanski.gerard.runit.program.util.ProgramWindowMoveListener;
import szczepanski.gerard.runit.view.scene.factory.MainSceneFactory;
import szczepanski.gerard.runit.view.tray.ProgramTrayManager;
import szczepanski.gerard.runit.view.util.ProgramSettingsManager;

import javax.swing.*;

public class Main extends Application {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private static ApplicationContext ctx;

    public static void main(String... args) {
        initComponents();
        launch(args);
    }

    private static void handleException(Thread t, Throwable e) {
        ProgramExceptionHandler.handleException(e);
    }

    private static void initComponents() {
        LOG.debug("Loading Spring beans");
        ctx = new AnnotationConfigApplicationContext(DependenciesConfig.class);
    }

    private static void loadSettingsOnProgramStart() {
        LOG.debug("Initial Loading Settings");
        ProgramSettingsManager programSettingsManager = ctx.getBean(ProgramSettingsManager.class);
        programSettingsManager.loadSettings();
    }

    private static void injectRunitToWindowsStartupRegistry() {
        LOG.debug("Add Runit to Windows startup registry");
        WindowsStartupInjector injector = new WindowsStartupInjector();
        injector.addRunitToStartupPrograms();
    }

    @Override
    public void start(Stage primaryStage) {
        Thread.setDefaultUncaughtExceptionHandler(Main::handleException);
        MainSceneFactory mainSceneFactory = ctx.getBean(MainSceneFactory.class);
        Scene mainScene = mainSceneFactory.createComponent();

        SwingUtilities.invokeLater(() -> {
            ProgramTrayManager.installInSystemTray(primaryStage);
            ProgramHotKeyListener.getInstance().registerProgramHotKey();
            ProgramWindowMoveListener.makeStageDraggable(primaryStage, (Pane) mainScene.getRoot());
            loadSettingsOnProgramStart();
            injectRunitToWindowsStartupRegistry();
        });

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle(ProgramConfig.PROGRAM_TITLE);
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH)));
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

}
