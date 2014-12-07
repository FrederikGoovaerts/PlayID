package fodot.objects.sentence.formulas.quantifiers;

import java.util.List;

import fodot.objects.exceptions.IllegalConnectorException;
import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public abstract class FodotQuantifier implements FodotFormula {
	private String symbol;
	private FodotVariable variable;
	private FodotFormula formula;

	
	public FodotQuantifier(String symbol, FodotVariable variable, FodotFormula formula) {
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
	
	public FodotFormula getFormula() {
		return formula;
	}
	
	@Override
	public List<FodotVariable> getFreeVariables() {
		List<FodotVariable> formulaVars = getFormula().getFreeVariables();
		//Remove the var that is being quantized by this formula
		if (formulaVars.contains(getVariable())) {
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
