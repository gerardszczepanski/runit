package szczepanski.gerard.runit.program.registry;

import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.common.util.StringUtils;

import java.io.IOException;

/**
 * This class inject Runit program to Windows startup.
 *
 * @author Gerard Szczepanski
 * @since Runit 1.5.0
 */
public class WindowsStartupInjector {

    private static final String CMD = "reg add HKLM\\Software\\Microsoft\\Windows\\CurrentVersion\\Run //v Runit //d Runit.exe";

    public void addRunitToStartupPrograms() {
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(CMD);
        } catch (IOException e) {
            throw new RunitRuntimeException(ExceptionCode.R_013, StringUtils.EMPTY);
        }
    }

}
