package szczepanski.gerard.runit.settings.service.writer;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.settings.service.loader.Settings;

/**
 * This interface is responsible for Save Settings.
 * 
 * @author Gerard Szczepanski
 *
 */
public interface SettingsWriter {
	
	void updateSettings(Settings newSettings) throws RunitRuntimeException;
	
	void restoreDefaultSettings();
	
}
