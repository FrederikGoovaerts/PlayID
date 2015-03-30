package playid.domain.exceptions.fodot;


public class IllegalConnectorException extends FodotException {

	private static final long serialVersionUID = 7066442650194644483L;

	public IllegalConnectorException(Object caller, String connector) {
		super(connector + " is not a valid connector for " + caller.getClass().getSimpleName());
	}
}
