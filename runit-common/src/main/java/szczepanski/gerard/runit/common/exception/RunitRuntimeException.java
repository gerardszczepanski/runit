package szczepanski.gerard.runit.common.exception;

import lombok.Getter;

public class RunitRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -730044633290873418L;

    @Getter
    private final String exceptionCode;

    public RunitRuntimeException(ExceptionCode exceptionCode, Throwable cause, Object... msgArgs) {
        super(resolveExceptionMessage(exceptionCode.getTemplateMessage(), msgArgs), cause);
        this.exceptionCode = exceptionCode.getCode();
    }

    public RunitRuntimeException(ExceptionCode exceptionCode, Object... msgArgs) {
        super(resolveExceptionMessage(exceptionCode.getTemplateMessage(), msgArgs));
        this.exceptionCode = exceptionCode.getCode();
    }

    private static String resolveExceptionMessage(String messageTemplate, Object... msgArgs) {
        return String.format(messageTemplate, msgArgs);
    }

}
