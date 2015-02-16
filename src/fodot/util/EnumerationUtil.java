package fodot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.exceptions.answer.StructureParsingException;
import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.structure.elements.typeenum.elements.FodotTypeFunctionEnumerationElement;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.objects.vocabulary.elements.IFodotDomainElement;

public class EnumerationUtil {
	
	private final static String VAR_NAME = "[a-zA-Z0-9_()\\s]*";
	private final static String PREDICATE_TERM_REGEX = "^" + VAR_NAME + "[(]([\\s]*"+VAR_NAME+"[\\s]*[,])*[\\s]*"+VAR_NAME+"[\\s]*[)][\\s]*";

	private static final String OPENING_BRACKET = "{";
	private static final String CLOSING_BRACKET = "}";	
	
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
			return new FodotTypeFunctionEnumerationElement(termDecl, elements);		
		}
		return new FodotConstant(value, type).toEnumerationElement();
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
	
	public static int getArity(String domain) {
		if (!domain.contains("{")) {//No brackets: single value;
			return 1;
		} else {
			String oneElement = domain.split(";")[0];
			//count amount of commas + 1
			int result = 1;
			while (oneElement.contains(",")) {
				oneElement = oneElement.substring(oneElement.indexOf(",")+1);
				result += 1;
			}
			if (oneElement.contains("->")) {
				result += 1;
			}
			return result;
		}
	}
	
	/**********************************************
	 *  Enumeration recognision
	 ***********************************************/

	private static String RANGE_REGEX = "^[{][\\s]*[-]?[0-9]+[.][.][-]?[0-9]+[\\s]*[}]$";

	public static boolean containsRange(String line) {
		return line.trim().matches(RANGE_REGEX);
	}
	private static String SINGLE_VALUE_REGEX = "^[a-zA-Z0-9_()]*$";

	public static boolean isSingleValue(String line) {
		return line.trim().matches(SINGLE_VALUE_REGEX);
	}

	private static String DOMAIN_REGEX = "^[{][a-zA-Z0-9_();,.\\->\\s]*[}]$";

	public static boolean containsDomain(String line) {
		return line.trim().matches(DOMAIN_REGEX);
	}

	/**
	 * Returns whatever is between curly braces
	 */
	public static String extractDomain(String line) {
		int firstBracket = line.indexOf(OPENING_BRACKET)+1;
		int lastBracket = line.lastIndexOf(CLOSING_BRACKET);
		if (!containsDomain(line) || firstBracket < 0 || lastBracket < firstBracket) {
			return line.trim();
		}
		String domain = line.substring(firstBracket, lastBracket);
		return domain.trim();
	}
	
	public static boolean canBeTypeEnumeration(String domain) {
		return containsRange(domain) || (!domain.contains(";") && !domain.contains("->")) ;
	}
	
	public static boolean canBeFunctionEnumeration(String domain) {
		return !domain.contains(OPENING_BRACKET) || domain.contains("->");
	}

	/**********************************************/
	
	public static List<IFodotEnumerationElement> convertConstantsToEnumerations(Collection<? extends FodotConstant> constants) {
		List<IFodotEnumerationElement> result = new ArrayList<>();
		for (FodotConstant c : constants) {
			result.add(c.toEnumerationElement());
		}
		return result;
	}
}
