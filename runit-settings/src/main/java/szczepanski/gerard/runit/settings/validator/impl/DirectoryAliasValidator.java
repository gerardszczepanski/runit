package szczepanski.gerard.runit.settings.validator.impl;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.loader.Alias;

@RequiredArgsConstructor
public class DirectoryAliasValidator extends AbstractAliasValidator {

    private final DirectoryPathValidator rootPathValidator;

    @Override
    public void validate(Alias obj) throws RunitValidationException {
        validateAliasName(obj.getName());
        rootPathValidator.validate(obj.getValue());
    }

}
