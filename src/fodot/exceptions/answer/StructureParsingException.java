package fodot.exceptions.answer;

public class StructureParsingException extends AnswerException {
	
	private static final long serialVersionUID = -6600319450036414077L;
	
	private static final String DEFAULT_MESSAGE =
			"An error occured during parsing a FO(.) structure.\n";
	
	public StructureParsingException(String msg) {
		super(DEFAULT_MESSAGE + msg);
	}
	
	public StructureParsingException(String msg, Throwable cause) {
		super(DEFAULT_MESSAGE + msg, cause);
	}
	
	public StructureParsingException(Throwable cause) {
		super(cause);
	}
	
	
	public StructureParsingException() {
		super();
	}
}
