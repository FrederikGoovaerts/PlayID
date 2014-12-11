package fodot.objects.sentence.terms;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.exceptions.InvalidTermNameException;
import fodot.objects.util.TermsUtil;
import fodot.objects.vocabulary.elements.FodotType;

public class FodotVariable implements IFodotTerm {

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
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> result = new HashSet<FodotVariable>();
		result.add(this);
		return result;
	}

	@Override
	public String toCode() {
		return name;
	}

	@Override
	public String toString() {
		return "[variable: "+getName()+"]";
	}
	
}
