package fodot.objects.vocabulary.elements;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.IFodotElement;
import fodot.objects.exceptions.InvalidTermNameException;
import fodot.objects.util.TermsUtil;

public abstract class FodotArgumentListDeclaration implements IFodotElement {

	/***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotArgumentListDeclaration(String name, List<FodotType> argumentTypes) {
       	setName(name);
        this.argumentTypes = argumentTypes;
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private String name;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
		if (!TermsUtil.isValidName(name)) {
			throw new InvalidTermNameException(name);
		}
		this.name = name;
    }

    /*************************************
     * Argument types
     */

    private List<FodotType> argumentTypes;

    public List<FodotType> getArgumentTypes(){
        return new ArrayList<>(argumentTypes);
    }

    public int getAmountOfArgumentTypes(){
        return argumentTypes.size();
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
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((argumentTypes == null) ? 0 : argumentTypes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FodotArgumentListDeclaration other = (FodotArgumentListDeclaration) obj;
		if (argumentTypes == null) {
			if (other.argumentTypes != null)
				return false;
		} else if (!argumentTypes.equals(other.argumentTypes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
    
	
}
