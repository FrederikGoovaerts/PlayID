package fodot.objects.sentence.terms;

import java.util.ArrayList;
import java.util.List;

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
	public List<FodotVariable> getFreeVariables() {
		return new ArrayList<FodotVariable>();
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
