package szczepanski.gerard.runit.program.config;

import javafx.scene.control.Tab;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import szczepanski.gerard.runit.common.config.ProgramConfig;
import szczepanski.gerard.runit.search.service.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.service.algorithm.impl.DirectoryAliasSearchAlgorithm;
import szczepanski.gerard.runit.search.service.algorithm.impl.FileSearchAlgorithm;
import szczepanski.gerard.runit.search.service.algorithm.impl.WebAliasSearchAlgorithm;
import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.search.service.cache.impl.LexicalFrequencySearchCache;
import szczepanski.gerard.runit.search.service.service.SearchService;
import szczepanski.gerard.runit.search.service.service.SearchTermMatcher;
import szczepanski.gerard.runit.search.service.service.impl.FuzzySearchTermMatcher;
import szczepanski.gerard.runit.search.service.service.impl.SearchServiceImpl;
import szczepanski.gerard.runit.settings.service.concator.PropertyConcator;
import szczepanski.gerard.runit.settings.service.concator.impl.AliasPropertyConcator;
import szczepanski.gerard.runit.settings.service.concator.impl.StringPropertyConcator;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.loader.impl.DefaultSettingsProvider;
import szczepanski.gerard.runit.settings.service.loader.impl.PropertySettingsLoader;
import szczepanski.gerard.runit.settings.service.mapper.SettingsPropertiesMapper;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;
import szczepanski.gerard.runit.settings.service.spliterator.impl.AliasPropertySpliterator;
import szczepanski.gerard.runit.settings.service.spliterator.impl.StringPropertySpliterator;
import szczepanski.gerard.runit.settings.service.validator.Validator;
import szczepanski.gerard.runit.settings.service.validator.impl.DirectoryAliasValidator;
import szczepanski.gerard.runit.settings.service.validator.impl.DirectoryPathValidator;
import szczepanski.gerard.runit.settings.service.validator.impl.FileExtensionValidator;
import szczepanski.gerard.runit.settings.service.validator.impl.WebAliasValidator;
import szczepanski.gerard.runit.settings.service.writer.SettingsWriter;
import szczepanski.gerard.runit.settings.service.writer.impl.PropertySettingsWriter;
import szczepanski.gerard.runit.view.controller.*;
import szczepanski.gerard.runit.view.factory.FxmlComponentFactory;
import szczepanski.gerard.runit.view.scene.factory.MainSceneFactory;
import szczepanski.gerard.runit.view.scene.factory.SettingsSceneFactory;
import szczepanski.gerard.runit.view.scene.factory.SettingsStagePresenter;
import szczepanski.gerard.runit.view.tab.factory.SettingsCacheTabFactory;
import szczepanski.gerard.runit.view.tab.factory.SettingsDirTabFactory;
import szczepanski.gerard.runit.view.tab.factory.SettingsGeneralTabFactory;
import szczepanski.gerard.runit.view.tab.factory.SettingsWebTabFactory;
import szczepanski.gerard.runit.view.util.ProgramSettingsManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        return new SettingsSceneFactory(settingsTabFactories());
    }

    @Bean
    public Collection<FxmlComponentFactory<Tab>> settingsTabFactories() {
        List<FxmlComponentFactory<Tab>> tabFactories = new ArrayList<>(3);
        tabFactories.add(settingsGeneralTabFactory());
        tabFactories.add(settingsWebTabFactory());
        tabFactories.add(settingsDirTabFactory());
        tabFactories.add(settingsCacheTabFactory());
        return tabFactories;
    }

    @Bean
    public SettingsGeneralTabFactory settingsGeneralTabFactory() {
        return new SettingsGeneralTabFactory(settingsGeneralPaneTabController());
    }

    @Bean
    public SettingsGeneralPaneTabController settingsGeneralPaneTabController() {
        return new SettingsGeneralPaneTabController(settingsLoader(), settingsWriter(), directoryPathValidator(), fileExtensionValidator());
    }

    @Bean
    public SettingsWebTabFactory settingsWebTabFactory() {
        return new SettingsWebTabFactory(settingsWebPaneTabController());
    }

    @Bean
    public SettingsDirTabFactory settingsDirTabFactory() {
        return new SettingsDirTabFactory(settingsDirPaneTabController());
    }

    @Bean
    public SettingsCacheTabFactory settingsCacheTabFactory() {
        return new SettingsCacheTabFactory(settingsCachePaneTabController());
    }

    @Bean
    public SettingsDirectoriesPaneTabController settingsDirPaneTabController() {
        return new SettingsDirectoriesPaneTabController(settingsLoader(), settingsWriter(), directoryAliasValidator());
    }

    @Bean
    public SettingsWebPaneTabController settingsWebPaneTabController() {
        return new SettingsWebPaneTabController(settingsLoader(), settingsWriter(), webAliasValidator());
    }

    @Bean
    public SettingsCachePaneTabController settingsCachePaneTabController() {
        return new SettingsCachePaneTabController(cache());
    }


    @Bean
    public SettingsWriter settingsWriter() {
        return new PropertySettingsWriter(ProgramConfig.PROPERTIES_CONFIG_FILE_PATH, settingsPropertiesMapper(), settingsLoader(), defaultSettingsProvider());
    }

    @Bean
    public DefaultSettingsProvider defaultSettingsProvider() {
        return new DefaultSettingsProvider();
    }

    @Bean
    public Validator<String> directoryPathValidator() {
        return new DirectoryPathValidator();
    }

    @Bean
    public Validator<String> fileExtensionValidator() {
        return new FileExtensionValidator();
    }

    @Bean
    public Validator<Alias> webAliasValidator() {
        return new WebAliasValidator();
    }

    @Bean
    public Validator<Alias> directoryAliasValidator() {
        return new DirectoryAliasValidator((DirectoryPathValidator) directoryPathValidator());
    }

    @Bean
    public SearchService searchService() {
        return SearchServiceImpl.builder().searchAlgorithms(searchAlgorithms()).settingsLoader(settingsLoader()).cache(cache()).build();
    }

    @Bean
    public SettingsLoader settingsLoader() {
        return new PropertySettingsLoader(ProgramConfig.PROPERTIES_CONFIG_FILE_PATH, settingsPropertiesMapper());
    }

    @Bean
    public SettingsPropertiesMapper settingsPropertiesMapper() {
        return new SettingsPropertiesMapper(stringPropertySpliterator(), aliasPropertySpliterator(), stringPropertyConcator(), aliasPropertyConcator());
    }

    @Bean
    public Cache cache() {
        return new LexicalFrequencySearchCache(ProgramConfig.CACHE_LIMIT, ProgramConfig.CACHED_RESULTS_TO_CLEAR_SIZE);
    }

    @Bean
    public PropertySpliterator<String> stringPropertySpliterator() {
        return new StringPropertySpliterator();
    }

    @Bean
    public PropertySpliterator<Alias> aliasPropertySpliterator() {
        return new AliasPropertySpliterator();
    }

    @Bean
    public PropertyConcator<String> stringPropertyConcator() {
        return new StringPropertyConcator();
    }

    @Bean
    public PropertyConcator<Alias> aliasPropertyConcator() {
        return new AliasPropertyConcator();
    }

    private List<SearchAlgorithm> searchAlgorithms() {
        List<SearchAlgorithm> searchAlgrithms = new ArrayList<>(3);

        searchAlgrithms.add(new FileSearchAlgorithm(searchTermMatcher()));
        searchAlgrithms.add(new WebAliasSearchAlgorithm(searchTermMatcher()));
        searchAlgrithms.add(new DirectoryAliasSearchAlgorithm(searchTermMatcher()));

        return searchAlgrithms;
    }

    @Bean
    public SearchTermMatcher searchTermMatcher() {
        return new FuzzySearchTermMatcher();
    }

    @Bean
    public ProgramSettingsManager programSettingsManager() {
        return new ProgramSettingsManager(settingsLoader(), settingsWriter());
    }

}
