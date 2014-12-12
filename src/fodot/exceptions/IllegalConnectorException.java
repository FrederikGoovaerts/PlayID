package fodot.exceptions;


public class IllegalConnectorException extends RuntimeException {

	public IllegalConnectorException(Object caller, String connector) {
		super(connector + " is not a valid connector for " + caller);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
