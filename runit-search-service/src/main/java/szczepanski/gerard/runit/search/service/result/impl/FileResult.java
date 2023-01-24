package szczepanski.gerard.runit.search.service.result.impl;

import javafx.scene.image.Image;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.SearchResultRepresentation;
import szczepanski.gerard.runit.search.service.util.DesktopFileRunner;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents File found by {@code SearchService}.
 *
 * @author Gerard Szczepanski
 */
public class FileResult extends SearchResult {

    private final File file;

    private FileResult(File file) {
        super(new SearchResultRepresentation(FileResultImageProvider.getImageFromFile(file), FilenameUtils.getBaseName(file.getName())));
        this.file = file;
    }

    public static FileResult fromFile(File file) {
        isFileValid(file);
        return new FileResult(file);
    }

    private static void isFileValid(File file) {
        if (file == null) {
            throw new RunitRuntimeException(ExceptionCode.R_007);
        }
    }

    @Override
    public void run() {
        DesktopFileRunner.run(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FileResult that = (FileResult) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), file);
    }

    /**
     * Helper, Utility class for FileResult.
     * <p>
     * It is responsible for fetching FileResult File image.
     *
     * @author Gerard Szczepanski
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FileResultImageProvider {
        private static final String DEFAULT_IMAGE_PATH = "/images/search-results/fileResult.png";
        private static final Image DEFAULT_IMAGE = new Image(WebPageResult.class.getResourceAsStream(DEFAULT_IMAGE_PATH));

        private static Image getImageFromFile(File file) {
            return loadImageFromFile(file).orElse(DEFAULT_IMAGE);
        }

        private static Optional<Image> loadImageFromFile(File file) {
            try {
                Icon iconFromFile = FileSystemView.getFileSystemView().getSystemIcon(file);
                java.awt.Image imageFromFile = ((ImageIcon) iconFromFile).getImage();
                BufferedImage bufferedImageFromFile = (BufferedImage) imageFromFile;
                Image imageForFileResult = SwingFXUtils.toFXImage(bufferedImageFromFile, null);

                return Optional.of(imageForFileResult);
            } catch (ClassCastException e) {
                return Optional.empty();
            }
        }
    }



}
