package szczepanski.gerard.runit.settings.service.spliterator;

import java.util.List;

import szczepanski.gerard.runit.common.util.StringUtils;

/**
 * Splits property string into a List T values.
 * 
 * @author Gerard Szczepanski
 */
@FunctionalInterface
public interface PropertySpliterator<T> {
	
	List<T> fromPropertyString(String propertyString);
	
	default boolean isPropertyEmpty(String propertyString) {
		return StringUtils.EMPTY.equals(propertyString);
	}
	
}
