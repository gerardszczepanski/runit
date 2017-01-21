package szczepanski.gerard.runit.settings.service.concator.impl;

import java.util.List;

import szczepanski.gerard.runit.settings.service.concator.PropertyConcator;
import szczepanski.gerard.runit.settings.service.loader.Alias;

public class AliasPropertyConcator extends PropertyConcator<Alias> {
	
	private static final String ALIAS_DELIMETER = "=";

	@Override
	protected String convertToStringProperty(List<Alias> values) {
		String outputString = EMPTY_STRING;

		for (Alias alias : values) {
			outputString = outputString + DELIMETER + alias.getName() + ALIAS_DELIMETER + alias.getValue();
		}

		return outputString.substring(1);
	}

}
