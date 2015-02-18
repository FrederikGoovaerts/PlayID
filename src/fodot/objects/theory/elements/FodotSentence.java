package fodot.objects.theory.elements;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fodot.exceptions.fodot.FodotException;
import fodot.exceptions.fodot.NonVariablefreeSentenceException;
import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.terms.FodotVariable;

/**
 * A FO(.) sentence is a FO(.) formula with no free occurences of variables
 * Sentences end with a dot.
 */
public class FodotSentence extends FodotElement implements IFodotElement, IFodotTheoryElement {
	private IFodotFormula formula;

	public FodotSentence(IFodotFormula formula) {
		super();
		setFormula(formula);
	}

	public IFodotFormula getFormula() {
		return formula;
	}

	private void setFormula(IFodotFormula formula) {
		if (formula.getFreeVariables() != null && !(formula.getFreeVariables().isEmpty())) {
			throw new NonVariablefreeSentenceException(formula);
		}		
		if (hasClashingVariableNames(formula)) {
			throw new FodotException("There are different variables with the same name in following formula: \n " + formula);
		}
		this.formula = formula;
	}

	private boolean hasClashingVariableNames(IFodotFormula formula) {
		Map<String, FodotVariable> variables = new HashMap<>();
		
		for (IFodotElement el : formula.getAllInnerElementsOfClass(FodotVariable.class)) {
			FodotVariable var = (FodotVariable) el;
			if (variables.get(var.getName()) != null
					&& !variables.get(var.getName()).equals(var)) {
				return true;
			}			
			variables.put(var.getName(), var);
		}
		return false;
	}

	@Override
	public String toCode() {
		return formula.toCode() + ".";
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
		FodotSentence other = (FodotSentence) obj;
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
