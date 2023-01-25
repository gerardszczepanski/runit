package szczepanski.gerard.runit.settings.service.validator.impl;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.settings.service.validator.Validator;

public class FileExtensionValidator implements Validator<String> {

    private static final String FAILURE_MESSAGE = "Im sorry, but file extension that you've provided is not valid. Please try again.";
    private static final String ONLY_LETTERS_PATTERN = "[A-Za-z]*";

    @Override
    public void validate(String obj) throws RunitValidationException {
        if (obj == null || obj.equals(StringUtils.EMPTY) || !obj.matches(ONLY_LETTERS_PATTERN)) {
            throw new RunitValidationException(FAILURE_MESSAGE);
        }
    }

}
