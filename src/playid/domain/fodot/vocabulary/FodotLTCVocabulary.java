package playid.domain.fodot.vocabulary;

import java.util.Collection;

import playid.domain.fodot.vocabulary.elements.IFodotVocabularyElement;

public class FodotLTCVocabulary extends FodotVocabulary {
	
	public FodotLTCVocabulary(String name, Collection<? extends IFodotVocabularyElement> elements) {
		super(name, elements);
	}
	
	public FodotLTCVocabulary() {
		this(null, null);
	}

	@Override
	public String toCode() {
		return "LTC" + super.toCode();
	}
	
	@Override
	public String toString() {
		return "[LTCFodotVocabulary: " + toCode() + "]";
	}
}
