package fodot.objects.vocabulary.elements;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.util.CollectionPrinter;

public class FodotPredicateTermDeclaration extends
		FodotArgumentListDeclaration implements IFodotDomainElement {

	private FodotType type;
	
	public FodotPredicateTermDeclaration(String name, List<FodotType> argumentTypes, FodotType elementOfType) {
		super(name, argumentTypes);
		setType(elementOfType);
	}

	
	public FodotType getType() {
		return type;
	}



	private void setType(FodotType type) {
//		if (this.type != null) {
//			this.type.removeDomainElement(this);
//		}
		this.type = type;
		this.type.addDomainElement(this);
	}



	@Override
	public String toCode() {
		return getName() + CollectionPrinter.toCoupleAsCode(getArgumentTypes());
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FodotPredicateTermDeclaration other = (FodotPredicateTermDeclaration) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (getArgumentTypes() == null) {
			if (other.getArgumentTypes()  != null)
				return false;
		} else if (!getArgumentTypes() .equals(other.getArgumentTypes() ))
			return false;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "[FodotPredicateTermDeclaration "+toCode()+"]";
	}


	@Override
	public Set<FodotType> getRequiredTypes() {
		Set<FodotType> result = new HashSet<FodotType>();
		//Check all arguments
		for (FodotType arg : getArgumentTypes()) {
			result.addAll(arg.getPrerequiredTypes());
		}
		//
		result.addAll(getArgumentTypes());
		
		return result;
	}
	
	

	
	
}
