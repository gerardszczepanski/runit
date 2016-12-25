package szczepanski.gerard.runit.service.search.loader;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PropertySettingsLoaderTest {

	String propertiesPath = "testSettings.properties";

	PropertySettingsLoader settingsLoader;

	@BeforeTest
	public void beforeTest() {
		PropertySpliterator<String> stringPropertySpliterator = Mockito.mock(PropertySpliterator.class);
		PropertySpliterator<WebAlias> webAliasPropertySpliterator = Mockito.mock(PropertySpliterator.class);
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
