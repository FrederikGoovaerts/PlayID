package fodot.objects.structure;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.structure.enumerations.FodotEnumeration;

public class FodotStructure {

	private List<FodotEnumeration> enumerations;

	public FodotStructure(List<FodotEnumeration> enumerations) {
		super();
		this.enumerations = enumerations;
	}

	public void addEnumeration(FodotEnumeration enumeration) {
		enumerations.add(enumeration);
	}
	
	public void removeEnumeration(FodotEnumeration enumeration) {
		enumerations.remove(enumeration);
	}
	
	public List<FodotEnumeration> getEnumerations() {
		return new ArrayList<FodotEnumeration>(enumerations);
	}
	
	
}
