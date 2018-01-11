package szczepanski.gerard.runit.settings.service.spliterator;

import szczepanski.gerard.runit.common.util.StringUtils;

import java.util.List;

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
