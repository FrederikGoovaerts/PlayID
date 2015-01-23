package fodot.exceptions.fodot;

public class IllegalAmountOfArgumentsException extends FodotException {
	
	private static final long serialVersionUID = -6668328064839732212L;

	public IllegalAmountOfArgumentsException(Object caller, int given, int expected) {
		super("\nThe amount of arguments don't match in "+caller.getClass()+"."
					+ "\n Given: " + given
					+ "\n Expected: " + expected);
	}

}
