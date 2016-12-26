package szczepanski.gerard.runit.service.search.loader;

import java.util.List;
import java.util.Spliterator;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Settings {
	
	private List<String> rootDirectioresToScan;
	private List<String> fileExtensions;
	private List<WebAlias> webAliases;
	
	public Spliterator<String> getRootDirectories() {
		return rootDirectioresToScan.spliterator();
	}
	
	public Spliterator<String> getFileExtensions() {
		return fileExtensions.spliterator();
	}
	
	public Spliterator<WebAlias> getWebAiases() {
		return webAliases.spliterator();
	}
	
}
