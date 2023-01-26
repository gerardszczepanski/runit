package szczepanski.gerard.runit.settings.concator;

import szczepanski.gerard.runit.common.util.StringUtils;

import java.util.List;

public abstract class PropertyConcator<T> {

    /**
     * Template method which checks if values Collection is empty.
     */
    public String toStringProperty(List<T> values) {
        return values.isEmpty() ? StringUtils.EMPTY : convertToStringProperty(values);
    }

    protected abstract String convertToStringProperty(List<T> values);
}
