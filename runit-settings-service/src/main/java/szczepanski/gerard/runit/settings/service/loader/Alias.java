package szczepanski.gerard.runit.settings.service.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alias alias = (Alias) o;
        return Objects.equals(name, alias.name) &&
                Objects.equals(value, alias.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
