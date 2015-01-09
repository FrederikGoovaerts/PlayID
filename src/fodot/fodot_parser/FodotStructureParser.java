package fodot.fodot_parser;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.file.IFodotFile;
import fodot.objects.file.IFodotFileElement;
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
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
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
	public FodotVocabulary getVocabulary() {
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


	/**********************************************
	 *  Parsing the structure
	 ***********************************************/
	//Processor of the answer
	public void process() {
		List<String> linesToProcess = ParserUtil.trimElements(ParserUtil.splitOn(getToParse(), "\n"));

		//Process do
		for (String line : linesToProcess) {
			if (isError(line)) {
				setErrorOccured(true);
				break;
			} if (isThrowAwayLine(line)){
				//No-op
			} else if (declaresNewStructure(line)) {
				processStructureLine(line);
			} else if (isAssignment(line)) {
				processAssignment(line);				
			} else if (isEndOfStructure(line)) {
				break;
			} else {
				throw new IllegalArgumentException("Can't process " + line);
			}
		}


	}

	private boolean isError(String line) {
		return line.startsWith("Error: ");
	}

	private boolean isEndOfStructure(String line) {
		return line.trim().equals("}");
	}

	private void processStructureLine(String line) {
		int beginIndex = line.lastIndexOf(":")+1;
		int endIndex = line.lastIndexOf("{")-1;
		String vocName = line.substring(beginIndex, endIndex).trim();
		for (IFodotFileElement el : getInputFile().getElementOf(FodotVocabulary.class)) {
			if (el.getName().equals(vocName)) {
				setVocabulary((FodotVocabulary) el);
			}
		}
		setResultingStructure(new FodotStructure(getVocabulary()));

	}

	private boolean declaresNewStructure(String line) {
		return line.startsWith("structure");
	}

	private boolean isThrowAwayLine(String line) {
		if (line == null)
			return true;

		return line.trim().equals("");
	}


	//Name stuff
	private static final String ASSIGNMENT_CHARACTER = "=";

	private boolean isAssignment(String line) {
		return line.contains(ASSIGNMENT_CHARACTER);
	}

	public void processAssignment(String line) {
		String[] splitted = line.split(ASSIGNMENT_CHARACTER);
		if (splitted.length != 2) {
			throw new IllegalArgumentException("Something went wrong when processing assignment: " + line);
		}
		String name = splitted[0].trim();
		String domain = splitted[1].trim();
		String extractedDomain = extractDomain(domain);

		IFodotStructureElement elementToAdd = null;
		IFodotVocabularyElement elementVocElement = getVocabulary().getElementWithName(name);
		switch (getDomainType(name)) {
		case FUNCTION:
			FodotFunctionDeclaration funcDeclaration = (FodotFunctionDeclaration) elementVocElement;
			if (!containsDomain(domain)) {
				elementToAdd = new FodotConstantFunctionEnumeration(
						funcDeclaration,
						EnumerationUtil.toTypeEnumerationElement(domain.trim(), funcDeclaration.getReturnType()));
			} else {
				elementToAdd = new FodotFunctionEnumeration(funcDeclaration, 
						extractFunctionEnumerationElements(funcDeclaration, extractedDomain));
			}
			break;
		case PREDICATE:
			FodotPredicateDeclaration predDeclaration = (FodotPredicateDeclaration) elementVocElement;
			elementToAdd = new FodotPredicateEnumeration( predDeclaration,
					extractPredicateEnumerationElements(predDeclaration, domain));
			break;
		case TYPE:
			FodotTypeDeclaration typeDeclaration = (FodotTypeDeclaration) elementVocElement;
			if (containsRange(extractedDomain)) {

				elementToAdd = new FodotNumericalTypeRangeEnumeration(
						typeDeclaration,
						new FodotConstant(extractedDomain.split("..")[0], typeDeclaration.getType()),
						new FodotConstant(extractedDomain.split("..")[1], typeDeclaration.getType())
						);
			} else {
				elementToAdd = new FodotTypeEnumeration(typeDeclaration,
						extractSinglevaluedDomain(typeDeclaration, domain));
			}
			break;
		default:
			throw new IllegalStateException("Not yet implemented :c");
		}
		if (elementToAdd != null)
			getResultingStructure().addElement(elementToAdd);
	}

	//Range stuff
	private static String RANGE_REGEX = "^[{][0-9]*..[0-9]*[}]$";

	private boolean containsRange(String line) {
		return line.trim().matches(RANGE_REGEX);
	}

	//Domain stuff

	private static final String OPENING_BRACKET = "{";
	private static final String CLOSING_BRACKET = "}";	
	private static final String MULTIVALUE_DIVIDER = ";";
	private static final String SINGLEVALUE_DIVIDER = ",";
	private static final String RESULT_DIVIDER = "->";

	private enum DomainType {FUNCTION, PREDICATE, TYPE};

	private DomainType getDomainType(String name) {
		//TODO: link with the IDP_transformator to know what type

		IFodotVocabularyElement el = getVocabulary().getElementWithName(name);
		if (el.getClass().equals(FodotFunctionDeclaration.class)) {
			return DomainType.FUNCTION;
		}
		if (el.getClass().equals(FodotPredicateDeclaration.class)) {
			return DomainType.PREDICATE;
		}
		if (el.getClass().equals(FodotTypeDeclaration.class)) {
			return DomainType.TYPE;
		}

		throw new IllegalArgumentException("Not a recognized class: " + el.getClass());
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


	private List<IFodotTypeEnumerationElement> extractSinglevaluedDomain(FodotTypeDeclaration decl, String line) {
		if (line.contains(MULTIVALUE_DIVIDER)) {
			throw new IllegalArgumentException("Tried parsing a multiargumented line with the constantdomain parser");
		}
		String domainString = extractDomain(line);

		//Fill domain elements
		List<String> elementsToConvert = ParserUtil.trimElements(ParserUtil.splitOn(domainString, SINGLEVALUE_DIVIDER));
		List<IFodotTypeEnumerationElement> domainElements = 
				EnumerationUtil.toTypeEnumerationElement(
						elementsToConvert, FormulaUtil.createTypeList(decl.getType(), elementsToConvert.size()));	

		return domainElements;
	}

	private Set<IFodotPredicateEnumerationElement> extractPredicateEnumerationElements(FodotPredicateDeclaration decl, String line) {
		if (line.contains(RESULT_DIVIDER)) {
			throw new IllegalStateException(line + " is a function domain, don't parse it with a predicate domain extractor");
		}
		String domainString = extractDomain(line);
		List<String> domainElements = ParserUtil.trimElements(ParserUtil.splitOn(domainString, MULTIVALUE_DIVIDER));
		Set<IFodotPredicateEnumerationElement> result = new LinkedHashSet<IFodotPredicateEnumerationElement>();
		for (String element : domainElements) {
			result.add(
					new FodotPredicateEnumerationElement(
							EnumerationUtil.toTypeEnumerationElement(
									ParserUtil.trimElements(ParserUtil.splitOn(element, SINGLEVALUE_DIVIDER)),
									decl.getArgumentTypes())));
		}
		return result;
	}

	private Set<IFodotFunctionEnumerationElement> extractFunctionEnumerationElements(FodotFunctionDeclaration decl, String line) {
		String domainString = extractDomain(line);
		List<String> domainElements = ParserUtil.trimElements(ParserUtil.splitOn(domainString, MULTIVALUE_DIVIDER));
		Set<IFodotFunctionEnumerationElement> result = new LinkedHashSet<IFodotFunctionEnumerationElement>();
		//		Map<List<String>,String> result = new HashMap<List<String>, String>();
		for (String element : domainElements) {
			if (element.contains(RESULT_DIVIDER)) {
				String[] splitted = element.split(RESULT_DIVIDER);
				String elementsToProcess = splitted[0];
				String resultString = splitted[1].trim();

				List<IFodotTypeEnumerationElement> functionValues =
						EnumerationUtil.toTypeEnumerationElement(
								ParserUtil.trimElements(ParserUtil.splitOn(elementsToProcess, SINGLEVALUE_DIVIDER)),
								decl.getArgumentTypes()); 
				IFodotTypeEnumerationElement functionReturn =
						EnumerationUtil.toTypeEnumerationElement(resultString, decl.getReturnType());

				result.add(new FodotFunctionEnumerationElement(functionValues, functionReturn));
			}
			//			else {
			//				result.add(new FodotConstantFunctionEnumeration(decl, new FodotConstant(element, decl.getReturnType())));
			//
			//			}
		}
		return result;
	}


	//CLASS VARIABLES
	private boolean errorOccured = false;
	public boolean hasErrorOccured() {
		return errorOccured;
	}

	private void setErrorOccured(boolean errorOccured) {
		this.errorOccured = errorOccured;
	}

	/**********************************************/




}
