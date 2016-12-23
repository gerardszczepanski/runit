package szczepanski.gerard.runit.common.exception;

public class RunitRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -730044633290873418L;

	public RunitRuntimeException() {
		super();
	}

	public RunitRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RunitRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RunitRuntimeException(String message) {
		super(message);
	}

	public RunitRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
