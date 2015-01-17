package fodot.objects.structure;

import java.util.Collection;

import fodot.objects.file.FodotFileElementWithNamedElements;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.structure.elements.IFodotStructureElement;
import fodot.objects.vocabulary.FodotVocabulary;

public class FodotStructure extends FodotFileElementWithNamedElements<IFodotStructureElement> implements IFodotFileElement {

	private FodotVocabulary vocabulary;
	private static final String DEFAULT_NAME = "S";

	public FodotStructure(String name, FodotVocabulary vocabulary, Collection<? extends IFodotStructureElement> elements) {
		super(name, elements, vocabulary);
		setVocabulary(vocabulary);
	}

	public FodotStructure(String name, FodotVocabulary voc) {
		this(name, voc, null);
	}
	
	public FodotStructure(FodotVocabulary voc) {
		this(null, voc, null);
	}
	
	/* NAMES */
	@Override
	public String getFileElementName() {
		return "structure";
	}
	
	@Override
	public String getDefaultName() {
		return DEFAULT_NAME;
	}
	
	/* VOCABULARY */

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	private void setVocabulary(FodotVocabulary voc) {
		this.vocabulary = (voc == null? new FodotVocabulary() : voc);
		
	}	
	
}
