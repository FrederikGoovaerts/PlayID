package fodot.exceptions.idp;

public class NoValidModelsException extends IdpException {

	private static final long serialVersionUID = 1L;

	public NoValidModelsException() {
		super("No valid models were found.");
	}
}
