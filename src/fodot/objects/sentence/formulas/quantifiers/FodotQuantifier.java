package fodot.objects.sentence.formulas.quantifiers;

import java.util.List;
import java.util.Set;

import fodot.objects.exceptions.IllegalConnectorException;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public abstract class FodotQuantifier implements IFodotFormula {
	private String symbol;
	private List<FodotVariable> variables;
	private IFodotFormula formula;

	
	public FodotQuantifier(String symbol, List<FodotVariable> variable, IFodotFormula formula) {
		super();
		if (!isValidSymbol(symbol)) {
			throw new IllegalConnectorException(this, symbol);
		}
		this.symbol = symbol;
		this.formula = formula;
		this.variables = variable;
	}

	public String getSymbol() {
		return symbol;
	}

	public List<FodotVariable> getVariable() {
		return variables;
	}
	
	public IFodotFormula getFormula() {
		return formula;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> formulaVars = getFormula().getFreeVariables();
		//Remove the var that is being quantized by this formula
		formulaVars.remove(getVariable());
		return formulaVars;
 	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(getSymbol() + " ");
		for (FodotVariable var : variables) {
			builder.append(var.getName() + " [" + var.getType().getTypeName() + "] ");
		}
		builder.append(" : " + getFormula().toCode());
		return builder.toString();
	}
	
	public abstract boolean isValidSymbol(String symbol);

}
