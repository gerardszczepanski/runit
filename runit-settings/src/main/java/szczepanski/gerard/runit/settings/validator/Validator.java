package szczepanski.gerard.runit.settings.validator;

import szczepanski.gerard.runit.common.exception.RunitValidationException;

@FunctionalInterface
public interface Validator<T> {

    void validate(T obj) throws RunitValidationException;
}
