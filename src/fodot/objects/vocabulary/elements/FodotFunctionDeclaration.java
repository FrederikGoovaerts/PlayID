package fodot.objects.vocabulary.elements;

import java.util.List;

import fodot.objects.util.CollectionUtil;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotFunctionDeclaration extends FodotArgumentListDeclaration {

	private final FodotType returnType;
	
	public FodotFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		super(name, argumentTypes);
		this.returnType = returnType;
	}

	public FodotType getReturnType() {
		return returnType;
	}

	@Override
	public String toCode() {
		return getName() + CollectionUtil.toCoupleAsCode(getArgumentTypes()) + " : " + getReturnType().getTypeName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((returnType == null) ? 0 : returnType.hashCode());
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
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}
	
	
    
}
