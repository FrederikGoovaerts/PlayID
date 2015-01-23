package fodot.exceptions.playid;

import fodot.exceptions.PlayIdException;

public class PlayIdArgumentException extends PlayIdException {
	private static final long serialVersionUID = 192268689913874522L;
	
	public PlayIdArgumentException(String msg) {
		super(msg);
	}
	
	public PlayIdArgumentException() {
		this("An error occured with the arguments given to PlayID.");
	}
	
}
