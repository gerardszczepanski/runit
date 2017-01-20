package szczepanski.gerard.runit.program.util;

import org.apache.log4j.Logger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runnit.view.util.ProgramErrorDisplayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramExceptionHandler {
	private static final Logger LOG = Logger.getLogger(ProgramExceptionHandler.class);

	public static void handleException(Throwable e) {
		LOG.debug(String.format("Handle Exception: [%s]", e.getMessage()));
		e.printStackTrace();

		if (isRunitException(e)) {
			handleRunitException(e);
		} else {
			handleAnotherException(e);
		}
	}

	private static void handleRunitException(Throwable e) {
		RunitRuntimeException runitException = (RunitRuntimeException) e;
		ProgramErrorDisplayer.showError(runitException.getExceptionCode(), runitException.getMessage());
	}

	private static void handleAnotherException(Throwable e) {
		ProgramErrorDisplayer.showError(ExceptionCode.UNEXPECTED_ERROR.getExceptionCode(),
				ExceptionCode.UNEXPECTED_ERROR.getExceptionTemplateMessage());
	}

	private static boolean isRunitException(Throwable e) {
		return RunitRuntimeException.class.isInstance(e);
	}

}
