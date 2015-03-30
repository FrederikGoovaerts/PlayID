package playid.util;

import java.util.ArrayList;
import java.util.List;

import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.theory.elements.IFodotTheoryElement;
import playid.domain.fodot.theory.elements.terms.FodotConstant;
import playid.domain.fodot.theory.elements.terms.FodotVariable;

public class TermUtil {
	
	
	public static List<String> getValues(List<FodotConstant> constants) {
		List<String> result = new ArrayList<String>();
		for (FodotConstant c : constants) {
			result.add(c.getValue());
		}
		return result;
	}
	
	public static List<FodotVariable> extractVariables(IFodotTheoryElement element) {
		List<FodotVariable> result = new ArrayList<FodotVariable>();
		for (IFodotElement el : element.getAllInnerElementsOfClass(FodotVariable.class)) {
			result.add((FodotVariable) el);
		}
		return result;
	}
}
