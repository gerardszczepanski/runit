package szczepanski.gerard.runit.settings.service.loader.impl;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.impl.PropertySettingsLoader;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;

public class PropertySettingsLoaderTest {

	String propertiesPath = "testSettings.properties";

	PropertySettingsLoader settingsLoader;

	@SuppressWarnings("unchecked")
	@BeforeTest
	public void beforeTest() {
		PropertySpliterator<String> stringPropertySpliterator = Mockito.mock(PropertySpliterator.class);
		PropertySpliterator<Alias> webAliasPropertySpliterator = Mockito.mock(PropertySpliterator.class);
		settingsLoader = new PropertySettingsLoader(propertiesPath, stringPropertySpliterator,
				webAliasPropertySpliterator);
	}

	@Test
	public void loadPropertiesSuccess() {
		// Act
		Settings settings = settingsLoader.loadSettings();

		// Assert
		Assert.assertNotNull(settings);
	}

}
