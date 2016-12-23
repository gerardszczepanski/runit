package szczepanski.gerard.runit.program.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.program.config.DependenciesConfig;
import szczepanski.gerard.runnit.view.scene.factory.MainSceneFactory;

public class Main extends Application {
	
	private static ApplicationContext ctx;
	
	public static void main(String... args) {
		initComponents();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainSceneFactory mainSceneFactory = ctx.getBean(MainSceneFactory.class);
		
		primaryStage.setTitle(ProgramConfig.PROGRAM_TITLE);
		primaryStage.setResizable(false);
		primaryStage.setScene(mainSceneFactory.createComponent());
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH)));
		primaryStage.show();
	}
	
	private static final void initComponents() {
		ctx = new AnnotationConfigApplicationContext(DependenciesConfig.class);
	}

}
