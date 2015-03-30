package playid.domain.exceptions.gdl;

public class GdlTypeIdentificationError extends GdlTransformationException {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_MESSAGE = "An error occured when identifying the types of a GDL file.\n";
	
	public GdlTypeIdentificationError() {
		super(DEFAULT_MESSAGE);
    }

    public GdlTypeIdentificationError(String message) {
        super(DEFAULT_MESSAGE + message);
    }

    public GdlTypeIdentificationError(String message, Throwable cause) {
        super(DEFAULT_MESSAGE + message, cause);
    }

    public GdlTypeIdentificationError(Throwable cause) {
        super(cause);
    }
}
