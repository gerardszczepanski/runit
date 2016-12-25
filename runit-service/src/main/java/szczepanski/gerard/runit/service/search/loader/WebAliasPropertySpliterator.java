package szczepanski.gerard.runit.service.search.loader;

import java.util.ArrayList;
import java.util.List;

public class WebAliasPropertySpliterator implements PropertySpliterator<WebAlias> {

	private static final String ALIAS_DELIMETER = "=";

	@Override
	public List<WebAlias> fromPropertyString(String propertyString) {
		String[] splittedStrings = propertyString.split(DELIMETER);
		return createWebAliases(splittedStrings);
	}

	private List<WebAlias> createWebAliases(String[] splittedStrings) {
		List<WebAlias> webAliases = new ArrayList<>();
		
		for (String splittedString: splittedStrings) {
			String[] splittedStringParts = splittedString.split(ALIAS_DELIMETER);
			String alias = splittedStringParts[0];
			String webAddress = splittedStringParts[1];
			webAliases.add(new WebAlias(alias, webAddress));
		}
		
		return webAliases;
	}

}
