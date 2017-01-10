package szczepanski.gerard.runit.settings.service.loader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.mapper.SettingsPropertiesMapper;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;

/**
 * Loads Settings from Properties file.
 * 
 * @author Gerard Szczepaanski
 */
@RequiredArgsConstructor
public class PropertySettingsLoader implements SettingsLoader {
	private static final Logger LOG = Logger.getLogger(PropertySettingsLoader.class);

	private final String propertiesPath;
	private final SettingsPropertiesMapper settingsPropertiesMapper;
	
	private Settings currentSettings;
	private boolean isSettingsActual;

	@Override
	public Settings loadSettings() {
		if (!isSettingsActual) {
			LOG.debug("Loading settings from " + propertiesPath);
			reloadSettings();
		}
		
		return currentSettings;
	}
	
	private void reloadSettings() {
		Properties properties = loadProperties();
		currentSettings = settingsPropertiesMapper.toSettings(properties);
		isSettingsActual = true;
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
			throw new RunitRuntimeException(ExceptionCode.R_008, e);
		}
	}

	@Override
	public void markSettingsChanged() {
		isSettingsActual = false;
	}

}
