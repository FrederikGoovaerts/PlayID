package fodot.objects.formulas;

import fodot.objects.type.FodotType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotPredicate extends FodotFormula {
    
    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotPredicate(String predicateName, List<FodotType> argumentTypes) {
        this.predicateName = predicateName;
        this.argumentTypes = argumentTypes;
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private final String predicateName;

    public String getPredicateName() {
        return predicateName;
    }

    /*************************************
     * Predicate types
     */

    private List<FodotType> argumentTypes;

    public List<FodotType> getArgumentTypes(){
        return new ArrayList<>(argumentTypes);
    }

    public FodotType getArgumentType(int i){
        if(i<0 || i>=argumentTypes.size())
            throw new IllegalArgumentException("Invalid index.");
        return argumentTypes.get(i);
    }

    public void setArgumentType(int i, FodotType t){
        if(!getArgumentType(i).equals(FodotType.getPlaceHolderType()))
            throw new IllegalArgumentException("Can't replace a legal type!");
        this.argumentTypes.set(i, t);
    }

    /************************************/



}
