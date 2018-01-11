package szczepanski.gerard.runit.settings.service.concator.impl;

import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.settings.service.concator.PropertyConcator;

import java.util.List;

public class StringPropertyConcator extends PropertyConcator<String> {

    @Override
    protected String convertToStringProperty(List<String> values) {
        String outputString = StringUtils.EMPTY;

        for (String val : values) {
            outputString = outputString + StringUtils.DELIMETER + val;
        }
        return outputString.substring(1);
    }

}
