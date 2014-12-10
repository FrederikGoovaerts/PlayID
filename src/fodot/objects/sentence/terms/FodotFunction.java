package fodot.objects.sentence.terms;

import java.util.List;

import fodot.objects.sentence.formulas.argumented.FodotAbstractArgumentList;

public class FodotFunction extends FodotAbstractArgumentList implements IFodotTerm {
	
	public FodotFunction(String name, List<IFodotTerm> arguments) {
		super(name, arguments);
	}
	
	@Override
	public String toString() {
		return "[function "+getName()+": " + argumentsToString() + "]";
	}

}
