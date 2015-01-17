package fodot.exceptions.idp;

public class NoValidModelsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoValidModelsException() {
		super("No valid models were found.");
	}
}
