package szczepanski.gerard.runit.settings.spliterator.impl;

import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.settings.spliterator.PropertySpliterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringPropertySpliterator implements PropertySpliterator<String> {
    private static final List<String> EMPTY_LIST = new ArrayList<>();

    @Override
    public List<String> fromPropertyString(String propertyString) {
        if (isPropertyEmpty(propertyString)) {
            return EMPTY_LIST;
        }

        String[] splitStrings = propertyString.split(StringUtils.DELIMITER);
        return Arrays.asList(splitStrings);
    }

}
