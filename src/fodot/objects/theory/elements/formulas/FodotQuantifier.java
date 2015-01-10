package fodot.objects.theory.elements.formulas;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fodot.exceptions.IllegalConnectorException;
import fodot.objects.theory.elements.terms.FodotVariable;

public class FodotQuantifier implements IFodotFormula {
	private String symbol;
	private Set<FodotVariable> variables;
	private IFodotFormula formula;
	private boolean shouldShowBrackets = true;

	/**********************************************
	 *  Static map with bindingorder
	 ***********************************************/
	
	private static final String VALID_QUANTIFIER_REGEX = "!|([\\?]([=][<]|[<>]|=)?[0-9]*)|\\?";

	private static final Map<String, Integer> BINDING_ORDERS;
    static {
        Map<String, Integer> connectorsMap = new HashMap<String, Integer>();
	    connectorsMap.put("!", 26);
	    connectorsMap.put("?", 27);
        BINDING_ORDERS = Collections.unmodifiableMap(connectorsMap);
    }
	
	/**********************************************/
	
	public FodotQuantifier(String symbol, Set<FodotVariable> variable, IFodotFormula formula) {
		super();
		if (!isValidSymbol(symbol)) {
			throw new IllegalConnectorException(this, symbol);
		}
		this.symbol = symbol;
		this.formula = formula;
		this.variables = variable;
	}

	//Symbol
	public String getSymbol() {
		return symbol;
	}

	//Variable
	public Set<FodotVariable> getVariable() {
		return variables;
	}

	//Formula	
	public IFodotFormula getFormula() {
		return formula;
	}

	//Brackets

	protected boolean shouldShowBrackets() {
		return shouldShowBrackets;
	}

	protected void setShouldShowBrackets(boolean shouldShowBrackets) {
		this.shouldShowBrackets = shouldShowBrackets;
	}


	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> formulaVars = getFormula().getFreeVariables();
		//Remove the var that is being quantized by this formula
		formulaVars.removeAll(getVariable());
		return formulaVars;
	}

	@Override
	public int getBindingOrder() {
		return BINDING_ORDERS.get(getSymbol().trim().substring(0, 1));
	}
	
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(getSymbol() + " ");
		for (FodotVariable var : variables) {
			builder.append(var.getName() + " [" + var.getType().getName() + "] ");
		}
		
		builder.append(" : ");
		
		if (getBindingOrder() >= 0
				&& getFormula().getBindingOrder() >= getBindingOrder()) {
			builder.append( "(" + getFormula().toCode() + ")");
		} else {
			builder.append( getFormula().toCode());
		}
		
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
