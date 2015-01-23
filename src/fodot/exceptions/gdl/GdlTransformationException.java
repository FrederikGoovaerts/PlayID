package fodot.exceptions.gdl;

import fodot.exceptions.PlayIdException;

public class GdlTransformationException extends PlayIdException {

	private static final long serialVersionUID = -1351740076904299583L;

	private static final String DEFAULT_MESSAGE =
			"An error occured while transforming the GDL file.\n";
	
	public GdlTransformationException() {
		super(DEFAULT_MESSAGE);
    }

    public GdlTransformationException(String message) {
        super(DEFAULT_MESSAGE + message);
    }

    public GdlTransformationException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE + message, cause);
    }

    public GdlTransformationException(Throwable cause) {
        super(cause);
    }
}
