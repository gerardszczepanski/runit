package szczepanski.gerard.runit.service.search.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents alias o the web site to open
 * 
 * eg. alias -> fullAddress
 * google -> http://www.google.com
 * 
 * @author Gerard Szczepanski
 */

@Getter
@AllArgsConstructor
public class WebAlias {
	
	private String alias;
	private String fullAddress;
	
}
