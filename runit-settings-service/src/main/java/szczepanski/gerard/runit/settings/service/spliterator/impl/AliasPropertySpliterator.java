package szczepanski.gerard.runit.settings.service.spliterator.impl;

import java.util.ArrayList;
import java.util.List;

import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.spliterator.PropertySpliterator;

public class AliasPropertySpliterator implements PropertySpliterator<Alias> {
	private static final List<Alias> EMPTY_LIST = new ArrayList<>(0);

	private static final String ALIAS_DELIMETER = "=";

	@Override
	public List<Alias> fromPropertyString(String propertyString) {
		if (isPropertyEmpty(propertyString)) {
			return EMPTY_LIST;
		} 
		
		String[] splittedStrings = propertyString.split(DELIMETER);
		return createAliases(splittedStrings);
	}

	private List<Alias> createAliases(String[] splittedStrings) {
		List<Alias> aliases = new ArrayList<>();
		
		for (String splittedString: splittedStrings) {
			String[] splittedStringParts = splittedString.split(ALIAS_DELIMETER);
			String aliasName = splittedStringParts[0];
			String aliasValue = splittedStringParts[1];
			aliases.add(new Alias(aliasName, aliasValue));
		}
		
		return aliases;
	}

}
