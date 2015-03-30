package playid.domain.fodot.vocabulary.elements;

import java.util.List;

import playid.util.CollectionPrinter;
	
public class FodotPredicateDeclaration extends FodotArgumentListDeclaration implements IFodotVocabularyElement {
    public FodotPredicateDeclaration(String predicateName, List<FodotType> argumentTypes) {
        super(predicateName, argumentTypes);
    }

	@Override
	public String toCode() {
		return getName() + CollectionPrinter.toCoupleAsCode(getArgumentTypes());
	}
    
	@Override
	public String toString() {
		return "[ FodotPredicateDeclaration: "+ toCode() +"]";
	}
}
