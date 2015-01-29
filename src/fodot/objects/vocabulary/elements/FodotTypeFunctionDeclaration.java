package fodot.objects.vocabulary.elements;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.util.CollectionPrinter;

public class FodotTypeFunctionDeclaration extends FodotFunctionDeclaration implements IFodotDomainElement {
	
	public FodotTypeFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType elementOfType) {
		super(name, argumentTypes, elementOfType);
	}

	@Override
	protected void setType(FodotType type) {
		super.setType(type);
		getType().addDomainElement(this);
	}



	@Override
	public String toCode() {
		return super.toCode();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
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
		FodotTypeFunctionDeclaration other = (FodotTypeFunctionDeclaration) obj;
		if (getType() == null) {
			if (other.getType() != null)
				return false;
		} else if (!getType().equals(other.getType()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[FodotTypeFunctionDeclaration "+toCode()+"]";
	}


	@Override
	public Set<FodotType> getRequiredTypes() {
		Set<FodotType> result = new HashSet<FodotType>();
		//Check all arguments
		for (FodotType arg : getArgumentTypes()) {
			result.addAll(arg.getPrerequiredTypes());
		}
		result.addAll(getArgumentTypes());
		
		return result;
	}
	
	

	
	
}
