package fodot.objects.exceptions;

public class InvalidTermNameException extends RuntimeException {

	private String name;
	
	public InvalidTermNameException(String name) {
		super(name + " is not a valid name for a term in FO(.).");
		this.name = name;
	}
	
		
	public String getName() {
		return name;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
