package szczepanski.gerard.runit.settings.service.validator.impl;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.reporters.EmailableReporter;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.validator.Validator;

public class WebAliasValidator implements Validator<Alias> {
	
	
	private static final String FAILURE_ALIAS_NAME_MESSAGE = "Given alias name is not valid!";
	private static final String FAILURE_ALIAS_VALUE_MESSAGE = "Given web address is not valid URL address!";
	private static final String EMPTY_STRING = "";
	
	@Override
	public void validate(Alias obj) throws RunitValidationException {
		validateAliasName(obj.getName());
		validateWebAddress(obj.getValue());
	}
	
	private void validateAliasName(String name) throws RunitValidationException {
		if (name == null || name.equals(EMPTY_STRING)) {
			throw new RunitValidationException(FAILURE_ALIAS_NAME_MESSAGE);
		}
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
