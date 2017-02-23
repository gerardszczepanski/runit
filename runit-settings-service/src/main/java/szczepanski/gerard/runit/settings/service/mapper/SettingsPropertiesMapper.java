package szczepanski.gerard.runit.settings.service.mapper;

import java.util.Properties;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.settings.service.concator.PropertyConcator;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;

@RequiredArgsConstructor
public class SettingsPropertiesMapper {

	private static final String ROOT_DIRECTORIES_KEY = "root.directories";
	private static final String FILE_EXTENSIONS_KEY = "file.extensions";
	private static final String WEB_ALIASES_KEY = "web.aliases";
	private static final String DIRECTORIES_ALIASES_KEY = "dir.aliases";

	private final PropertySpliterator<String> stringPropertySpliterator;
	private final PropertySpliterator<Alias> aliasPropertySpliterator;
	private final PropertyConcator<String> stringPropertyConcator;
	private final PropertyConcator<Alias> aliasPropertyConcator;

	public Properties toProperties(Settings settings) {
		Properties properties = new Properties();

		properties.setProperty(ROOT_DIRECTORIES_KEY, stringPropertyConcator.toStringProperty(settings.getRootDirectioresToScan()));
		properties.setProperty(FILE_EXTENSIONS_KEY, stringPropertyConcator.toStringProperty(settings.getFileExtensions()));
		properties.setProperty(WEB_ALIASES_KEY, aliasPropertyConcator.toStringProperty(settings.getWebAliases()));
		properties.setProperty(DIRECTORIES_ALIASES_KEY, aliasPropertyConcator.toStringProperty(settings.getDirAliases()));

		return properties;
	}

	public Settings toSettings(Properties properties) {
		return Settings.builder()
				.rootDirectioresToScan(stringPropertySpliterator.fromPropertyString(properties.getProperty(ROOT_DIRECTORIES_KEY)))
				.fileExtensions(stringPropertySpliterator.fromPropertyString(properties.getProperty(FILE_EXTENSIONS_KEY)))
				.webAliases(aliasPropertySpliterator.fromPropertyString(properties.getProperty(WEB_ALIASES_KEY)))
				.dirAliases(aliasPropertySpliterator.fromPropertyString(properties.getProperty(DIRECTORIES_ALIASES_KEY))).build();
	}

}
