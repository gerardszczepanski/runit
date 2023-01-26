package szczepanski.gerard.runit.settings.writer;

import szczepanski.gerard.runit.settings.loader.Settings;

/**
 * This interface is responsible for Save Settings.
 *
 * @author Gerard Szczepanski
 */
public interface SettingsWriter {

    void updateSettings(Settings newSettings);

    void restoreDefaultSettings();

}
