package fodot.objects.vocabulary.elements;

import java.util.List;

import fodot.util.CollectionUtil;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotFunctionDeclaration extends FodotArgumentListDeclaration {

	private final FodotType returnType;
	private final boolean isPartial;
	
	public FodotFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType, boolean isPartial) {
		super(name, argumentTypes);
		this.returnType = returnType;
		this.isPartial = isPartial;
	}

	public FodotFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		this(name, argumentTypes, returnType, false);
	}
	
	public FodotType getReturnType() {
		return returnType;
	}
	
	public boolean isPartial() {
		return isPartial;
	}

	@Override
	public String toCode() {
		return (isPartial() ? "partial " : "") + getName() +
				CollectionUtil.toCoupleAsCode(getArgumentTypes()) + " : " + getReturnType().getName();
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
