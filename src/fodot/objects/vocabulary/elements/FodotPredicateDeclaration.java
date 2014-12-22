package fodot.objects.vocabulary.elements;

import java.util.List;

import fodot.util.CollectionUtil;
	
public class FodotPredicateDeclaration extends FodotArgumentListDeclaration implements IFodotVocabularyElement {
    public FodotPredicateDeclaration(String predicateName, List<FodotType> argumentTypes) {
        super(predicateName, argumentTypes);
    }

	@Override
	public String toCode() {
		return getName() + CollectionUtil.toCoupleAsCode(getArgumentTypes());
	}
    
}
