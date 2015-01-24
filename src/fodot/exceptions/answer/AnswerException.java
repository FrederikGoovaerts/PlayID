package fodot.exceptions.answer;

import fodot.exceptions.PlayIdException;

public class AnswerException extends PlayIdException {
	
	private static final long serialVersionUID = -8390509919796928159L;
	
	private static final String DEFAULT_MESSAGE =
			"An error occured during calculating an answer based on the IDP output.\n";
	
	public AnswerException(String msg) {
		super(DEFAULT_MESSAGE + msg);
	}
	
	public AnswerException(String msg, Throwable cause) {
		super(DEFAULT_MESSAGE + msg, cause);
	}
	
	public AnswerException(Throwable cause) {
		super(cause);
	}
	
	
	public AnswerException() {
		super();
	}
}
