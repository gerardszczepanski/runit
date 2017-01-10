package szczepanski.gerard.runit.settings.service.concator.impl;

import java.util.List;

import szczepanski.gerard.runit.settings.service.concator.PropertyConcator;

public class StringPropertyConcator implements PropertyConcator<String> {

	@Override
	public String toStringProperty(List<String> values) {
		String outputString = "";
		
		for (String val: values) {
			outputString = outputString + DELIMETER + val;
		}
		
		return outputString.substring(1);
	}

}
