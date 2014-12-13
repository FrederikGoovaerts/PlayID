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
		setEnumerations(enumerations);
		setName(name);
		setVocabulary(vocabulary);
	}

	private static final String DEFAULT_NAME = "S";
	
	public FodotStructure(FodotVocabulary voc) {
		this(null, voc, new ArrayList<FodotEnumeration>());
	}

	
	/* NAME */

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = (name == null ? DEFAULT_NAME : name);
	}
	
	/* ENUMERATIONS */

	private void setEnumerations(List<FodotEnumeration> enumerat) {
		this.enumerations = (enumerat == null ? new ArrayList<FodotEnumeration>() : enumerat);
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

	/* VOCABULARY */

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	private void setVocabulary(FodotVocabulary voc) {
		this.vocabulary = (voc == null? new FodotVocabulary() : voc);
		
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

	/* MERGE */
	public void merge(FodotStructure other) {
		enumerations.addAll(other.getEnumerations());
	}

	
	
}
