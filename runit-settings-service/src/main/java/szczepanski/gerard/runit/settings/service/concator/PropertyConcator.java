package szczepanski.gerard.runit.settings.service.concator;

import java.util.List;

public abstract class PropertyConcator<T> {
	
	protected static final String DELIMETER = ";";
	protected static final String EMPTY_STRING = "";
	
	/**
	 * Template method which checks if values Colelction is empty. 
	 */
	public String toStringProperty(List<T> values) {
		if (values.isEmpty()) {
			return EMPTY_STRING;
		}
		
		return convertToStringProperty(values);
	}
	
	protected abstract String convertToStringProperty(List<T> values);
}
