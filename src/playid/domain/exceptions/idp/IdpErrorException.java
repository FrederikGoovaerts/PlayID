package playid.domain.exceptions.idp;

public class IdpErrorException extends IdpException {

	private static final long serialVersionUID = 1L;

	public IdpErrorException(String idpOutput) {
		super("\nAn error has occured while connecting to IDP."
				+ "\n\nIdp output:\n"+idpOutput);
	}
}
