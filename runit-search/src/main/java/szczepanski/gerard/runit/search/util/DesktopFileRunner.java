package szczepanski.gerard.runit.search.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitBusinessException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
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
