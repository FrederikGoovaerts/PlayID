package fodot.exceptions.idp;

public class IdpParseException extends IdpException {

	private static final long serialVersionUID = 1L;

	public IdpParseException(String idpOutput) {
		super("\nAn error has occured while parsing the IDP output."
				+ "\n\nIdp output:\n"+idpOutput);
	}
}
