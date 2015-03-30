package playid.domain.fodot.vocabulary.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import playid.domain.exceptions.fodot.InvalidTermNameException;
import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;
import playid.util.NameUtil;

public abstract class FodotArgumentListDeclaration extends FodotElement implements IFodotElement {

	/***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotArgumentListDeclaration(String name, List<FodotType> argumentTypes) {
       	setName(name);
		setArgumentTypes(argumentTypes);
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private String name;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
		if (!NameUtil.isValidName(name)) {
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

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return getArgumentTypes();
	}
    
    public int getAmountOfArgumentTypes(){
        return argumentTypes.size();
    }
    
    public int getArity() {
    	return getAmountOfArgumentTypes();
    }

	private void setArgumentTypes(List<FodotType> argumentTypes) {
		if(argumentTypes == null){
			this.argumentTypes = new ArrayList<FodotType>();
		} else {
			this.argumentTypes = argumentTypes;
		}
	}

    public FodotType getArgumentType(int i){
        if(i<0 || i>=argumentTypes.size())
            throw new IllegalArgumentException("Invalid index.");
        return argumentTypes.get(i);
    }

    public void setArgumentType(int i, FodotType t){
        this.argumentTypes.set(i, t);
    }
    
    public boolean hasArguments() {
    	return !argumentTypes.isEmpty();
    }

    /************************************/


	public Set<FodotType> getPrerequiredTypes() {
		return new LinkedHashSet<FodotType>(getArgumentTypes());
	}
	
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
