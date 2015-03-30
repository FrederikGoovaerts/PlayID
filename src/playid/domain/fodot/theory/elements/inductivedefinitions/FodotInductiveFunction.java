package playid.domain.fodot.theory.elements.inductivedefinitions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import playid.domain.exceptions.fodot.InvalidTypeException;
import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.theory.elements.terms.FodotFunction;
import playid.domain.fodot.theory.elements.terms.FodotVariable;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;
import playid.domain.fodot.vocabulary.elements.FodotType;


public class FodotInductiveFunction extends FodotElement implements IFodotInductiveDefinitionElement {

	private static final int BINDING_ORDER = -1;
	
	private FodotFunction function;
	private IFodotTerm functionResult;
	
	public FodotInductiveFunction(FodotFunction function,
			IFodotTerm functionResult) {
		super();
		this.function = function;
		setFunctionResult(functionResult);
	}

	private FodotFunction getFunction() {
		return function;
	}
	
	private void setFunctionResult(IFodotTerm functionRes) {
		if (functionRes == null) {
			throw new IllegalArgumentException("Functionresult can't be null");
		}
		FodotType expectedType = function.getDeclaration().getType();
		if (!functionRes.getType().isASubtypeOf(expectedType)
				&& !functionRes.getType().isASupertypeOf(expectedType)) {
			throw new InvalidTypeException(
					"The returntype of the function '"+this+"' in the inductive definition",
					functionRes.getType(), expectedType);
		}
		this.functionResult = functionRes;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> result = function.getFreeVariables();
		result.addAll(functionResult.getFreeVariables());
		return result;
	}
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
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
	
	@Override
	public String toString() {
		return "[ Inductive Function: "+toCode()+"]";
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		Set<IFodotElement> res = new HashSet<IFodotElement>(getFunction().getDirectFodotElements());
		res.add(functionResult);
		return res;
	}
	
	
	
	
	
}
