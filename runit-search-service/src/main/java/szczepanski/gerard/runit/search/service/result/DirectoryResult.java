package szczepanski.gerard.runit.search.service.result;

import java.io.File;

import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitBusinessException;
import szczepanski.gerard.runit.search.service.util.DesktopFileRunner;
import szczepanski.gerard.runit.settings.service.loader.Alias;

/**
 * Represents directory defined with alias in Settings.
 * 
 * @author Gerard Szczepanski
 */
public class DirectoryResult extends SearchResult {

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
		super(new SearchResultRepresentation(
				new Image(DirectoryResult.class.getResourceAsStream("/images/search-results/directoryResult.png")),
				alias.getName()));
		this.directory = new File(alias.getValue());
	}

	@Override
	public void run() {
		DesktopFileRunner.run(directory);
	}

}
