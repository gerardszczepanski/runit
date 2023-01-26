package szczepanski.gerard.runit.search.result.impl;

import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitBusinessException;
import szczepanski.gerard.runit.search.result.SearchResult;
import szczepanski.gerard.runit.search.result.SearchResultRepresentation;
import szczepanski.gerard.runit.search.util.DesktopFileRunner;
import szczepanski.gerard.runit.settings.loader.Alias;

import java.io.File;
import java.util.Objects;

/**
 * Represents directory defined with alias in Settings.
 *
 * @author Gerard Szczepanski
 */
public class DirectoryResult extends SearchResult {

    private static final String DIRECTORY_RESULT_DEFAULT_IMAGE_PATH = "/images/search-results/directoryResult.png";
    private static final Image DIRECTORY_RESULT_IMAGE = new Image(WebPageResult.class.getResourceAsStream(DIRECTORY_RESULT_DEFAULT_IMAGE_PATH));

    private final File directory;

    private DirectoryResult(Alias alias) {
        super(new SearchResultRepresentation(DIRECTORY_RESULT_IMAGE, alias.getName()));
        this.directory = new File(alias.getValue());
    }

    public static DirectoryResult fromDirectoryAlias(Alias directoryAlias) {
        isDirectoryAliasValid(directoryAlias);
        return new DirectoryResult(directoryAlias);
    }

    private static void isDirectoryAliasValid(Alias directoryAlias) {
        if (directoryAlias == null || directoryAlias.getName() == null || directoryAlias.getValue() == null) {
            throw new RunitBusinessException(ExceptionCode.B_001, new Object[]{directoryAlias.getName(), directoryAlias.getValue()});
        }
    }

    @Override
    public void run() {
        DesktopFileRunner.run(directory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DirectoryResult that = (DirectoryResult) o;
        return Objects.equals(directory, that.directory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), directory);
    }
}
