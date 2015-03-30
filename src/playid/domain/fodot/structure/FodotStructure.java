package playid.domain.fodot.structure;

import java.util.Collection;

import playid.domain.fodot.file.FodotFileElementWithNamedElements;
import playid.domain.fodot.file.IFodotFileElement;
import playid.domain.fodot.structure.elements.IFodotStructureElement;
import playid.domain.fodot.vocabulary.FodotVocabulary;

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
