package fodot.exceptions.idp;

public class IdpNonOptimalSolutionException extends IdpException {

	private static final long serialVersionUID = 8677609728951528995L;
	
	public final static String DEFAULT_MESSAGE =
			"IDP found a solution, but that solution wasn't an optimal solution.";
	
	
	public IdpNonOptimalSolutionException(String message) {
		super(DEFAULT_MESSAGE + "\nErrormessage: " + message);
	}
	
	public IdpNonOptimalSolutionException(int score, int maxScore) {
		this("Score/MaxScore: " + score + "/" + maxScore);
	}
	
	public IdpNonOptimalSolutionException() {
		super(DEFAULT_MESSAGE);
	}

}
