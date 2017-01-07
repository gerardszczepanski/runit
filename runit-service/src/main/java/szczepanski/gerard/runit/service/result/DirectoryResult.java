package szczepanski.gerard.runit.service.result;

import java.io.File;

import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.search.loader.Alias;
import szczepanski.gerard.runit.service.search.util.DesktopFileRunner;

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
			throw new RunitRuntimeException("SearchResult creation failure. DirectoryAlias is broken");
		}
	}
	
	private DirectoryResult(Alias alias) {
		super(new SearchResultRepresentation(new Image(DirectoryResult.class.getResourceAsStream("/images/search-results/directoryResult.png")), alias.getName()));
		this.directory = new File(alias.getValue());
	}

	@Override
	public void run() {
		DesktopFileRunner.run(directory);
	}
	
}
