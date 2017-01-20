package szczepanski.gerard.runit.settings.service.loader.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.mapper.SettingsPropertiesMapper;

/**
 * Loads Settings from Properties file.
 * 
 * @author Gerard Szczepaanski
 */
public class PropertySettingsLoader implements SettingsLoader {
	private static final Logger LOG = Logger.getLogger(PropertySettingsLoader.class);

	private final String propertiesPath;
	private final SettingsPropertiesMapper settingsPropertiesMapper;
	
	private Settings currentSettings;
	
	public PropertySettingsLoader(String propertiesPath, SettingsPropertiesMapper settingsPropertiesMapper) {
		this.propertiesPath = propertiesPath;
		this.settingsPropertiesMapper = settingsPropertiesMapper;
	}

	@Override
	public Settings getSettings() {
		if (!areSettingsLoaded()) {
			loadSettingsIntoMemory();
		}
		return currentSettings;
	}
	
	private boolean areSettingsLoaded() {
		return currentSettings != null;
	}

	private Properties loadProperties() {
		Properties properties = new Properties();
		
		try {
			InputStream in = new FileInputStream(propertiesPath);
			properties.load(in);
			in.close();
			return properties;
		} catch (IOException | NullPointerException e) {
			throw new RunitRuntimeException(ExceptionCode.R_008, e);
		}
	}

	@Override
	public void loadSettingsIntoMemory() {
		LOG.debug("Loading settings from " + propertiesPath);
		Properties properties = loadProperties();
		currentSettings = settingsPropertiesMapper.toSettings(properties);
	}

}
