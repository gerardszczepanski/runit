package szczepanski.gerard.runit.search.service.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitBusinessException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DesktopFileRunner {
	
	public static void run(File file) {
		Desktop desktop = Desktop.getDesktop();
		
		try {
			desktop.open(file);
		} catch (IOException e) {
			throw new RunitBusinessException(ExceptionCode.B_004, e);
		}
	}
	
}
