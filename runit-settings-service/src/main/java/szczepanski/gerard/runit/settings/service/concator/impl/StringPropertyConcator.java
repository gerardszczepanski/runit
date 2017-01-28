package szczepanski.gerard.runit.settings.service.concator.impl;

import java.util.List;

import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.settings.service.concator.PropertyConcator;

public class StringPropertyConcator extends PropertyConcator<String> {

	@Override
	protected String convertToStringProperty(List<String> values) {
		String outputString = StringUtils.EMPTY;

		for (String val : values) {
			outputString = outputString + DELIMETER + val;
		}

		return outputString.substring(1);
	}

}
