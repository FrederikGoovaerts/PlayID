package fodot.exceptions;

public class IllegalAmountOfArguments extends IllegalArgumentException {
	
	public IllegalAmountOfArguments(Object caller, int given, int expected) {
		super("\nThe amount of arguments don't match in "+caller.getClass()+"."
					+ "\n Given: " + given
					+ "\n Expected: " + expected);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
