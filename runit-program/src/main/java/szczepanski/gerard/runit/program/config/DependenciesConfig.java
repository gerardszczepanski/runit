package szczepanski.gerard.runit.program.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.search.service.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.service.service.impl.SearchServiceImpl;
import szczepanski.gerard.runit.search.service.service.impl.SimpleSearchCache;
import szczepanski.gerard.runit.search.service.service.impl.SimpleSearchTermMatcher;
import szczepanski.gerard.runit.service.search.algorithm.impl.DirectoryAliasSearchAlgorithm;
import szczepanski.gerard.runit.service.search.algorithm.impl.FileSearchAlgorithm;
import szczepanski.gerard.runit.service.search.algorithm.impl.WebAliasSearchAlgorithm;
import szczepanski.gerard.runit.service.service.Cache;
import szczepanski.gerard.runit.service.service.SearchService;
import szczepanski.gerard.runit.service.service.SearchTermMatcher;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.loader.impl.PropertySettingsLoader;
import szczepanski.gerard.runit.settings.service.service.SettingsWriter;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;
import szczepanski.gerard.runit.settings.service.spliterator.impl.AliasPropertySpliterator;
import szczepanski.gerard.runit.settings.service.spliterator.impl.StringPropertySpliterator;
import szczepanski.gerard.runnit.view.controller.MainSceneController;
import szczepanski.gerard.runnit.view.controller.SettingsSceneController;
import szczepanski.gerard.runnit.view.scene.factory.MainSceneFactory;
import szczepanski.gerard.runnit.view.scene.factory.SettingsSceneFactory;
import szczepanski.gerard.runnit.view.scene.factory.SettingsStagePresenter;

@Configuration
public class DependenciesConfig {

	@Bean
	public MainSceneFactory mainSceneFactory() {
		return new MainSceneFactory(mainSceneController());
	}

	@Bean
	public MainSceneController mainSceneController() {
		return new MainSceneController(searchService(), settingsStagePresenter());
	}
	
	@Bean 
	public SettingsStagePresenter settingsStagePresenter() {
		return new SettingsStagePresenter(settingsSceneFactory());
	}
	
	@Bean
	public SettingsSceneFactory settingsSceneFactory() {
		return new SettingsSceneFactory(settingsSceneController());
	}
	
	@Bean
	public SettingsSceneController settingsSceneController() {
		return new SettingsSceneController(settingsLoader(), settingsWriter());
	}
	
	@Bean
	public SettingsWriter settingsWriter() {
		return null; //Temporary
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
