package fodot.objects.theory.elements.formulas;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fodot.exceptions.fodot.IllegalConnectorException;
import fodot.objects.theory.elements.IFodotSentenceElement;
import fodot.objects.theory.elements.terms.FodotVariable;

public class FodotQuantifier implements IFodotFormula {
	private String symbol;
	private Set<FodotVariable> variables;
	private IFodotFormula formula;

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

	public static boolean isValidSymbol(String symbol) {
		return symbol != null && symbol.matches(VALID_QUANTIFIER_REGEX);
	}
	
	/**********************************************/
	
	public FodotQuantifier(String argSymbol, Collection<? extends FodotVariable>  argVariables, IFodotFormula argFormula) {
		super();
		setSymbol(argSymbol);
		setVariables(argVariables);
		setFormula(argFormula);
	}

	/**********************************************
	 *  Data getters&setters
	 ***********************************************/

	//Symbol
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String argSymbol) {
		if (!isValidSymbol(argSymbol)) {
			throw new IllegalConnectorException(this, symbol);
		}
		this.symbol = argSymbol;
	}

	//Variable
	public Set<FodotVariable> getVariables() {
		return variables;
	}

	private void setVariables(Collection<? extends FodotVariable> argVariables) {
		this.variables = new LinkedHashSet<FodotVariable>(argVariables);
	}
	
	//Formula	
	public IFodotFormula getFormula() {
		return formula;
	}
	
	public void setFormula(IFodotFormula argFormula) {
		this.formula = argFormula;
	}

	/**********************************************/
	

	/**********************************************
	 *  Sentence element obligatories
	 ***********************************************/
	@Override
	public Set<IFodotSentenceElement> getElementsOfClass(Class<? extends IFodotSentenceElement> clazz) {
		Set<IFodotSentenceElement> result = new HashSet<IFodotSentenceElement>();
		
		//Check for all elements
		for (IFodotSentenceElement var : getVariables()) {
			result.addAll(var.getElementsOfClass(clazz));
		}
		result.addAll(getFormula().getElementsOfClass(clazz));
		
		//Check for this itself
		if (clazz.isAssignableFrom(this.getClass())) {
			result.add(this);
		}
		
		return result;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> formulaVars = getFormula().getFreeVariables();
		//Remove the var that is being quantized by this formula
		formulaVars.removeAll(getVariables());
		return formulaVars;
	}

	@Override
	public int getBindingOrder() {
		return BINDING_ORDERS.get(getSymbol().trim().substring(0, 1));
	}

	/**********************************************/

	
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

	@Override
	public String toString() {
		return "[quantifier: "+toCode()+"]";
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
