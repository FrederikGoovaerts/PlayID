package fodot.objects.vocabulary.elements;

import java.util.ArrayList;
import java.util.List;

public abstract class FodotArgumentListDeclaration {

	/***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotArgumentListDeclaration(String name, List<FodotType> argumentTypes) {
        this.name = name;
        this.argumentTypes = argumentTypes;
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private final String name;

    public String getName() {
        return name;
    }

    /*************************************
     * Argument types
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
