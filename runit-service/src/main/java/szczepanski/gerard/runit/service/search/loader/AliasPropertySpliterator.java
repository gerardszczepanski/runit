package szczepanski.gerard.runit.service.search.loader;

import java.util.ArrayList;
import java.util.List;

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
