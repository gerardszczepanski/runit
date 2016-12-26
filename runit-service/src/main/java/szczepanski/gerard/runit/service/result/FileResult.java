package szczepanski.gerard.runit.service.result;

import java.io.File;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

public class FileResult implements SearchResult {
	
	private final File file;
	private final SearchResultRepresentation searchResultRepresentation;
	
	
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
		this.file = file;
		this.searchResultRepresentation = new SearchResultRepresentation(null, file.getName());
	}
	
	@Override
	public void run() {
	}

	@Override
	public SearchResultRepresentation getSearchResultRepresentation() {
		return searchResultRepresentation;
	}

}
