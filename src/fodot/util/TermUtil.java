package fodot.util;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.IFodotTheoryElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;

public class TermUtil {
	public List<String> getValues(List<FodotConstant> constants) {
		List<String> result = new ArrayList<String>();
		for (FodotConstant c : constants) {
			result.add(c.getValue());
		}
		return result;
	}
	
	public List<FodotVariable> extractVariables(IFodotTheoryElement element) {
		List<FodotVariable> result = new ArrayList<FodotVariable>();
		for (IFodotElement el : element.getElementsOfClass(FodotVariable.class)) {
			result.add((FodotVariable) el);
		}
		return result;
	}
}
