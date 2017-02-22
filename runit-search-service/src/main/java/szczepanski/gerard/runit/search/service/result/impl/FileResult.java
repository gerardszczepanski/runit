package szczepanski.gerard.runit.search.service.result.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FilenameUtils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.SearchResultRepresentation;
import szczepanski.gerard.runit.search.service.util.DesktopFileRunner;

/**
 * Represents File found by {@code SearchService}.
 * 
 * @author Gerard Szczepanski
 *
 */
public class FileResult extends SearchResult {

	private final File file;

	public static FileResult fromFile(File file) {
		isFileValid(file);
		return new FileResult(file);
	}

	private static void isFileValid(File file) {
		if (file == null) {
			throw new RunitRuntimeException(ExceptionCode.R_007);
		}
	}

	private FileResult(File file) {
		super(new SearchResultRepresentation(FileResultImageProvider.getImageFromFile(file),
				FilenameUtils.getBaseName(file.getName())));
		this.file = file;
	}

	@Override
	public void run() {
		DesktopFileRunner.run(file);
	}
	
	@Override
	public int hashCode() {
		final int prime = 53;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileResult other = (FileResult) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}



	/**
	 * Helper, Utility class for FileResult.
	 * 
	 * It is responsible for fetching FileResult File image.
	 *
	 * @author Gerard Szczepanski
	 *
	 */
	private static class FileResultImageProvider {
		private static final String DEFAULT_IMAGE_PATH = "/images/search-results/fileResult.png";
		private static final Image DEFAULT_IMAGE = new Image(
				WebPageResult.class.getResourceAsStream(DEFAULT_IMAGE_PATH));

		private static Image getImageFromFile(File file) {
			Optional<Image> optionalFileImage = loadImageFromFile(file);

			if (optionalFileImage.isPresent()) {
				return optionalFileImage.get();
			}
			return DEFAULT_IMAGE;
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
