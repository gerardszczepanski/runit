package szczepanski.gerard.runit.settings.service.loader.impl;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.impl.PropertySettingsLoader;
import szczepanski.gerard.runit.settings.service.mapper.SettingsPropertiesMapper;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;

public class PropertySettingsLoaderTest {

	String propertiesPath = "testSettings.properties";

	SettingsPropertiesMapper settingsPropertiesMapper;
	PropertySettingsLoader settingsLoader;

	@BeforeTest
	public void beforeTest() {
		settingsPropertiesMapper = Mockito.mock(SettingsPropertiesMapper.class);
		settingsLoader = new PropertySettingsLoader(propertiesPath, settingsPropertiesMapper);
	}

	@Test
	public void loadPropertiesSuccess() {
		//Arrange
		Settings loadedSettings = Settings.builder().build();
		Mockito.when(settingsPropertiesMapper.toSettings(Mockito.any())).thenReturn(loadedSettings);
		
		// Act
		Settings settings = settingsLoader.loadSettings();

		// Assert
		Assert.assertNotNull(settings);
		Mockito.verify(settingsPropertiesMapper).toSettings(Mockito.any());
	}

}
