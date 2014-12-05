package fodot.objects.terms;

import fodot.objects.exceptions.InvalidTermNameException;
import fodot.objects.type.FodotType;
import fodot.objects.util.TermsUtil;

public class FodotVariable extends FodotTerm {

	private String name;
	private FodotType type;
	
	public FodotVariable(String name, FodotType type) {
		super();
		setName(name);
		this.type = type;
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

	public FodotType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "[variable: "+getName()+"]";
	}
	
}
