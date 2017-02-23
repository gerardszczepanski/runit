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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alias other = (Alias) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
