package fodot.objects.sentence.terms;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.exceptions.InvalidTermNameException;
import fodot.objects.sentence.formulas.argumented.FodotAbstractArgumentList;
import fodot.objects.util.TermsUtil;

public class FodotFunction extends FodotAbstractArgumentList implements FodotTerm {

	private String name;
	private List<FodotTerm> arguments;
	
	public FodotFunction(String name, List<FodotTerm> arguments) {
		super(name, arguments);
	}
	
	@Override
	public String toString() {
		return "[function "+getName()+": " + argumentsToString() + "]";
	}

}
