package szczepanski.gerard.runit.service.search.loader;

/**
 * Loads settings for application.
 * 
 * Settings are stored in {@code Settings} obj.
 * 
 * @author Gerard Szczepanski
 *
 */
public interface SettingsLoader {
	
	Settings loadSettings();
	
}
