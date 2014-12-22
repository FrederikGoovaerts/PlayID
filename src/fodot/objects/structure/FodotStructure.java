package fodot.objects.structure;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fodot.objects.file.FodotFileElement;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.vocabulary.FodotVocabulary;

public class FodotStructure extends FodotFileElement<IFodotStructureElement> implements IFodotFileElement {

	private FodotVocabulary vocabulary;
	private static final String DEFAULT_NAME = "S";

	public FodotStructure(String name, FodotVocabulary vocabulary, Collection<? extends IFodotStructureElement> elements) {
		super(name, elements, vocabulary);
		setVocabulary(vocabulary);
	}

	
	public FodotStructure(FodotVocabulary voc) {
		this(null, voc, null);
	}
	
	/* NAMES */
	@Override
	public String getDefaultName() {
		return DEFAULT_NAME;
	}
	
	@Override
	public String getFileElementName() {
		return "structure";
	}
	
	/* VOCABULARY */

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	private void setVocabulary(FodotVocabulary voc) {
		this.vocabulary = (voc == null? new FodotVocabulary() : voc);
		
	}

	/* FODOT FILE ELEMENT */
	
	public void merge(FodotStructure other) {
		addAllElements(other.getElements());
	}


	@Override
	public Set<IFodotFileElement> getPrerequiredElements() {
		return new HashSet<IFodotFileElement>(Arrays.asList(getVocabulary()));
	}


	@Override
	public void mergeWith(IFodotFileElement other) {
		if (this.getClass().equals(other.getClass())) {
			merge((FodotStructure) other);
		}
	}

	
	
}
