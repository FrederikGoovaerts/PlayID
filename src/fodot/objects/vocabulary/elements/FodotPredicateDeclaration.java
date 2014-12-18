package fodot.objects.vocabulary.elements;

import java.util.List;

import fodot.util.CollectionUtil;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotPredicateDeclaration extends FodotArgumentListDeclaration implements IFodotDomainElement {
    
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
