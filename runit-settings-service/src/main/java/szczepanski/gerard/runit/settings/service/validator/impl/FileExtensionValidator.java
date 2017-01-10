package szczepanski.gerard.runit.settings.service.validator.impl;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.validator.Validator;

public class FileExtensionValidator implements Validator<String> {

	private static final String FAILURE_MESSAGE = "Given file extension is not valid!";
	
	@Override
	public void validate(String obj) throws RunitValidationException {
		if ("".equals(obj)) {
			throw new RunitValidationException(FAILURE_MESSAGE);
		}
	}

}
