package fodot.exceptions;

public class IllegalAmountOfArguments extends IllegalArgumentException {
	
	public IllegalAmountOfArguments(int given, int expected) {
		super("The amount of arguments don't match."
					+ "\n Given: " + given
					+ "\n Expected: " + expected);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
