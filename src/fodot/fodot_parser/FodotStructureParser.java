package fodot.fodot_parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fodot.exceptions.answer.StructureParsingException;
import fodot.objects.file.IFodotFile;
import fodot.objects.general.IFodotElement;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.IFodotStructureElement;
import fodot.objects.structure.elements.functionenum.FodotConstantFunctionEnumeration;
import fodot.objects.structure.elements.functionenum.FodotFunctionEnumeration;
import fodot.objects.structure.elements.functionenum.elements.FodotFunctionEnumerationElement;
import fodot.objects.structure.elements.functionenum.elements.IFodotFunctionEnumerationElement;
import fodot.objects.structure.elements.predicateenum.FodotPredicateEnumeration;
import fodot.objects.structure.elements.predicateenum.elements.FodotPredicateEnumerationElement;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typenum.FodotNumericalTypeRangeEnumeration;
import fodot.objects.structure.elements.typenum.FodotTypeEnumeration;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionFullDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;
import fodot.util.EnumerationUtil;
import fodot.util.FormulaUtil;
import fodot.util.ParserUtil;

public class FodotStructureParser {
	private IFodotFile file;
	private FodotVocabulary vocabulary;
	private FodotStructure result;
	private List<String> toParse;

	public FodotStructureParser(IFodotFile file, List<String> toParse) {
		setInputFile(file);
		setToParse(toParse);
	}

	public FodotStructureParser(IFodotFile file, String... toParse) {
		this(file, Arrays.asList(toParse));
	}

	/**********************************************
	 *  Static parser
	 ***********************************************/
	public static FodotStructure parse(IFodotFile file, String... toParse) {
		return parse(file, Arrays.asList(toParse));
	}

	public static FodotStructure parse(IFodotFile file, List<String> toParse) {
		FodotStructureParser parser = new FodotStructureParser(file, toParse);
		return parser.getResultingStructure();

	}

	/**********************************************/


	/**********************************************
	 *  Parsing the structure
	 ***********************************************/
	//Processor of the answer
	public void process() {
		List<String> linesToProcess = ParserUtil.trimElements(ParserUtil.splitOn(getToParse(), "\n"));

		//Process do
		for (String line : linesToProcess) {
			if (isThrowAwayLine(line)){
				//No-op
			} else if (declaresNewStructure(line)) {
				processStructureLine(line);
			} else if (isAssignment(line)) {
				processAssignment(line);				
			} else if (isEndOfStructure(line)) {
				break;
			} else {
				throw new StructureParsingException("Can't process " + line);
			}
		}
	}
	/**********************************************/


	/**********************************************
	 *  Line type recognizers
	 ***********************************************/
	private boolean isEndOfStructure(String line) {
		return line.trim().equals("}");
	}


	private boolean declaresNewStructure(String line) {
		return line.startsWith("structure");
	}

	private boolean isThrowAwayLine(String line) {
		if (line == null)
			return true;

		return line.trim().equals("");
	}

	private static final String ASSIGNMENT_CHARACTER = "=";

	private boolean isAssignment(String line) {
		return line.contains(ASSIGNMENT_CHARACTER);
	}

	/**********************************************/

	/**********************************************
	 *  Initiate the resulting structure
	 ***********************************************/
	private void processStructureLine(String line) {
		int beginIndex = line.lastIndexOf(":")+1;
		int endIndex = line.lastIndexOf("{")-1;
		String vocName = line.substring(beginIndex, endIndex).trim();
		for (IFodotElement el : getInputFile().getDirectElementsOfClass(FodotVocabulary.class)) {
			if (el instanceof FodotVocabulary) {
				FodotVocabulary curVoc = (FodotVocabulary) el;
				if (curVoc.getName().equals(vocName)) {
					setVocabulary(curVoc);
				}
			}
		}
		setResultingStructure(new FodotStructure(getVocabulary()));

	}
	/**********************************************/

	/**********************************************
	 *  Assignment processor
	 ***********************************************/

	private void processAssignment(String line) {
		String[] splitted = line.split(ASSIGNMENT_CHARACTER);
		if (splitted.length != 2) {
			throw new StructureParsingException("Something went wrong when processing assignment: " + line);
		}
		String name = splitted[0].trim();
		String domain = splitted[1].trim();

		IFodotStructureElement elementToAdd = null;
		IFodotVocabularyElement elementVocElement = getVocabulary().getElementsWithName(name).get(0); //TODO: fix this: Count the arity of the given domain!
		switch (getDomainType(name)) {
		case TYPE:
			elementToAdd = createTypeEnumeration((FodotTypeDeclaration) elementVocElement, domain);
			break;
		case PREDICATE:
			elementToAdd = createPredicateEnumeration((FodotPredicateDeclaration) elementVocElement, domain);
			break;
		case FUNCTION:
			elementToAdd = createFunctionEnumeration((FodotFunctionFullDeclaration) elementVocElement, domain);
			break;
		default:
			throw new StructureParsingException("Trying to process an assignment of an unknown type");
		}

		if (elementToAdd != null) {
			getResultingStructure().addElement(elementToAdd);
		}
	}
	/**********************************************/

