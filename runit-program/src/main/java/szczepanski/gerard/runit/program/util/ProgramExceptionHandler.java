package szczepanski.gerard.runit.program.util;

import org.apache.log4j.Logger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runnit.view.util.ProgramErrorDisplayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramExceptionHandler {
	private static final Logger LOG = Logger.getLogger(ProgramExceptionHandler.class);
	
	public static void handleException(Throwable e) {
		LOG.debug(String.format("Handle Exception: [%s]", e.getMessage()));
		e.printStackTrace();
		
		if (isRunitException(e)) {
			handleProgramException(e);
		} 
	}
	
	private static void handleProgramException(Throwable e) {
		RunitRuntimeException runitException = (RunitRuntimeException) e;
		ProgramErrorDisplayer.showError(runitException.getExceptionCode(), runitException.getMessage());
	}
	
	private static boolean isRunitException(Throwable e) {
		return RunitRuntimeException.class.isInstance(e);
	}
	
}
