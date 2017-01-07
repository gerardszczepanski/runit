package szczepanski.gerard.runit.common.exception;

public class RunitBusinessException extends RunitRuntimeException {
	private static final long serialVersionUID = -730044633290873418L;
	
	public RunitBusinessException(ExceptionCode code, Object[] msgArgs) {
		super(code, msgArgs);
	}
	
	public RunitBusinessException(ExceptionCode code, Throwable cause, Object... msgArgs) {
		super(code, cause, msgArgs);
	}

}
