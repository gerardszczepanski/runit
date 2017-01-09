package szczepanski.gerard.runit.settings.service.spliterator.impl;

import java.util.ArrayList;
import java.util.List;

import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;

public class AliasPropertySpliterator implements PropertySpliterator<Alias> {

	private static final String ALIAS_DELIMETER = "=";

	@Override
	public List<Alias> fromPropertyString(String propertyString) {
		String[] splittedStrings = propertyString.split(DELIMETER);
		return createWebAliases(splittedStrings);
	}

	private List<Alias> createWebAliases(String[] splittedStrings) {
		List<Alias> webAliases = new ArrayList<>();
		
		for (String splittedString: splittedStrings) {
			String[] splittedStringParts = splittedString.split(ALIAS_DELIMETER);
			String alias = splittedStringParts[0];
			String webAddress = splittedStringParts[1];
			webAliases.add(new Alias(alias, webAddress));
		}
		
		return webAliases;
	}

}
