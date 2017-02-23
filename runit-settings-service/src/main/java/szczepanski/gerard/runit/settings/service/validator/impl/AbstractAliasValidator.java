package szczepanski.gerard.runit.settings.service.validator.impl;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.validator.Validator;

public abstract class AbstractAliasValidator implements Validator<Alias> {

	private static final String FAILURE_ALIAS_NAME_MESSAGE = "Given alias name is not valid!";

	protected void validateAliasName(String name) throws RunitValidationException {
		if (name == null || name.equals(StringUtils.EMPTY)) {
			throw new RunitValidationException(FAILURE_ALIAS_NAME_MESSAGE);
		}
	}
}
