package szczepanski.gerard.runit.program.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.view.util.ProgramErrorDisplayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramExceptionHandler {
    private static final Logger LOG = Logger.getLogger(ProgramExceptionHandler.class);

    public static void handleException(Throwable e) {
        LOG.debug(String.format("Handle Exception: [%s]", e.getMessage()));
        e.printStackTrace();

        if (isRunitException(e)) {
            handleRunitException(e);
        } else {
            handleAnotherException();
        }
    }

    private static void handleRunitException(Throwable e) {
        RunitRuntimeException runitException = (RunitRuntimeException) e;
        ProgramErrorDisplayer.showError(runitException.getExceptionCode(), runitException.getMessage());
    }

    private static void handleAnotherException() {
        ProgramErrorDisplayer.showError(ExceptionCode.UNEXPECTED_ERROR.getCode(), ExceptionCode.UNEXPECTED_ERROR.getTemplateMessage());
    }

    private static boolean isRunitException(Throwable e) {
        return RunitRuntimeException.class.isInstance(e);
    }

}
