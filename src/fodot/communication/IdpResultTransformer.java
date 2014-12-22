package fodot.communication;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Sets.SetView;

import fodot.gdl_parser.Parser;
import fodot.objects.file.IFodotFile;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;

/**
 * This class will read the results of IDP and puts it in useful maps
 * @author Thomas
 *
 */
public class IdpResultTransformer {
	private String textualResult;
	private IFodotFile inputFodot;
	private List<IdpModel> models = new ArrayList<IdpModel>();
	private String vocName;
	private FodotVocabulary currentVocabulary; 

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
		IdpModel currentModel = new IdpModel();
		addModel(currentModel);
		for (String line : linesToProcess) {
			if (isThrowAwayLine(line)){
				//No-op
			} else if (isNewModelLine(line)) {
				currentModel = new IdpModel();
				addModel(currentModel);
			} else if (declaresNewStructure(line)) {
				processStructureLine(line);
			} else if (isAssignment(line)) {
				processAssignment(currentModel, line);				
			} else if (isEndOfStructure(line)) {
				currentModel = null;
				setCurrentVocabulary(null);
			} else {
				throw new IllegalArgumentException("Can't process " + line);
			}
		}


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
	}

	private boolean declaresNewStructure(String line) {
		return line.startsWith("structure");
	}

	private boolean isThrowAwayLine(String line) {
		return line.startsWith("====") || line.startsWith("Number of models");
	}

	public List<IdpModel> getModels() {
		return models;
	}
	
	public void addModel(IdpModel model) {
		models.add(model);
	}

	//New Model
	private boolean isNewModelLine(String line) {
		return line.trim().startsWith("Model");
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
		switch (getDomainType(name)) {
		case FUNCTION:
			model.addFunctionResult(name, extractMultivaluedResultDomain(domain));
			break;
		case PREDICATE:
			model.addPredicateResult(name, extractMultivaluedDomain(domain));
			break;
		case CONSTANT:
			model.addConstantResult(name, extractSinglevaluedDomain(domain));
			break;
		}
	}



	//Domain stuff

	private static final String OPENING_BRACKET = "{";
	private static final String CLOSING_BRACKET = "}";	
	private static final String MULTIVALUE_DIVIDER = ";";
	private static final String SINGLEVALUE_DIVIDER = ",";
	private static final String RESULT_DIVIDER = "->";

	private enum DomainType {FUNCTION, PREDICATE, CONSTANT};

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
			return DomainType.CONSTANT;
		}
		
		throw new IllegalArgumentException("Not a recognized class: " + el.getClass());
	}

	private boolean containsDomain(String line) {
		return line.contains("{") && line.contains("}");
	}

	/**
	 * Returns whatever is between curly braces
	 */
	private String extractDomain(String line) {
		int firstBracket = line.indexOf(OPENING_BRACKET)+1;
		int lastBracket = line.lastIndexOf(CLOSING_BRACKET);
		if (!containsDomain(line) || firstBracket < 0 || lastBracket < firstBracket) {
			return line;
//			throw new IllegalArgumentException(line + " does not contain curly braces: no domain can be extracted");
		}
		String domain = line.substring(firstBracket, lastBracket);
		return domain;
	}

	private List<String> extractSinglevaluedDomain(String line) {
		if (line.contains(MULTIVALUE_DIVIDER)) {
			throw new IllegalArgumentException("Tried parsing a multiargumented line with the constantdomain parser");
		}
		String domainString = extractDomain(line);

		//Fill domain elements
		List<String> domainElements = trimElements(splitOn(domainString, SINGLEVALUE_DIVIDER));	

		return domainElements;
	}

	private List<List<String>> extractMultivaluedDomain(String line) {
		if (line.contains(RESULT_DIVIDER)) {
			throw new IllegalStateException(line + " is a function domain, don't parse it with a predicate domain extractor");
		}
		String domainString = extractDomain(line);
		List<String> domainElements = trimElements(splitOn(domainString, MULTIVALUE_DIVIDER));
		List<List<String>> result = new ArrayList<List<String>>();
		for (String element : domainElements) {
			result.add(trimElements(splitOn(element, SINGLEVALUE_DIVIDER)));
		}
		return result;
	}

	private Map<List<String>,String> extractMultivaluedResultDomain(String line) {
		String domainString = extractDomain(line);
		List<String> domainElements = trimElements(splitOn(domainString, MULTIVALUE_DIVIDER));
		Map<List<String>,String> result = new HashMap<List<String>, String>();
		for (String element : domainElements) {
			if (element.contains(RESULT_DIVIDER)) {
				String[] splitted = element.split(RESULT_DIVIDER);
				String elementsToProcess = splitted[0];
				String resultString = splitted[1].trim();
				result.put(trimElements(splitOn(elementsToProcess, SINGLEVALUE_DIVIDER)), resultString);
			} else {
				result.put(Arrays.asList(""), element);
				
			}
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
	
	//MAIN
	
	public static void main(String[] args) {
		//		IdpResultTransformer trans = 
		Parser p = new Parser(new File("resources/games/blocks.kif"));
		new IdpResultTransformer(p.getParsedFodot(), "Number of models: 1"
				+ "\nModel 1"
				+ "\n======="
				+ "\nstructure  : V {"
				+ "\n  ScoreType = { 0..100 }"
				+ "\n  Time = { 0..20 }"
				+ "\n  C_clear = { 0,c_a; 0,c_b; 0,c_c; 1,c_a; 1,c_b; 2,c_a }"
				+ "\n  C_on = { 1,c_b,c_c; 2,c_a,c_b; 2,c_b,c_c }"
				+ "\n  C_step = { 0,c_2; 1,c_3; 2,c_4 }"
				+ "\n  C_table = { 0,c_a; 0,c_b; 0,c_c; 1,c_a; 1,c_c; 2,c_c }"
				+ "\n  I_clear = { c_b; c_c }"
				+ "\n  I_on = { c_c,c_a }"
				+ "\n  I_step = { c_1 }"
				+ "\n  I_table = { c_a; c_b }"
				+ "\n  clear = { 0,c_b; 0,c_c; 1,c_a; 1,c_b; 1,c_c; 2,c_a; 2,c_b; 3,c_a }"
				+ "\n  do = { 0,p_robot,u(c_c,c_a); 1,p_robot,s(c_b,c_c); 2,p_robot,s(c_a,c_b) }"
				+ "\n  on = { 0,c_c,c_a; 2,c_b,c_c; 3,c_a,c_b; 3,c_b,c_c }"
				+ "\n  step = { 0,c_1; 1,c_2; 2,c_3; 3,c_4 }"
				+ "\n  succ = { c_1,c_2; c_2,c_3; c_3,c_4 }"
				+ "\n  table = { 0,c_a; 0,c_b; 1,c_a; 1,c_b; 1,c_c; 2,c_a; 2,c_c; 3,c_c }"
				+ "\n  terminalTime = { 3 }"
				+ "\n  Next = { 0->1; 1->2; 2->3 }"
				+ "\n  Score = { p_robot->100 }"
				+ "\n  Start = 0"
				+ "\n	}");
	}

}
