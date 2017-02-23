package szczepanski.gerard.runit.settings.service.validator.impl;

import java.io.File;
import java.nio.file.InvalidPathException;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.validator.Validator;

/**
 * This object validates whether given String direcroty path is valid, or not.
 * If no, then throw RunitValidationException.
 * 
 * @author Gerard Szczepanski
 *
 */
public class DirectoryPathValidator implements Validator<String> {

	private static final String FAILURE_MESSAGE = "Given directory path is not valid!";

	@Override
	public void validate(String obj) throws RunitValidationException {
		File fileFromPath = tryToCreateFileFromPath(obj);
		checkIfFileFromPathIsDirectory(fileFromPath);
	}

	private File tryToCreateFileFromPath(String path) throws RunitValidationException {
		try {
			return new File(path);
		} catch (InvalidPathException | NullPointerException e) {
			throw new RunitValidationException(FAILURE_MESSAGE);
		}
	}

	private void checkIfFileFromPathIsDirectory(File fileFromPath) throws RunitValidationException {
		if (!fileFromPath.isDirectory()) {
			throw new RunitValidationException(FAILURE_MESSAGE);
		}
	}

}
