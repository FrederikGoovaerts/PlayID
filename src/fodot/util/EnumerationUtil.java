package fodot.util;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotType;

public class EnumerationUtil {
	
	
	private final static String PREDICATE_TERM_REGEX = "[a-zA-Z0-9_\\s]*[(][a-zA-Z0-9_()\\s]*[)][\\s]*";
	
	public static List<IFodotTypeEnumerationElement> toTypeEnumerationElements(List<String> values, List<FodotType> types) {
		if (values.size() != types.size()) {
			throw new IllegalArgumentException("Not equal sizes of arguments \nValues: " + values + "\nTypes: " + types);
		}
		List<IFodotTypeEnumerationElement> result = new ArrayList<IFodotTypeEnumerationElement>();
		for (int i = 0; i < values.size(); i++) {
			result.add(toTypeEnumerationElement(values.get(i), types.get(i)));
		}
		
		return result;
	}
	
	public static List<IFodotTypeEnumerationElement> toTypeEnumerationElements(List<String> values, FodotType type) {
		 return toTypeEnumerationElements(values, FormulaUtil.createTypeList(type, values.size()));
	}
	
	public static IFodotTypeEnumerationElement toTypeEnumerationElement(String value, FodotType type) {
		if (value.matches(PREDICATE_TERM_REGEX)) {
			throw new IllegalStateException("No predicate term converter yet :c");
		}
		return new FodotConstant(value, type);
	}
	
	
}
