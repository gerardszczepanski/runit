package szczepanski.gerard.runit.settings.service.loader.impl;

import java.util.ArrayList;
import java.util.List;

import szczepanski.gerard.advanced.collection.facade.AdvancedCollectionFactory;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;

/**
 * This class is responsible for load default settings from Users Windows System. 
 * 
 * @author Gerard Szczepanski
 *
 */
public class DefaultSettingsProvider {
	private static final List<Alias> EMPTY_LIST = new ArrayList<>(0);
	
	public Settings loadDefaultSettings() {
		return Settings.builder()
						.rootDirectioresToScan(getDefaultRootPaths())
						.fileExtensions(getDefaultExtensions())
						.webAliases(getDefaultWebAliases())
						.dirAliases(getDefaultDirAliases())
						.build();
	}
	
	private List<String> getDefaultRootPaths() {
		List<String> rootPaths = AdvancedCollectionFactory.list();
		rootPaths.add(System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu");
		rootPaths.add("C:\\ProgramData\\Microsoft\\Windows\\Start Menu");
		return rootPaths;
	}
	
	private List<String> getDefaultExtensions() {
		List<String> extensions = AdvancedCollectionFactory.list();
		extensions.add("lnk");
		extensions.add("exe");
		return extensions;
	}
	
	private List<Alias> getDefaultWebAliases() {
		List<Alias> webAliases = AdvancedCollectionFactory.list();
		webAliases.add(new Alias("google", "http://google.com"));
		webAliases.add(new Alias("gmail", "http://gmail.com"));
		webAliases.add(new Alias("youtube", "http://youtube.com"));
		webAliases.add(new Alias("yahoo", "http://yahoo.com"));
		webAliases.add(new Alias("facebook", "http://facebook.com"));
		webAliases.add(new Alias("runit", "https://github.com/gspansky/runit"));
		return webAliases;
	}
	
	private List<Alias> getDefaultDirAliases() {
		return EMPTY_LIST;
	}
}
