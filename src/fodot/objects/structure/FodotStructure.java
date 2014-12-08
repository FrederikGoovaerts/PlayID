package fodot.objects.structure;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.IFodotElement;
import fodot.objects.structure.enumerations.FodotEnumeration;
import fodot.objects.vocabulary.FodotVocabulary;

public class FodotStructure implements IFodotElement {

	private List<FodotEnumeration> enumerations;
	private String name;
	private FodotVocabulary vocabulary;

	public FodotStructure(String name, FodotVocabulary vocabulary, List<FodotEnumeration> enumerations) {
		super();
		this.enumerations = enumerations;
		this.name = name;
		this.vocabulary = vocabulary;
	}
	
	private static final String DEFAULT_NAME = "S";
	
	public FodotStructure(FodotVocabulary voc) {
		this(DEFAULT_NAME, voc, new ArrayList<FodotEnumeration>());
	}

	
	/* NAME */

	public String getName() {
		return name;
	}
	
	/* ENUMERATIONS */
	
	public void addEnumeration(FodotEnumeration enumeration) {
		enumerations.add(enumeration);
	}
	
	public void removeEnumeration(FodotEnumeration enumeration) {
		enumerations.remove(enumeration);
	}
	
	public List<FodotEnumeration> getEnumerations() {
		return new ArrayList<FodotEnumeration>(enumerations);
	}

	/* VOCABULARY */

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	/* FODOT ELEMENT */
	
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("structure "+getName() + " : " + getVocabulary() + " {");
		for (FodotEnumeration enumeration : enumerations) {
			builder.append(enumeration.toCode() + "\n");
		}
		builder.append("}");
		return builder.toString();
	}

	
	
}
