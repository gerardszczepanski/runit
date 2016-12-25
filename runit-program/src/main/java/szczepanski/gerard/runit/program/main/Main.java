package szczepanski.gerard.runit.program.main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.program.config.DependenciesConfig;
import szczepanski.gerard.runnit.view.scene.factory.MainSceneFactory;

public class Main extends Application {
	private static final Logger LOG = Logger.getLogger(Main.class);
	
	private static ApplicationContext ctx;
	
	public static void main(String... args) {
		initComponents();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Platform.setImplicitExit(false);
		MainSceneFactory mainSceneFactory = ctx.getBean(MainSceneFactory.class);
		
		primaryStage.setTitle(ProgramConfig.PROGRAM_TITLE);
		primaryStage.setResizable(false);
		primaryStage.setScene(mainSceneFactory.createComponent());
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH)));
		primaryStage.show();
	}
	
	private static final void initComponents() {
		LOG.debug("Loading Spring beans");
		ctx = new AnnotationConfigApplicationContext(DependenciesConfig.class);
	}

}
