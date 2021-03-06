package playid.domain.exceptions.idp;

public class OutOfResourcesException extends IdpException {

	private static final long serialVersionUID = -1266750500075746963L;

	public OutOfResourcesException(String line) {
		super("IDP ran out of resources. The timelimit was probably exceeded."
				+ "\nErrorMessage: " + line);
	}
}
