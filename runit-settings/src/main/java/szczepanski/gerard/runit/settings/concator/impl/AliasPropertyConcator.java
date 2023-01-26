package szczepanski.gerard.runit.settings.concator.impl;

import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.settings.concator.PropertyConcator;
import szczepanski.gerard.runit.settings.loader.Alias;

import java.util.List;

public class AliasPropertyConcator extends PropertyConcator<Alias> {

    private static final String ALIAS_DELIMITER = "=";

    @Override
    protected String convertToStringProperty(List<Alias> values) {
        String outputString = StringUtils.EMPTY;

        for (Alias alias : values) {
            outputString = outputString + StringUtils.DELIMITER + alias.getName() + ALIAS_DELIMITER + alias.getValue();
        }
        return outputString.substring(1);
    }

}
