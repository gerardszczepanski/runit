package szczepanski.gerard.runit.settings.service.writer.impl;

import org.apache.log4j.Logger;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.loader.impl.PropertySettingsLoader;
import szczepanski.gerard.runit.settings.service.writer.SettingsWriter;

@RequiredArgsConstructor
public class PropertySettingsWriter implements SettingsWriter {
	private static final Logger LOG = Logger.getLogger(PropertySettingsWriter.class);
	
	private final SettingsLoader settingsLoader;
	
	@Override
	public void updateSettings(Settings newSettings) {
		
	}

	@Override
	public void restoreDefaultSettings() {
		// TODO Auto-generated method stub
	}

}
