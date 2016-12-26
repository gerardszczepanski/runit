package szczepanski.gerard.runit.service.search.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

/**
 * Loads Settings from Properties file.
 * 
 * @author Gerard Szczepaanski
 */
@RequiredArgsConstructor
public class PropertySettingsLoader implements SettingsLoader {

	private static final String ROOT_DIRECTORIES_KEY = "root.directories";
	private static final String FILE_EXTENSIONS_KEY = "file.extensions";
	private static final String WEB_ALIASES_KEY = "web.aliases";

	private final String propertiesPath;
	private final PropertySpliterator<String> stringPropertySpliterator;
	private final PropertySpliterator<WebAlias> webAliasPropertySpliterator;
	
	private Settings currentSettings;
	private boolean isSettingsActual;

	@Override
	public Settings loadSettings() {
		if (!isSettingsActual) {
			reloadSettings();
		}
		
		return currentSettings;
	}
	
	private void reloadSettings() {
		Properties properties = loadProperties();
		currentSettings = loadSettingsFromProperties(properties);
	}

	private Properties loadProperties() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		InputStream in = loader.getResourceAsStream(propertiesPath);
		try {
			properties.load(in);
			in.close();
			return properties;
		} catch (IOException e) {
			throw new RunitRuntimeException(e);
		}
	}

	private Settings loadSettingsFromProperties(Properties properties) {
		return Settings.builder()
					.rootDirectioresToScan(stringPropertySpliterator.fromPropertyString(properties.getProperty(ROOT_DIRECTORIES_KEY)))
					.fileExtensions(stringPropertySpliterator.fromPropertyString(properties.getProperty(FILE_EXTENSIONS_KEY)))
					.webAliases(webAliasPropertySpliterator.fromPropertyString(properties.getProperty(WEB_ALIASES_KEY)))
					.build();
	}

	@Override
	public void markSettingsChanged() {
		isSettingsActual = false;
	}

}
