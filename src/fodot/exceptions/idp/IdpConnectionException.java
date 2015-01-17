package fodot.exceptions.idp;

import java.io.IOException;

public class IdpConnectionException extends IOException {

	private static final long serialVersionUID = 1L;
	
	public final static String DEFAULT_MESSAGE = "Failed to connect to IDP. Is the IDP location set correctly?";
	
	public IdpConnectionException(String message) {
		super(DEFAULT_MESSAGE + "\nErrormessage: " + message);
	}
	
	public IdpConnectionException() {
		super(DEFAULT_MESSAGE);
	}
}
