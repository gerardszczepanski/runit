package szczepanski.gerard.runit.settings.service.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.Objects;

/**
 * Represents alias defined in Properties.
 *
 * @author Gerard Szczepanski
 */

@Value
@AllArgsConstructor
public class Alias {

    String name;
    String value;

    @Override
    public String toString() {
        return String.format("[name->%s, value->%s]", name, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
