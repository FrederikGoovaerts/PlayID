package fodot.communication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will read the results of IDP and puts it in useful maps
 * @author Thomas
 *
 */
public class IdpResultTransformer {
	public String input;

	public IdpResultTransformer(String idpResult) {
		super();
		setInput(idpResult);
		processResult();
	}
	
	//Input	
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}

	//Processor of the answer
	public void processResult() {
		
	}
	
	private void addConstantResult(String name,
			List<String> extractSinglevaluedDomain) {
		// TODO Auto-generated method stub
		
	}

	private void addPredicateResult(String name,
			List<List<String>> extractMultivaluedDomain) {
		// TODO Auto-generated method stub
		
	}

	private void addFunctionResult(String name,
			Map<List<String>, String> extractMultivaluedResultDomain) {
		// TODO Auto-generated method stub
		
	}	
	
	//Name stuff
	private static final String ASSIGNMENT_CHARACTER = "=";
	
	public String processAssignment(String line) {
		String[] splitted = line.split(ASSIGNMENT_CHARACTER);
		if (splitted.length != 2) {
			throw new IllegalArgumentException("Something went wrong when processing assignment: " + line);
		}
		
		String name = splitted[0];
		String domain = splitted[1];
		switch (getDomainType(name, domain)) {
		case FUNCTION:
			addFunctionResult(name, extractMultivaluedResultDomain(domain));
			break;
		case PREDICATE:
			addPredicateResult(name, extractMultivaluedDomain(domain));
			break;
		case CONSTANT:
			addConstantResult(name, extractSinglevaluedDomain(domain));
			break;
		}
		
		int equalsPos = line.indexOf(ASSIGNMENT_CHARACTER);
		if (equalsPos < 0) {
		}
		return line.substring(0, equalsPos).trim();
	}



	//Domain stuff

	private static final String OPENING_BRACKET = "{";
	private static final String CLOSING_BRACKET = "}";	
	private static final String MULTIVALUE_DIVIDER = ";";
	private static final String SINGLEVALUE_DIVIDER = ",";
	private static final String RESULT_DIVIDER = "->";
	
	private enum DomainType {FUNCTION, PREDICATE, CONSTANT};
	
	private DomainType getDomainType(String name, String domain) {
		//TODO: link with the IDP_transformator to know what type
		return null;
		
//		if (domain.contains(RESULT_DIVIDER)) {
//			return DomainType.FUNCTION;
//		}
//		if (domain.contains(MULTIVALUE_DIVIDER)) {
//			return DomainType.PREDICATE;
//		}
//		return DomainType.CONSTANT;
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
		if (firstBracket < 0 || lastBracket < firstBracket) {
			throw new IllegalArgumentException(line + " does not contain curly braces: no domain can be extracted");
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
		if (line.contains(RESULT_DIVIDER)) {
			throw new IllegalStateException(line + " is a function domain, don't parse it with a predicate domain extractor");
		}
		String domainString = extractDomain(line);
		List<String> domainElements = trimElements(splitOn(domainString, MULTIVALUE_DIVIDER));
		Map<List<String>,String> result = new HashMap<List<String>, String>();
		for (String element : domainElements) {
			String[] splitted = element.split(RESULT_DIVIDER);
			if (splitted.length != 2) {
				throw new IllegalStateException("No function divider for " + element);
			}
			String elementsToProcess = splitted[0];
			String resultString = splitted[1].trim();
			result.put(trimElements(splitOn(elementsToProcess, SINGLEVALUE_DIVIDER)), resultString);
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
		return result;
	}
	
	
	public static void main(String[] args) {
		IdpResultTransformer trans = new IdpResultTransformer("Number of models: 1"
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
