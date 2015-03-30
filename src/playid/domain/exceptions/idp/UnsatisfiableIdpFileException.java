package playid.domain.exceptions.idp;

public class UnsatisfiableIdpFileException extends IdpException {
	
	private static final long serialVersionUID = -8717567470045900752L;

	public UnsatisfiableIdpFileException() {
		super("The given idp file does not have any solutions. It is unsatisfiable.");
	}
}
