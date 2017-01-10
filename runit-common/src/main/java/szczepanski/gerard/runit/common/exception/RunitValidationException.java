package szczepanski.gerard.runit.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RunitValidationException extends Throwable {
	private static final long serialVersionUID = -823464266764923693L;
	
	@Getter
	private final String displayMessage;
	
}
