package playid.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import playid.domain.fodot.FodotElementBuilder;
import playid.domain.fodot.theory.elements.formulas.IFodotFormula;
import playid.domain.fodot.theory.elements.inductivedefinitions.IFodotInductiveDefinitionElement;
import playid.domain.fodot.theory.elements.terms.FodotVariable;
import playid.domain.fodot.vocabulary.elements.FodotType;

public class FormulaUtil {
	
	/**
	 * This method makes a formula variablefree by adding "forall" structures around it
	 * @param formula
	 * @return
	 */
	public static IFodotFormula makeVariableFree(IFodotFormula formula) {
		return makeVariableFree(formula, new HashSet<FodotVariable>());
	}

    public static IFodotFormula makeVariableFree(IFodotFormula formula, Set<FodotVariable> exceptions) {
        IFodotFormula newFormula = formula;
        Set<FodotVariable> freeVars = formula.getFreeVariables();
        freeVars.removeAll(exceptions);
        if(!freeVars.isEmpty()) {
            newFormula = FodotElementBuilder.createForAll(freeVars, newFormula);
        }
        return newFormula;
    }
	
	public static IFodotInductiveDefinitionElement makeVariableFreeInductive(IFodotInductiveDefinitionElement formula) {
		IFodotInductiveDefinitionElement newFormula = formula;
		Set<FodotVariable> freeVars = formula.getFreeVariables();
		if(!freeVars.isEmpty()) {
			newFormula = FodotElementBuilder.createInductiveForAll(freeVars, newFormula);
		}
		return newFormula;
	} 
	
	public static List<FodotType> createTypeList(FodotType type, int amount) {
		List<FodotType> result = new ArrayList<FodotType>();
		for (int i = 0; i < amount; i++) {
			result.add(type);
		}
		return result;
	}
	
	public static List<FodotType> removeTypes(List<FodotType> list, FodotType type) {
		List<FodotType> result = new ArrayList<FodotType>(list);
		while (result.contains(type)) {
			result.remove(type);
		}
		return result;
	}
}
