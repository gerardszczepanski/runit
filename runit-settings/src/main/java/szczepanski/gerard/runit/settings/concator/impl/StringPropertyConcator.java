package szczepanski.gerard.runit.settings.concator.impl;

import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.settings.concator.PropertyConcator;

import java.util.List;

public class StringPropertyConcator extends PropertyConcator<String> {

    @Override
    protected String convertToStringProperty(List<String> values) {
        StringBuilder outputString = new StringBuilder(StringUtils.EMPTY);

        for (String val : values) {
            outputString.append(StringUtils.DELIMITER).append(val);
        }
        return outputString.substring(1);
    }

}