	private static final String OPENING_BRACKET = "{";
	private static final String CLOSING_BRACKET = "}";	
	private static final String MULTIVALUE_DIVIDER = ";";
	private static final String SINGLEVALUE_DIVIDER = ",";
	private static final String RESULT_DIVIDER = "->";
	private static final String RANGE_DIVIDER = "..";

	/**********************************************
	 *  Type Enumeration Parser
	 ***********************************************/

	private IFodotStructureElement createTypeEnumeration(FodotTypeDeclaration decl, String domain) {
		//Ranged type enumeration
		if (containsRange(domain)) {
			List<FodotConstant> range = extractTypeEnumerationRange(decl.getType(), domain);
			return new FodotNumericalTypeRangeEnumeration(decl, range.get(0), range.get(1));
		} 
		//Normal type enumeration
		else {
			return new FodotTypeEnumeration(decl, extractTypeEnumerationDomain(decl.getType(), domain));
		}	
	}

	/**
	 * Converts a comma separated string to type enumerations elements with given types
	 */
	private List<IFodotTypeEnumerationElement> extractTypeEnumerationDomain(List<FodotType> types, String line) {
		//Errorchecking
		if (line.contains(MULTIVALUE_DIVIDER)) {
			throw new StructureParsingException("Tried parsing a multiargumented line with the constantdomain parser");
		}

		//Extract the domainstring
		String domainString = extractDomain(line);

		//Get all elements as string
		List<String> elementsToConvert = ParserUtil.splitOnTrimmed(domainString, SINGLEVALUE_DIVIDER);

		//Pass all the elements to the enumerationcreator
		List<IFodotTypeEnumerationElement> domainElements = 
				EnumerationUtil.toTypeEnumerationElements(elementsToConvert, types);	

		return domainElements;
	}

	/**
	 * Converts a comma separated string to type enumerations elements with the given type
	 */
	private List<IFodotTypeEnumerationElement> extractTypeEnumerationDomain(FodotType type, String line) {
		int amountOfElements = ParserUtil.getAmountOfStringInString(SINGLEVALUE_DIVIDER, line);
		List<FodotType> types = FormulaUtil.createTypeList(type, amountOfElements);
		return extractTypeEnumerationDomain(types, line);

	}

	/**
	 * Extract 'a' and 'b' from {a..b} and converts them to constants of the given type
	 */
	private List<FodotConstant> extractTypeEnumerationRange(FodotType type, String line) {
		List<String> range = ParserUtil.splitOnTrimmed(extractDomain(line), RANGE_DIVIDER);
		List<FodotConstant> result = new ArrayList<FodotConstant>();
		result.add(new FodotConstant(range.get(0), type));
		result.add(new FodotConstant(range.get(1), type));
		return result;

	}

	/**********************************************/

	/**********************************************
	 *  Predicate Enumeration Parser
	 ***********************************************/

	private IFodotStructureElement createPredicateEnumeration(FodotPredicateDeclaration decl, String domain) {
		return new FodotPredicateEnumeration(decl, extractPredicateEnumerationElements(decl, domain));		
	}

	private Collection<? extends IFodotPredicateEnumerationElement> extractPredicateEnumerationElements(FodotPredicateDeclaration decl, String line) {

		Set<IFodotPredicateEnumerationElement> result = new LinkedHashSet<IFodotPredicateEnumerationElement>();
		//Error checking
		if (line.contains(RESULT_DIVIDER)) {
			throw new StructureParsingException(line + " is a function domain, don't parse it with a predicate domain extractor");
		}

		if (isSingleValue(line)) {
			result.add(new FodotPredicateEnumerationElement(Arrays.asList(new FodotConstant(line.trim(), FodotType.BOOLEAN))));
		} else if (containsDomain(line)) {
			//Remove the brackets
			String domainString = extractDomain(line);

			//Split all elements
			List<String> domainElements = ParserUtil.splitOnTrimmed(domainString, MULTIVALUE_DIVIDER);

			//Parse all elements as if they were lists of type enumerations
			for (String element : domainElements) {
				result.add(	new FodotPredicateEnumerationElement(
						extractTypeEnumerationDomain(decl.getArgumentTypes(), element)));
			}
		} else {
			throw new StructureParsingException(line + " does not contain a 'Single Value' nor a domain.");
		}
		return result;
	}

