package fodot.exceptions.fodot;

import fodot.objects.vocabulary.elements.FodotType;

public class InvalidTypeException extends FodotException {
	
	private static final long serialVersionUID = 4268356800569416399L;

	public InvalidTypeException(String where, FodotType given, FodotType expected) {
		super(where  + " doesn't have the correct type"
				+ "\n Given: " + given.getName()
				+ "\n Expected : " + expected.getName());
	}
}
