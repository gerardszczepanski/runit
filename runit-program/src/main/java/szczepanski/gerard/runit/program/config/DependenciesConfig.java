package szczepanski.gerard.runit.program.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import szczepanski.gerard.runit.service.service.SearchService;
import szczepanski.gerard.runit.service.service.impl.SearchServiceImpl;
import szczepanski.gerard.runnit.view.controller.MainSceneController;
import szczepanski.gerard.runnit.view.scene.factory.MainSceneFactory;

@Configuration
public class DependenciesConfig {
	
	@Bean
	public MainSceneFactory mainSceneFactory() {
		return new MainSceneFactory(mainSceneController());
	}
	
	@Bean
	public MainSceneController mainSceneController() {
		return new MainSceneController(searchService());
	}
	
	@Bean
	public SearchService searchService() {
		return new SearchServiceImpl();
	}
	
}
