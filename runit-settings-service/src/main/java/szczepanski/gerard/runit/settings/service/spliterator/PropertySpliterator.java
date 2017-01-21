package szczepanski.gerard.runit.settings.service.spliterator;

import java.util.List;

/**
 * Splits property string into a List T values.
 * 
 * @author Gerard Szczepanski
 */
@FunctionalInterface
public interface PropertySpliterator<T> {
	
	String EMPTY_STRING = "";
	String DELIMETER = ";";
	
	List<T> fromPropertyString(String propertyString);
	
	default boolean isPropertyEmpty(String propertyString) {
		return EMPTY_STRING.equals(propertyString);
	}
	
}
