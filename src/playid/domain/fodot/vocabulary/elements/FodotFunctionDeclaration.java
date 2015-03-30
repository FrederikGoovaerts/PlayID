package playid.domain.fodot.vocabulary.elements;

import java.util.List;

import playid.util.CollectionPrinter;

public abstract class FodotFunctionDeclaration extends FodotArgumentListDeclaration {
	
	private FodotType type;
	
	public FodotFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		super(name, argumentTypes);
		setType(returnType);
	}

	/**********************************************
	 *  Type
	 ***********************************************/
	public FodotType getType() {
		return type;
	}

	protected void setType(FodotType type) {
		this.type = type;
	}
	/**********************************************/

	
	/**********************************************
	 *  Obligatory
	 ***********************************************/
	@Override
	public String toCode() {
		return getName() + (hasArguments() ? CollectionPrinter.toCoupleAsCode(getArgumentTypes()) : "");
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
		FodotFunctionDeclaration other = (FodotFunctionDeclaration) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "[FodotFunctionDeclaration "+toCode()+"]";
	}
	/**********************************************/
}
