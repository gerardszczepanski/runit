package szczepanski.gerard.runit.search.service.util;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebPageRunner {
	
	public static void browsePage(URI webPage) {
		Desktop desktop = Desktop.getDesktop();
		
		try {
			desktop.browse(webPage);
		} catch (IOException e) {
			throw new RunitRuntimeException(ExceptionCode.R_006, webPage);
		}
	}
	
}
