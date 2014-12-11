package fodot.objects.sentence.terms;

import java.util.HashSet;
import java.util.Set;

public class FodotConstant implements IFodotTerm {

	public String value;
	
	public FodotConstant(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public Set<FodotVariable> getFreeVariables() {
		return new HashSet<FodotVariable>();
	}

	@Override
	public String toCode() {
		return value;
	}

	@Override
	public String toString() {
		return "[constant: "+getValue()+"]";
	}
	
}
