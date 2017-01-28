package szczepanski.gerard.runit.settings.service.concator;

import java.util.List;

import szczepanski.gerard.runit.common.util.StringUtils;

public abstract class PropertyConcator<T> {
	
	protected static final String DELIMETER = ";";
	
	/**
	 * Template method which checks if values Colelction is empty. 
	 */
	public String toStringProperty(List<T> values) {
		if (values.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		return convertToStringProperty(values);
	}
	
	protected abstract String convertToStringProperty(List<T> values);
}
