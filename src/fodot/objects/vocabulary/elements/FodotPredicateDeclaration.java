package fodot.objects.vocabulary.elements;

import java.util.List;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotPredicateDeclaration extends FodotArgumentListDeclaration {
    
    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotPredicateDeclaration(String predicateName, List<FodotType> argumentTypes) {
        super(predicateName, argumentTypes);
    }
    
}
