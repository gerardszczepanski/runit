package szczepanski.gerard.runit.settings.service.loader.impl;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.mapper.SettingsPropertiesMapper;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class PropertySettingsLoaderTest {

    String propertiesPath = "testSettings.properties";

    SettingsPropertiesMapper settingsPropertiesMapper;
    PropertySettingsLoader settingsLoader;

    @BeforeTest
    public void beforeTest() {
        settingsPropertiesMapper = mock(SettingsPropertiesMapper.class);
        settingsLoader = new PropertySettingsLoader(propertiesPath, settingsPropertiesMapper);
    }

    @Test(enabled = false)
    public void loadPropertiesSuccess() {
        //Arrange
        Settings loadedSettings = Settings.builder().build();
        when(settingsPropertiesMapper.toSettings(any())).thenReturn(loadedSettings);

        // Act
        Settings settings = settingsLoader.getSettings();

        // Assert
        assertNotNull(settings);
        verify(settingsPropertiesMapper).toSettings(any());
    }

}
