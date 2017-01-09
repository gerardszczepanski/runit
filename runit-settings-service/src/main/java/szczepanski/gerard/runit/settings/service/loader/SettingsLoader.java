package szczepanski.gerard.runit.settings.service.loader;

/**
 * Loads settings for application.
 * 
 * Settings are stored in {@code Settings} obj.
 * SettingsLoader holds current {@code Settings} object 
 * and 'feeds' {@code SearchService} object with that Settings object at every 
 * search request.
 * 
 *  If Settings has changed during the program run, then 
 *  SettingsLoader reloads {@code Settings} object, and 
 *  make it new current object. 
 * 
 * @author Gerard Szczepanski
 *
 */
public interface SettingsLoader {
	
	Settings loadSettings();
	
	void markSettingsChanged();
	
}
