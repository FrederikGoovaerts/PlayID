package fodot.objects.theory.definitions;

import java.util.List;

import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.sentence.terms.FodotFunction;
import fodot.objects.sentence.terms.FodotTerm;
import fodot.objects.sentence.terms.FodotVariable;


public class FodotInductiveFunction implements FodotFormula {
	private FodotFunction function;
	private FodotTerm functionResult;
	
	@Override
	public List<FodotVariable> getFreeVariables() {
		List<FodotVariable> result = function.getFreeVariables();
		result.addAll(functionResult.getFreeVariables());
		return result;
	}
	
	@Override
	public String toCode() {
		return function.toCode() + " = " + functionResult.toCode();
	}	
	
}
