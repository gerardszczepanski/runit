package szczepanski.gerard.runit.service.result;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

public class FileResult extends SearchResult {
	
	private final File file;
	
	public static FileResult fromFile(File file) {
		isFileValid(file);
		return new FileResult(file);
	}
	
	private static void isFileValid(File file) {
		if (file == null) {
			throw new RunitRuntimeException("SearchResult creation failure. Input File is null");
		}
	}
	
	private FileResult(File file) {
		super(new SearchResultRepresentation(new Image(WebPageResult.class.getResourceAsStream("/images/search-results/fileResult.png")), FilenameUtils.getBaseName(file.getName())));
		this.file = file;
	}
	
	@Override
	public void run() {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(file);
		} catch (IOException e) {
			throw new RunitRuntimeException("File can not be executed.");
		}
	}

}
