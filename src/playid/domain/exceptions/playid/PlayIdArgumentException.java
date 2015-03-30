package playid.domain.exceptions.playid;

import playid.domain.exceptions.PlayIdException;

public class PlayIdArgumentException extends PlayIdException {
	private static final long serialVersionUID = 192268689913874522L;
	
	private static final String DEFAULT_MESSAGE = 
			"An error occured with the arguments given to PlayID.";
	
	public PlayIdArgumentException(String msg, int givenArgumentsAmount, int expectedArgumentsAmount) {
		super(DEFAULT_MESSAGE
				+ "\n" + msg
				+ "\nGiven amount of arguments: " + givenArgumentsAmount
				+ "\nExpected amount of arguments: " + expectedArgumentsAmount);
	}
	
	public PlayIdArgumentException(String msg) {
		super(DEFAULT_MESSAGE + "\n" + msg);
	}
	
	public PlayIdArgumentException() {
		this(DEFAULT_MESSAGE);
	}
	
}
