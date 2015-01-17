package fodot.exceptions.idp;

public class IdpParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IdpParseException(String errorline) {
		super("\nAn error has occured while running IDP: Is there a syntax error in the FO(.) code?"
				+ "\n\nIdp output:\n"+errorline);
	}
}
