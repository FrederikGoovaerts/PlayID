package fodot.objects.vocabulary;

import java.util.Collection;

import fodot.objects.vocabulary.elements.IFodotVocabularyElement;

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
}
