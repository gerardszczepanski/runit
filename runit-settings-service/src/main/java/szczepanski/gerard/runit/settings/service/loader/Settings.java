package szczepanski.gerard.runit.settings.service.loader;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Settings {
	
	private List<String> rootDirectioresToScan;
	private List<String> fileExtensions;
	private List<Alias> webAliases;
	private List<Alias> dirAliases;
	
}
