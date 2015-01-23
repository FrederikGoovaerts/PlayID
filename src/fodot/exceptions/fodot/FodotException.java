package fodot.exceptions.fodot;

import fodot.exceptions.PlayIdException;

public class FodotException extends PlayIdException {

	private static final long serialVersionUID = -1351740076904299583L;

	private static final String DEFAULT_MESSAGE =
			"An error occured while creating a FO(.) file.\n";
	
	public FodotException() {
		super(DEFAULT_MESSAGE);
    }

    public FodotException(String message) {
        super(DEFAULT_MESSAGE + message);
    }

    public FodotException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE + message, cause);
    }

    public FodotException(Throwable cause) {
        super(cause);
    }
}
