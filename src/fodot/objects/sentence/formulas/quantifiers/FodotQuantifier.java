package fodot.objects.sentence.formulas.quantifiers;

import java.util.Set;

import fodot.exceptions.IllegalConnectorException;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public class FodotQuantifier implements IFodotFormula {
	private String symbol;
	private Set<FodotVariable> variables;
	private IFodotFormula formula;

	/* VALID SYMBOL */
//	private static final List<String> VALID_SYMBOLS =
//			Arrays.asList(new String[]{"!", "?"});
	private static final String VALID_QUANTIFIER_REGEX = "!|([\\?]([=][<]|[<>]|=)?[0-9]*)|\\?";
	
	public FodotQuantifier(String symbol, Set<FodotVariable> variable, IFodotFormula formula) {
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

	public Set<FodotVariable> getVariable() {
		return variables;
	}
	
	public IFodotFormula getFormula() {
		return formula;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> formulaVars = getFormula().getFreeVariables();
		//Remove the var that is being quantized by this formula
		formulaVars.removeAll(getVariable());
		return formulaVars;
 	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("(" + getSymbol() + " ");
		for (FodotVariable var : variables) {
			builder.append(var.getName() + " [" + var.getType().getName() + "] ");
		}
		builder.append(" : " + getFormula().toCode()+")");
		return builder.toString();
	}
	
	public boolean isValidSymbol(String symbol) {
		return symbol.matches(VALID_QUANTIFIER_REGEX);
	}

	@Override
	public String toString() {
		return "[quantifier ("+ getSymbol() +") "+getVariable()+" in "+getFormula()+"]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result
				+ ((variables == null) ? 0 : variables.hashCode());
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
		FodotQuantifier other = (FodotQuantifier) obj;
		if (formula == null) {
			if (other.formula != null)
				return false;
		} else if (!formula.equals(other.formula))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}
	
	

}
