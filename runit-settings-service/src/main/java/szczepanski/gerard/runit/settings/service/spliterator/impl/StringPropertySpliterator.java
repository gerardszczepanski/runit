package szczepanski.gerard.runit.settings.service.spliterator.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;

public class StringPropertySpliterator implements PropertySpliterator<String>{
	private static final List<String> EMPTY_LIST = new ArrayList<>();
	
	@Override
	public List<String> fromPropertyString(String propertyString) {
		if (isPropertyEmpty(propertyString)) {
			return EMPTY_LIST;
		} 
		
		String[] splittedStrings = propertyString.split(StringUtils.DELIMETER);
		return Arrays.asList(splittedStrings);
	}

}
