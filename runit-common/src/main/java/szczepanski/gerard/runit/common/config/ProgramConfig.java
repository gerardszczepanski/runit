package szczepanski.gerard.runit.common.config;

import java.net.URI;
import java.net.URISyntaxException;

import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

public class ProgramConfig {
	
	public static final String PROGRAM_TITLE = "Runit";
	public static final String PROGRAM_SETTINGS_TITLE = PROGRAM_TITLE + " Settings";
	public static final String PROGRAM_ICON_PATH = "/images/icon/icon.png";
	public static final URI PROGRAM_ABOUT_PAGE;
	
	public static final String PROPERTIES_CONFIG_FILE_PATH = "./runit.properties";
	
	public static final int CACHE_LIMIT = 20;
	public static final double CACHE_CLEAR_PERCENTAGE_SIZE = 0.40;
	
	public static final int DELAY_TYPE_TIME_FOR_TRIGGER_SEARCH_IN_MS = 300;
	
	/**
	 * Load URI for Program. Exception should not happen, but catch clause is necessary.
	 */
	static {
		try {
			PROGRAM_ABOUT_PAGE = new URI("https://github.com/gspansky/runit");
		} catch (URISyntaxException e) {
			throw new RunitRuntimeException(ExceptionCode.UNEXPECTED_ERROR);
		}
	}
	
}
