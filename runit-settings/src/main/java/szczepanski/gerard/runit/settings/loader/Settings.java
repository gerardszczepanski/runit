package szczepanski.gerard.runit.settings.loader;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Settings {

    private List<String> rootDirectioresToScan;
    private List<String> fileExtensions;
    private List<Alias> webAliases;
    private List<Alias> dirAliases;

    public boolean areSettingsNotDefined() {
        return rootDirectioresToScan.isEmpty() && fileExtensions.isEmpty() && webAliases.isEmpty()
                && dirAliases.isEmpty();
    }
}
