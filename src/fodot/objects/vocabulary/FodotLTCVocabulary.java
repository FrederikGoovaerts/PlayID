package fodot.objects.vocabulary;

import java.util.Set;

import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;

public class FodotLTCVocabulary extends FodotVocabulary {
	public FodotLTCVocabulary(String name, Set<FodotTypeDeclaration> types, Set<FodotPredicateDeclaration> predicates,
			Set<FodotFunctionDeclaration> functions) {
		super(name, types, predicates, functions);
	}
	
	@Override
	public String toCode() {
		return "LTC" + super.toCode();
	}
}
