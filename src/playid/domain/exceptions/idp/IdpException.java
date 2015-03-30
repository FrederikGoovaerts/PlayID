package playid.domain.exceptions.idp;

import playid.domain.exceptions.PlayIdException;

public class IdpException extends PlayIdException {

	private static final long serialVersionUID = 2231917393772395470L;

	private static final String DEFAULT_MESSAGE = "An error occured while trying to run IDP.\n";
	
	public IdpException(String msg) {
		super(DEFAULT_MESSAGE + msg);
	}
	
	public IdpException(String msg, Throwable cause) {
		super(DEFAULT_MESSAGE + msg, cause);
	}
	
	public IdpException(Throwable cause) {
		super(cause);
	}
	
	
	public IdpException() {
		super(DEFAULT_MESSAGE);
	}
}
