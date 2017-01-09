package szczepanski.gerard.runit.settings.service.spliterator;

import java.util.List;

/**
 * Splits property string into a List T values.
 * 
 * @author Gerard Szczepanski
 */
@FunctionalInterface
public interface PropertySpliterator<T> {
	
	String DELIMETER = ";";
	
	List<T> fromPropertyString(String propertyString);
	
}
