package fodot.objects.structure.enumerations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fodot.objects.util.CollectionUtil;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;

public class FodotFunctionEnumeration extends FodotEnumeration {
	public FodotFunctionDeclaration function;
	public Map<String[], String> values;
	
	public FodotFunctionEnumeration(FodotFunctionDeclaration function) {
		this(function, new HashMap<String[], String>());
	}
	
	public FodotFunctionEnumeration(FodotFunctionDeclaration function, Map<String[], String> values) {
		super();
		this.function = function;
		this.values = values;
	}

	public FodotFunctionDeclaration getFunctionType() {
		return function;
	}

	/* VALUES */
	
	public void addValue(String[] input, String value) {
		//TODO: some kind of validator for the amount of and type of arguments
		values.put(input, value);
	}
	
	public void removeValue(String[] input) {
		values.remove(input);
	}
	
	public boolean containsValue(String[] input) {
		return values.containsKey(input);
	}
	
	public Map<String[], String> getValues() {
		return new HashMap<String[], String>(values);
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(function.getName() + " = {");
		for (String[] key : getValues().keySet()) {
			builder.append(CollectionUtil.toCouple(Arrays.asList(key)) + " -> " + values.get(key) + "; \n");
		}
		builder.append("}");
		return builder.toString();
	}
		
}
