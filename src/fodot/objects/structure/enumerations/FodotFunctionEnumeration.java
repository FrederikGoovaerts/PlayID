package fodot.objects.structure.enumerations;

import java.util.HashMap;
import java.util.Map;

import fodot.objects.vocabulary.elements.FodotType;

public class FodotFunctionEnumeration {
	public FodotType type;
	public Map<String[], String> values;
	
	public FodotFunctionEnumeration(FodotType type) {
		this(type, new HashMap<String[], String>());
	}
	
	public FodotFunctionEnumeration(FodotType type, Map<String[], String> values) {
		super();
		this.type = type;
		this.values = values;
	}

	public FodotType getType() {
		return type;
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
		
}
