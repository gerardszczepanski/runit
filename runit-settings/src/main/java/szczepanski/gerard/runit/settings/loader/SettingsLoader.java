package szczepanski.gerard.runit.settings.loader;

/**
 * Loads settings for application.
 * <p>
 * Settings are stored in {@code Settings} obj.
 * SettingsLoader holds current {@code Settings} object
 * and 'feeds' {@code SearchService} object with that Settings object at every
 * search request.
 *
 * @author Gerard Szczepanski
 */
public interface SettingsLoader {

    Settings getSettings();

    void loadSettingsIntoMemory();
}
