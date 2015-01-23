package fodot.objects.theory.elements.formulas;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.terms.FodotVariable;

public class FodotNot extends FodotElement implements IFodotFormula {
	private IFodotFormula formula;
	private static final int BINDING_ORDER = 0;

	public FodotNot(IFodotFormula formula) {
		super();
		this.formula = formula;
	}

	public IFodotFormula getFormula() {
		return formula;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		return formula.getFreeVariables();
	}
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
	}

	@Override
	public String toCode() {
		return "~" + formula.toCode();
	}
	@Override
	public String toString() {
		return "[not : " + formula + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
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
		FodotNot other = (FodotNot) obj;
		if (formula == null) {
			if (other.formula != null)
				return false;
		} else if (!formula.equals(other.formula))
			return false;
		return true;
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return Arrays.asList(formula);
	}
	
	
	
	
}
