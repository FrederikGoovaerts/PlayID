package fodot.objects.terms;

import java.util.List;

import fodot.objects.exceptions.InvalidTermNameException;
import fodot.objects.util.TermsUtil;

public class FodotFunction extends FodotTerm {

	private String name;
	private List<String> arguments;
	//TODO: Complete this: values mapping!
	
	public FodotFunction(String name, List<String> arguments) {
		super();
		setName(name);
		this.arguments = arguments;
	}

	public void setName(String name) {
		if (!TermsUtil.isValidName(name)) {
			throw new InvalidTermNameException(name);
		}
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "[function: " + name + arguments.toString() + "]";
	}

}
