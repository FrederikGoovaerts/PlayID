package fodot.objects.structure.enumerations;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.type.FodotType;

public class FodotPredicateEnumeration {
	public FodotType type;
	public List<String[]> values;
	
	public FodotPredicateEnumeration(FodotType type) {
		this(type, new ArrayList<String[]>());
	}
	
	public FodotPredicateEnumeration(FodotType type, List<String[]> values) {
		super();
		this.type = type;
		this.values = values;
	}

	public FodotType getType() {
		return type;
	}

	/* VALUES */
	
	public void addValue(String[] value) {
		//TODO: some kind of validator for the amount of and type of arguments
		values.add(value);
	}
	
	public void removeValue(String[] value) {
		values.remove(value);
	}
	
	public boolean containsValue(String[] value) {
		return values.contains(value);
	}
	
	public List<String[]> getValues() {
		return new ArrayList<String[]>(values);
	}
		
}
