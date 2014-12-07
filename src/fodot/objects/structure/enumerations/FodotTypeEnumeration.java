package fodot.objects.structure.enumerations;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.vocabulary.elements.FodotType;

public class FodotTypeEnumeration {
	public FodotType type;
	public List<String> values;
	
	public FodotTypeEnumeration(FodotType type) {
		this(type, new ArrayList<String>());
	}
	
	public FodotTypeEnumeration(FodotType type, List<String> values) {
		super();
		this.type = type;
		this.values = values;
	}

	public FodotType getType() {
		return type;
	}

	/* VALUES */
	
	public void addValue(String value) {
		values.add(value);
	}
	
	public void removeValue(String value) {
		values.remove(value);
	}
	
	public boolean containsValue(String value) {
		return values.contains(value);
	}
	
	public List<String> getValues() {
		return new ArrayList<String>(values);
	}
		
}