	/**********************************************/

	/**********************************************
	 *  Function Enumeration Parser
	 ***********************************************/

	private IFodotStructureElement createFunctionEnumeration(FodotFunctionFullDeclaration decl, String domain) {
		if (isSingleValue(domain)) {
			return new FodotConstantFunctionEnumeration(
					decl, EnumerationUtil.toTypeEnumerationElement(domain.trim(), decl.getReturnType()));
		} else {
			return new FodotFunctionEnumeration(decl, 
					extractFunctionEnumerationElements(decl, extractDomain(domain)));
		}
	}

	private Set<IFodotFunctionEnumerationElement> extractFunctionEnumerationElements(FodotFunctionFullDeclaration decl, String line) {
		//Remove domain brackets
		String domainString = extractDomain(line);


		List<String> domainElements = ParserUtil.splitOnTrimmed(domainString, MULTIVALUE_DIVIDER);
		Set<IFodotFunctionEnumerationElement> result = new LinkedHashSet<IFodotFunctionEnumerationElement>();

		for (String element : domainElements) {
			String[] splitted = element.split(RESULT_DIVIDER);
			String elementsToProcess = splitted[0];
			String resultString = splitted[1].trim();

			List<IFodotTypeEnumerationElement> functionValues =
					extractTypeEnumerationDomain(decl.getArgumentTypes(), elementsToProcess);
			IFodotTypeEnumerationElement functionReturn =
					EnumerationUtil.toTypeEnumerationElement(resultString, decl.getReturnType());

			result.add(new FodotFunctionEnumerationElement(functionValues, functionReturn));
		}
		return result;
	}

	/**********************************************/

	/**********************************************
	 *  Enumeration recognision
	 ***********************************************/


	private enum EnumerationType {FUNCTION, PREDICATE, TYPE};

	private EnumerationType getDomainType(String name) {

		IFodotVocabularyElement el = getVocabulary().getElementsWithName(name).get(0); // TODO: fix me: count the arity!
		if (el.getClass().equals(FodotFunctionFullDeclaration.class)) {
			return EnumerationType.FUNCTION;
		}
		if (el.getClass().equals(FodotPredicateDeclaration.class)) {
			return EnumerationType.PREDICATE;
		}
		if (el.getClass().equals(FodotTypeDeclaration.class)) {
			return EnumerationType.TYPE;
		}

		throw new StructureParsingException("Not a recognized class: " + el.getClass());
	}


	private static String RANGE_REGEX = "^[{][\\s]*[0-9]+[.][.][0-9]+[\\s]*[}]$";

	private boolean containsRange(String line) {
		return line.trim().matches(RANGE_REGEX);
	}
	private static String SINGLE_VALUE_REGEX = "^[a-zA-Z0-9_()]*$";

	private boolean isSingleValue(String line) {
		return line.trim().matches(SINGLE_VALUE_REGEX);
	}

	private static String DOMAIN_REGEX = "^[{][a-zA-Z0-9_();,.\\->\\s]*[}]$";

	private boolean containsDomain(String line) {
		return line.trim().matches(DOMAIN_REGEX);
	}

	/**
	 * Returns whatever is between curly braces
	 */
	private String extractDomain(String line) {
		int firstBracket = line.indexOf(OPENING_BRACKET)+1;
		int lastBracket = line.lastIndexOf(CLOSING_BRACKET);
		if (!containsDomain(line) || firstBracket < 0 || lastBracket < firstBracket) {
			return line.trim();
		}
		String domain = line.substring(firstBracket, lastBracket);
		return domain.trim();
	}

	/**********************************************/

	//Getters&Setters

	/**********************************************
	 *  toParse
	 ***********************************************/
	private List<String> getToParse() {
		return toParse;
	}

	private void setToParse(List<String> toParse) {
		this.toParse = toParse;
	}

	/**********************************************/


	/**********************************************
	 *  File
	 ***********************************************/
	private IFodotFile getInputFile() {
		return file;
	}

	private void setInputFile(IFodotFile file) {
		this.file = file;
	}
	/**********************************************/


	/**********************************************
	 *  Vocabulary
	 ***********************************************/
	private FodotVocabulary getVocabulary() {
		return vocabulary;
	}

	private void setVocabulary(FodotVocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}
	/**********************************************/

	/**********************************************
	 *  Resulting structure
	 ***********************************************/
	public FodotStructure getResultingStructure() {
		if (result == null) {
			process();
		}
		return result;
	}

	private void setResultingStructure(FodotStructure result) {
		this.result = result;
	}
	/**********************************************/

}
