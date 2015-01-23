package fodot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fodot.objects.FodotElementBuilder;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.inductivedefinitions.IFodotInductiveDefinitionElement;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotType;

public class FormulaUtil {
	
	/**
	 * This method makes a formula variablefree by adding "forall" structures around it
	 * @param formula
	 * @return
	 */
	public static IFodotFormula makeVariableFree(IFodotFormula formula) {
		IFodotFormula newFormula = formula;
		Set<FodotVariable> freeVars = formula.getFreeVariables();
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
		List<FodotType> result = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			result.add(type);
		}
		return result;
	}
}
