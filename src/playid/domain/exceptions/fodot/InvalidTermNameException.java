package playid.domain.exceptions.fodot;

public class InvalidTermNameException extends FodotException {

	private static final long serialVersionUID = 955188520513599245L;
	
	private String name;
	
	public InvalidTermNameException(String name) {
		super(name + " is not a valid name for a term in FO(.).");
		this.name = name;
	}
	
		
	public String getName() {
		return name;
	}

}
