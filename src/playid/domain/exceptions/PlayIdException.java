package playid.domain.exceptions;

public class PlayIdException extends RuntimeException {
	private static final long serialVersionUID = -1599293948373247364L;
	
	public PlayIdException(String msg) {
		super(msg);
	}
	
	public PlayIdException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public PlayIdException(Throwable cause) {
		super(cause);
	}
	
	
	public PlayIdException() {
		super();
	}
}
