package fodot.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdpModel {

	private Map<String, List<String>> constants;
	private Map<String, List<List<String>>> predicates;
	private Map<String, Map<List<String>, String>> functions;
	
	public IdpModel() {
		super();
		this.constants = new HashMap<String, List<String>>();
		this.predicates = new HashMap<String, List<List<String>>>();
		this.functions = new HashMap<String, Map<List<String>, String>>();
	}
	
	public void addConstantResult(String name,	List<String> extractSinglevaluedDomain) {
		constants.put(name, extractSinglevaluedDomain);
		
	}
	
	public void addPredicateResult(String name, List<List<String>> extractMultivaluedDomain) {
		predicates.put(name, extractMultivaluedDomain);
		
	}

	public void addFunctionResult(String name,	Map<List<String>, String> extractMultivaluedResultDomain) {
		functions.put(name, extractMultivaluedResultDomain);
		
	}	
	


}
