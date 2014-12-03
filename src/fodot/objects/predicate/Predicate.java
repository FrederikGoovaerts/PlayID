package fodot.objects.predicate;

import fodot.objects.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class Predicate {
    
    /***************************************************************************
     * Constructor
     **************************************************************************/

    public Predicate(String predicateName, List<Type> argumentTypes) {
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

    private List<Type> argumentTypes;

    public List<Type> getArgumentTypes(){
        return new ArrayList<>(argumentTypes);
    }

    public Type getArgumentType(int i){
        if(i<0 || i>=argumentTypes.size())
            throw new IllegalArgumentException("Invalid index.");
        return argumentTypes.get(i);
    }

    public void setArgumentType(int i, Type t){
        if(!getArgumentType(i).equals(Type.getPlaceHolderType()))
            throw new IllegalArgumentException("Can't replace a legal type!");
        this.argumentTypes.set(i, t);
    }

    /************************************/



}
