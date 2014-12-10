package fodot.objects.theory.definitions;

import java.util.List;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotFunction;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.sentence.terms.FodotVariable;


public class FodotInductiveFunction implements IFodotFormula {
	private FodotFunction function;
	private IFodotTerm functionResult;
	
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
