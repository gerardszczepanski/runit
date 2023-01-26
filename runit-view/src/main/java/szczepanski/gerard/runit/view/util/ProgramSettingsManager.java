package szczepanski.gerard.runit.view.util;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.settings.loader.Settings;
import szczepanski.gerard.runit.settings.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.writer.SettingsWriter;

/**
 * This class is responsible for load Settings if exist,
 * and then check if settings are initialized. If not, then
 * invoke SettingsWriter restoreDefaults.
 * <p>
 * If Settings file is not exist, then ProgramSettingsManager will create settings file,
 * with default configuration.
 *
 * @author Gerard Szczepanski
 */
@RequiredArgsConstructor
public class ProgramSettingsManager {
    private static final Logger LOG = Logger.getLogger(ProgramSettingsManager.class);

    private final SettingsLoader settingsLoader;
    private final SettingsWriter settingsWriter;

    public void loadSettings() {
        LOG.debug("Loading settings");
        try {
            Settings initialSettings = settingsLoader.getSettings();
            restoreSettingsIfNotDefined(initialSettings);
        } catch (RunitRuntimeException e) {
            LOG.debug("Settings file is not created yet. Creating default settings file");
            settingsWriter.restoreDefaultSettings();
        }
    }

    private void restoreSettingsIfNotDefined(Settings initialSettings) {
        if (initialSettings.areSettingsNotDefined()) {
            LOG.debug("Settings are not defined. Injecting default settings");
            settingsWriter.restoreDefaultSettings();
        }
    }

}
