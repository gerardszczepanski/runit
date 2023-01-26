package szczepanski.gerard.runit.search.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
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
