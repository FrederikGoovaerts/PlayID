package fodot.exceptions.idp;

public class UnsatisfiableIdpFileException extends RuntimeException {
	
	private static final long serialVersionUID = -8717567470045900752L;

	public UnsatisfiableIdpFileException() {
		super("The given idp file does not have any solutions. It is unsatisfiable.");
	}
}
