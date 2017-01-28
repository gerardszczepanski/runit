package szczepanski.gerard.runit.search.service.result.impl;

import java.io.File;

import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitBusinessException;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.SearchResultRepresentation;
import szczepanski.gerard.runit.search.service.util.DesktopFileRunner;
import szczepanski.gerard.runit.settings.service.loader.Alias;

/**
 * Represents directory defined with alias in Settings.
 * 
 * @author Gerard Szczepanski
 */
public class DirectoryResult extends SearchResult {

	private static final String DIRECTORY_RESULT_DEFAULT_IMAGE_PATH = "/images/search-results/directoryResult.png";
	private static final Image DIRECTORY_RESULT_IMAGE = new Image(
			WebPageResult.class.getResourceAsStream(DIRECTORY_RESULT_DEFAULT_IMAGE_PATH));

	private final File directory;

	public static DirectoryResult fromDirectoryAlias(Alias directoryAlias) {
		isDirecotyrAliasValid(directoryAlias);
		return new DirectoryResult(directoryAlias);
	}

	private static void isDirecotyrAliasValid(Alias directoryAlias) {
		if (directoryAlias == null || directoryAlias.getName() == null || directoryAlias.getValue() == null) {
			throw new RunitBusinessException(ExceptionCode.B_001,
					new Object[] { directoryAlias.getName(), directoryAlias.getValue() });
		}
	}

	private DirectoryResult(Alias alias) {
		super(new SearchResultRepresentation(DIRECTORY_RESULT_IMAGE, alias.getName()));
		this.directory = new File(alias.getValue());
	}

	@Override
	public void run() {
		DesktopFileRunner.run(directory);
	}

}
