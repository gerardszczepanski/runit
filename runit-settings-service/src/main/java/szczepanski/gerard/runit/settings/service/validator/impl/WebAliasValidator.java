package szczepanski.gerard.runit.settings.service.validator.impl;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.loader.Alias;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class WebAliasValidator extends AbstractAliasValidator {

    private static final String FAILURE_ALIAS_VALUE_MESSAGE = "Im sorry, but web address that you've provided is not a valid URL address. Please try again.";

    @Override
    public void validate(Alias obj) throws RunitValidationException {
        validateAliasName(obj.getName());
        validateWebAddress(obj.getValue());
    }

    private void validateWebAddress(String uriString) throws RunitValidationException {
        try {
            URL url = new URL(uriString);
            url.toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RunitValidationException(FAILURE_ALIAS_VALUE_MESSAGE);
        }
    }

}
