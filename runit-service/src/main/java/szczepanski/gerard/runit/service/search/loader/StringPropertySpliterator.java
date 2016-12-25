package szczepanski.gerard.runit.service.search.loader;

import java.util.Arrays;
import java.util.List;

public class StringPropertySpliterator implements PropertySpliterator<String>{

	@Override
	public List<String> fromPropertyString(String propertyString) {
		String[] splittedStrings = propertyString.split(DELIMETER);
		return Arrays.asList(splittedStrings);
	}

}
