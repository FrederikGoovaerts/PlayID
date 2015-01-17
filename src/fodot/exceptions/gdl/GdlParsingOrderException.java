package fodot.exceptions.gdl;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlParsingOrderException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GdlParsingOrderException() {
    }

    public GdlParsingOrderException(String message) {
        super(message);
    }

    public GdlParsingOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public GdlParsingOrderException(Throwable cause) {
        super(cause);
    }
}
