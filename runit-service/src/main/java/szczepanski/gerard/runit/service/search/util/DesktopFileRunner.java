package szczepanski.gerard.runit.service.search.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DesktopFileRunner {
	
	public static void run(File file) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(file);
		} catch (IOException e) {
			throw new RunitRuntimeException("File can not be executed.");
		}
	}
	
}
