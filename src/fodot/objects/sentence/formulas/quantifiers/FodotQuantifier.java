package fodot.objects.sentence.formulas.quantifiers;

import java.util.Set;

import fodot.objects.exceptions.IllegalConnectorException;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public abstract class FodotQuantifier implements IFodotFormula {
	private String symbol;
	private FodotVariable variable;
	private IFodotFormula formula;

	
	public FodotQuantifier(String symbol, FodotVariable variable, IFodotFormula formula) {
		super();
		if (!isValidSymbol(symbol)) {
			throw new IllegalConnectorException(this, symbol);
		}
		this.symbol = symbol;
		this.formula = formula;
		this.variable = variable;
	}

	public String getSymbol() {
		return symbol;
	}

	public FodotVariable getVariable() {
		return variable;
	}
	
	public IFodotFormula getFormula() {
		return formula;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> formulaVars = getFormula().getFreeVariables();
		//Remove the var that is being quantized by this formula
		while (formulaVars.contains(getVariable())) {
			formulaVars.remove(getVariable());
		}
		return formulaVars;
 	}

	@Override
	public String toCode() {
		return getSymbol() + " " + variable.getName() + " [" + variable.getType().getTypeName() + "] :" + getFormula().toCode();
	}
	
	public abstract boolean isValidSymbol(String symbol);

}
