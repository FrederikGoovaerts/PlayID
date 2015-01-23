package fodot.exceptions.idp;

public class IdpParseException extends IdpException {

	private static final long serialVersionUID = 1L;

	public IdpParseException(String idpOutput) {
		super("\nAn error has occured while parsing IDP: Is there a syntax error in the FO(.) code?"
				+ "\n\nIdp output:\n"+idpOutput);
	}
}
