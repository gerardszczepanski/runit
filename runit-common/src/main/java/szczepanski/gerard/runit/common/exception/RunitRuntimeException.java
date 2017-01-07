package szczepanski.gerard.runit.common.exception;

import lombok.Getter;

public class RunitRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -730044633290873418L;
	
	@Getter
	private final String exceptionCode;

	public RunitRuntimeException(ExceptionCode code, Throwable cause, Object... msgArgs) {
		super(resolveExceptionMessage(code.getExceptionTemplateMessage(), msgArgs), cause);
		this.exceptionCode = code.getExceptionCode();
	}

	public RunitRuntimeException(ExceptionCode code, Object... msgArgs) {
		super(resolveExceptionMessage(code.getExceptionTemplateMessage(), msgArgs));
		this.exceptionCode = code.getExceptionCode();
	}
	
	private static String resolveExceptionMessage(String messageTemplate, Object... msgArgs) {
		return String.format(messageTemplate, msgArgs);
	}
	
}
