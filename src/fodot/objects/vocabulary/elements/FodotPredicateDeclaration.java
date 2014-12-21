package fodot.objects.vocabulary.elements;

import java.util.List;
import java.util.Set;

import fodot.util.CollectionUtil;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotPredicateDeclaration extends FodotArgumentListDeclaration implements IFodotVocabularyElement {
    
    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotPredicateDeclaration(String predicateName, List<FodotType> argumentTypes) {
        super(predicateName, argumentTypes);
    }

	@Override
	public String toCode() {
		return getName() + CollectionUtil.toCoupleAsCode(getArgumentTypes());
	}
    
}
