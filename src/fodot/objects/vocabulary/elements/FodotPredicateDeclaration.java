package fodot.objects.vocabulary.elements;

import java.util.List;

import org.apache.batik.script.rhino.RhinoInterpreter.ArgumentsBuilder;

import fodot.objects.util.CollectionUtil;

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

	@Override
	public String toCode() {
		return getName() + CollectionUtil.toCoupleAsCode(getArgumentTypes());
	}
    
}
