package szczepanski.gerard.runit.settings.service.concator.impl;

import java.util.List;

import szczepanski.gerard.runit.settings.service.concator.PropertyConcator;
import szczepanski.gerard.runit.settings.service.loader.Alias;

public class AliasPropertyConcator implements PropertyConcator<Alias> {
	
	private static final String ALIAS_DELIMETER = "=";
	private static final String EMPTY_STRING = "";

	@Override
	public String toStringProperty(List<Alias> values) {
		if (values.isEmpty()) {
			return EMPTY_STRING;
		}
		
		return convertToStringProperty(values);
	}

	private String convertToStringProperty(List<Alias> values) {
		String outputString = EMPTY_STRING;

		for (Alias alias : values) {
			outputString = outputString + DELIMETER + alias.getName() + ALIAS_DELIMETER + alias.getValue();
		}

		return outputString.substring(1);
	}

}
