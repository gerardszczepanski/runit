package szczepanski.gerard.runit.service.search.loader;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Settings {
	
	private List<String> rootDirectioresToScan;
	private List<String> fileExtensions;
	private List<WebAlias> webAliases;
	
}
