package szczepanski.gerard.runit.program.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.service.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.service.search.algorithm.impl.FileSearchAlgorithm;
import szczepanski.gerard.runit.service.search.algorithm.impl.WebAliasSearchAlgorithm;
import szczepanski.gerard.runit.service.search.loader.PropertySettingsLoader;
import szczepanski.gerard.runit.service.search.loader.PropertySpliterator;
import szczepanski.gerard.runit.service.search.loader.SettingsLoader;
import szczepanski.gerard.runit.service.search.loader.StringPropertySpliterator;
import szczepanski.gerard.runit.service.search.loader.WebAlias;
import szczepanski.gerard.runit.service.search.loader.WebAliasPropertySpliterator;
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
		return SearchServiceImpl.builder().searchAlgorithms(searchAlgorithms()).settingsLoader(settingsLoader())
				.build();
	}

	@Bean
	public SettingsLoader settingsLoader() {
		return new PropertySettingsLoader(ProgramConfig.PROPERTIES_CONFIG_FILE_PATH, stringPropertySpliterator(),
				webAliasPropertySpliterator());
	}

	@Bean
	public PropertySpliterator<String> stringPropertySpliterator() {
		return new StringPropertySpliterator();
	}

	@Bean
	public PropertySpliterator<WebAlias> webAliasPropertySpliterator() {
		return new WebAliasPropertySpliterator();
	}

	private List<SearchAlgorithm> searchAlgorithms() {
		List<SearchAlgorithm> searchAlgrithms = new ArrayList<>();

		//searchAlgrithms.add(new FileSearchAlgorithm());
		searchAlgrithms.add(new WebAliasSearchAlgorithm());

		return searchAlgrithms;
	}

}
