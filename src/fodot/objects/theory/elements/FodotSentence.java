package fodot.objects.theory.elements;

import java.util.HashSet;
import java.util.Set;

import fodot.exceptions.NonVariablefreeSentenceException;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.util.NameUtil;

/**
 * A FO(.) sentence is a FO(.) formula with no free occurences of variables
 * Sentences end with a dot.
 */
public class FodotSentence implements IFodotElement, IFodotTheoryElement {
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
		
		//Improve variable names (you can delete this if you think this is dirty)
		Set<FodotVariable> variables = new HashSet<FodotVariable>();
		for (IFodotSentenceElement el : formula.getElementsOfClass(FodotVariable.class)) {
			variables.add((FodotVariable) el);
		}
		NameUtil.improveGeneratedVariableNames(variables);
		
		this.formula = formula;
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
	
	
}
