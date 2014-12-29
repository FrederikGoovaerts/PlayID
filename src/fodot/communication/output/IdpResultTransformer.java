package fodot.communication.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.file.IFodotFile;
import fodot.objects.file.IFodotFileElement;
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

/**
 * This class will read the results of IDP and puts it in useful maps
 * @author Thomas
 *
 */
public class IdpResultTransformer {
	private String textualResult;
	private IFodotFile inputFodot;
	IdpModel currentModel;
	private List<IdpModel> models = new ArrayList<IdpModel>();
	private String vocName;
	private FodotVocabulary currentVocabulary; 
	private boolean errorOccured;

	public IdpResultTransformer(IFodotFile inputFodot, String textResult) {
		super();
		setInputFodot(inputFodot);
		setTextualResult(textResult);
		processResult();
	}

	//Input	
	public String getTextualResult() {
		return textualResult;
	}
	public void setTextualResult(String result) {
		this.textualResult = result;
	}	

	public IFodotFile getInputFodot() {
		return inputFodot;
	}

	public void setInputFodot(IFodotFile inputFodot) {
		this.inputFodot = inputFodot;
	}



	//Processor of the answer
	public void processResult() {
		List<String> linesToProcess = trimElements(Arrays.asList(getTextualResult().split("\n")));


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
				processAssignment(getCurrentModel(), line);				
			} else if (isEndOfStructure(line)) {
				setCurrentModel(null);
				setCurrentVocabulary(null);
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
		this.vocName = line.substring(beginIndex, endIndex).trim();
		for (IFodotFileElement el : getInputFodot().getElementOf(FodotVocabulary.class)) {
			if (el.getName().equals(vocName)) {
				setCurrentVocabulary((FodotVocabulary) el);
			}
		}
		setCurrentModel(new IdpModel(getCurrentVocabulary()));

	}

	private boolean declaresNewStructure(String line) {
		return line.startsWith("structure");
	}

	private boolean isThrowAwayLine(String line) {
		if (line == null)
			return true;

		line = line.trim();
		return line.equals("")
				|| line.startsWith("Warning:")
				|| line.startsWith("====")
				|| line.startsWith("Number of models")
				|| line.startsWith("Model");
	}

	public List<IdpModel> getModels() {
		return models;
	}

	private void setCurrentModel(IdpModel idpModel) {
		if (idpModel != null) {
			addModel(idpModel);
		}
		this.currentModel = idpModel;
	}

	private IdpModel getCurrentModel() {
		return currentModel;
	}

	public void addModel(IdpModel model) {
		models.add(model);
	}

	//Name stuff
	private static final String ASSIGNMENT_CHARACTER = "=";

	private boolean isAssignment(String line) {
		return line.contains(ASSIGNMENT_CHARACTER);
	}

	public void processAssignment(IdpModel model, String line) {
		String[] splitted = line.split(ASSIGNMENT_CHARACTER);
		if (splitted.length != 2) {
			throw new IllegalArgumentException("Something went wrong when processing assignment: " + line);
		}
		String name = splitted[0].trim();
		String domain = splitted[1].trim();
		String extractedDomain = extractDomain(domain);

		IFodotStructureElement elementToAdd = null;
		IFodotVocabularyElement elementVocElement = getCurrentVocabulary().getElementWithName(name);
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
			model.getStructure().addElement(elementToAdd);
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

		IFodotVocabularyElement el = getCurrentVocabulary().getElementWithName(name);
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
		List<String> elementsToConvert = trimElements(splitOn(domainString, SINGLEVALUE_DIVIDER));
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
		List<String> domainElements = trimElements(splitOn(domainString, MULTIVALUE_DIVIDER));
		Set<IFodotPredicateEnumerationElement> result = new LinkedHashSet<IFodotPredicateEnumerationElement>();
		for (String element : domainElements) {
			result.add(
					new FodotPredicateEnumerationElement(
							EnumerationUtil.toTypeEnumerationElement(
									trimElements(splitOn(element, SINGLEVALUE_DIVIDER)),
									decl.getArgumentTypes())));
		}
		return result;
	}

	private Set<IFodotFunctionEnumerationElement> extractFunctionEnumerationElements(FodotFunctionDeclaration decl, String line) {
		String domainString = extractDomain(line);
		List<String> domainElements = trimElements(splitOn(domainString, MULTIVALUE_DIVIDER));
		Set<IFodotFunctionEnumerationElement> result = new LinkedHashSet<IFodotFunctionEnumerationElement>();
		//		Map<List<String>,String> result = new HashMap<List<String>, String>();
		for (String element : domainElements) {
			if (element.contains(RESULT_DIVIDER)) {
				String[] splitted = element.split(RESULT_DIVIDER);
				String elementsToProcess = splitted[0];
				String resultString = splitted[1].trim();

				List<IFodotTypeEnumerationElement> functionValues =
						EnumerationUtil.toTypeEnumerationElement(
								trimElements(splitOn(elementsToProcess, SINGLEVALUE_DIVIDER)),
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

	//General helpers

	private List<String> trimElements(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			list.set(i, list.get(i).trim());
		}
		return list;
	}


	private List<String> splitOn(String toSplit, String splitter) {
		List<String> result;
		if (toSplit.contains(splitter)) {
			result = Arrays.asList(toSplit.split(splitter));
		} else {
			result = Arrays.asList(toSplit);
		}

		result = concatElementsWithBrackets(result);

		return result;
	}

	/**
	 * This method concats elements if they contain open brackets so it's not split over multiple elements
	 * @param result
	 * @return
	 */
	private List<String> concatElementsWithBrackets(List<String> input) {
		List<String> result = new ArrayList<String>();
		int openBracketCounter = 0;
		String currentElement = null;
		for (String el : input) {
			openBracketCounter = openBracketCounter + getAmountOfCharInString('(', el) - getAmountOfCharInString(')', el);

			if (currentElement == null) {
				currentElement = el;
			} else {
				currentElement += ", " + el;
			}
			if (openBracketCounter == 0) {
				result.add(currentElement);
				currentElement = null;
			}
		}

		return result;
	}

	private int getAmountOfCharInString(char splitter, String string) {
		int result = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == splitter) {
				result += 1;
			}
		}
		return result;		
	}

	//CLASS VARIABLES
	public FodotVocabulary getCurrentVocabulary() {
		return currentVocabulary;
	}

	public void setCurrentVocabulary(FodotVocabulary currentVocabulary) {
		this.currentVocabulary = currentVocabulary;
	}

	public boolean hasErrorOccured() {
		return errorOccured;
	}

	private void setErrorOccured(boolean errorOccured) {
		this.errorOccured = errorOccured;
	}
}
