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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
		result = prime * result
				+ ((functionResult == null) ? 0 : functionResult.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FodotInductiveFunction other = (FodotInductiveFunction) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (functionResult == null) {
			if (other.functionResult != null)
				return false;
		} else if (!functionResult.equals(other.functionResult))
			return false;
		return true;
	}
	
	
	
	
	
}
