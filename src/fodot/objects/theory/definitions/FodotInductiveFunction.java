package fodot.objects.theory.definitions;

import java.util.Set;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotFunction;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;


public class FodotInductiveFunction implements IFodotFormula {
	private FodotFunction function;
	private IFodotTerm functionResult;
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> result = function.getFreeVariables();
		result.addAll(functionResult.getFreeVariables());
		return result;
	}
	
	@Override
	public String toCode() {
		return function.toCode() + " = " + functionResult.toCode();
	}	
	
}
