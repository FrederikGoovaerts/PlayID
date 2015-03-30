package playid.domain.exceptions.idp;

public class IdpSyntaxErrorException extends IdpException {

	private static final long serialVersionUID = 2184030680831552815L;

	public IdpSyntaxErrorException(String line) {
		super("The given IDP file contains syntax errors: \nOutput:" + line);
	}
}
