package fodot.objects.vocabulary;

import java.util.Collection;
import java.util.Set;

import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;

public class FodotLTCVocabulary extends FodotVocabulary {
	@Deprecated
	public FodotLTCVocabulary(String name, Set<FodotTypeDeclaration> types, Set<FodotPredicateDeclaration> predicates,
			Set<FodotFunctionDeclaration> functions) {
		super(name, types, predicates, functions);
	}
	
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
