package szczepanski.gerard.runit.program.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.service.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.service.search.algorithm.impl.DirectoryAliasSearchAlgorithm;
import szczepanski.gerard.runit.service.search.algorithm.impl.FileSearchAlgorithm;
import szczepanski.gerard.runit.service.search.algorithm.impl.WebAliasSearchAlgorithm;
import szczepanski.gerard.runit.service.search.loader.Alias;
import szczepanski.gerard.runit.service.search.loader.AliasPropertySpliterator;
import szczepanski.gerard.runit.service.search.loader.PropertySettingsLoader;
import szczepanski.gerard.runit.service.search.loader.PropertySpliterator;
import szczepanski.gerard.runit.service.search.loader.SettingsLoader;
import szczepanski.gerard.runit.service.search.loader.StringPropertySpliterator;
import szczepanski.gerard.runit.service.service.Cache;
import szczepanski.gerard.runit.service.service.SearchService;
import szczepanski.gerard.runit.service.service.SearchTermMatcher;
import szczepanski.gerard.runit.service.service.impl.SearchServiceImpl;
import szczepanski.gerard.runit.service.service.impl.SimpleSearchCache;
import szczepanski.gerard.runit.service.service.impl.SimpleSearchTermMatcher;
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
		return SearchServiceImpl.builder()
								.searchAlgorithms(searchAlgorithms())
								.settingsLoader(settingsLoader())
								.cache(cache())
								.build();
	}

	@Bean
	public SettingsLoader settingsLoader() {
		return new PropertySettingsLoader(ProgramConfig.PROPERTIES_CONFIG_FILE_PATH, stringPropertySpliterator(),
				webAliasPropertySpliterator());
	}
	
	@Bean
	public Cache cache() {
		return new SimpleSearchCache(ProgramConfig.CACHE_LIMIT, ProgramConfig.CACHE_CLEAR_PERCENTAGE_SIZE);
	}

	@Bean
	public PropertySpliterator<String> stringPropertySpliterator() {
		return new StringPropertySpliterator();
	}

	@Bean
	public PropertySpliterator<Alias> webAliasPropertySpliterator() {
		return new AliasPropertySpliterator();
	}

	private List<SearchAlgorithm> searchAlgorithms() {
		List<SearchAlgorithm> searchAlgrithms = new ArrayList<>();

		searchAlgrithms.add(new FileSearchAlgorithm(searchTermMatcher()));
		searchAlgrithms.add(new WebAliasSearchAlgorithm(searchTermMatcher()));
		searchAlgrithms.add(new DirectoryAliasSearchAlgorithm(searchTermMatcher()));

		return searchAlgrithms;
	}
	
	@Bean
	public SearchTermMatcher searchTermMatcher() {
		return new SimpleSearchTermMatcher();
	}

}
