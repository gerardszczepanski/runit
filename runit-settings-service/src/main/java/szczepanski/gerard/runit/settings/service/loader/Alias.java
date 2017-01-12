package szczepanski.gerard.runit.settings.service.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents alias defined in Properties.
 * 
 * @author Gerard Szczepanski
 */

@Getter
@AllArgsConstructor
public class Alias {
	
	private final String name;
	private final String value;
	
	
	@Override
	public String toString() {
		return String.format("[name->%s, value->%s]", name, value);
	}
	
	
	
}
