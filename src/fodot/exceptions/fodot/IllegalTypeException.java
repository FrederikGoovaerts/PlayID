package fodot.exceptions.fodot;

import fodot.objects.vocabulary.elements.FodotType;

public class IllegalTypeException extends IllegalArgumentException {
	public IllegalTypeException(String where, FodotType given, FodotType expected) {
		super(where  + " doesn't have the correct type"
				+ "\n Given: " + given.getName()
				+ "\n Expected : " + expected.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
