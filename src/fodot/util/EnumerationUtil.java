package fodot.util;

import java.util.ArrayList;
import java.util.List;

import fodot.exceptions.answer.StructureParsingException;
import fodot.objects.structure.elements.typenum.elements.FodoTypeFunctionEnumerationElement;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.IFodotDomainElement;

public class EnumerationUtil {
	
	private final static String VAR_NAME = "[a-zA-Z0-9_()\\s]*";
	private final static String PREDICATE_TERM_REGEX = "^" + VAR_NAME + "[(]([\\s]*"+VAR_NAME+"[\\s]*[,])*[\\s]*"+VAR_NAME+"[\\s]*[)][\\s]*";
	
	public static List<IFodotTypeEnumerationElement> toTypeEnumerationElements(List<String> values, List<FodotType> types) {
		if (values.size() != types.size()) {
			throw new StructureParsingException("Not equal sizes of arguments \nValues: " + values + "\nTypes: " + types);
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
			
			int firstBracketPosition = value.indexOf("(");
			String termName = value.substring(0, firstBracketPosition).trim();
			FodotTypeFunctionDeclaration termDecl = getPredicateTermDeclaration(termName, type);
			
			String allElementsString = value.substring(firstBracketPosition+1, value.lastIndexOf(")")).trim();
			List<IFodotTypeEnumerationElement> elements = toTypeEnumerationElements(ParserUtil.splitOnTrimmed(allElementsString, ","), termDecl.getArgumentTypes());
			return new FodoTypeFunctionEnumerationElement(termDecl, elements);		
		}
		return new FodotConstant(value, type);
	}
	
	private static FodotTypeFunctionDeclaration getPredicateTermDeclaration(String name, FodotType type) {
		for (IFodotDomainElement el : type.getDomainElements()) {
			if (el instanceof FodotTypeFunctionDeclaration) {
				FodotTypeFunctionDeclaration casted = (FodotTypeFunctionDeclaration) el;
				if (casted.getName().equals(name)) {
					return casted;
				}
			}
		}
		return null;
	}
}
