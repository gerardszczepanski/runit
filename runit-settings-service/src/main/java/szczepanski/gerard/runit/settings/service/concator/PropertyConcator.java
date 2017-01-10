package szczepanski.gerard.runit.settings.service.concator;

import java.util.List;

@FunctionalInterface
public interface PropertyConcator<T> {
	
	String DELIMETER = ";";
	
	String toStringProperty(List<T> values);
	
}
