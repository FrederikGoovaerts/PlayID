package playid.domain.gdl_transformers.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.gdl.grammar.GdlVariable;

import playid.domain.fodot.theory.elements.terms.FodotVariable;

public class VariableRegisterer {
	private Map<GdlVariable, FodotVariable> variableMap;
	private Set<FodotVariable> usedVariables;
	
	public VariableRegisterer() {
		this(new HashMap<GdlVariable, FodotVariable>());
	}
	
	public VariableRegisterer(Map<GdlVariable, FodotVariable> variables) {
		this.variableMap = variables;
		this.usedVariables = new HashSet<FodotVariable>();
		if (variables != null && variables.values() != null) {
			usedVariables.addAll(variables.values());
		}
	}

	/**********************************************
	 *  Register block
	 ***********************************************/

	public void addTranslation(GdlVariable gdl, FodotVariable fodot) {
		if (hasTranslationFor(gdl)) {
			throw new IllegalArgumentException(gdl + " already has been mapped!");
		}
		variableMap.put(gdl, fodot);
		usedVariables.add(fodot);
	}
	
	public void registerVariable(FodotVariable fodot) {
		usedVariables.add(fodot);
	}	

	/**********************************************/
	
	public Set<FodotVariable> getRegisteredVariables() {
		return new HashSet<FodotVariable>(usedVariables);
	}
	
	public boolean hasTranslationFor(GdlVariable gdl) {
		return variableMap.containsKey(gdl);
	}
	
	public FodotVariable translate(GdlVariable gdl) {
		if (!hasTranslationFor(gdl)) {
			throw new IllegalArgumentException("There's no translation for " + gdl);
		}
		return variableMap.get(gdl);
	}
}
