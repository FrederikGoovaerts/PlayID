package fodot.objects.vocabulary.elements;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.general.IFodotElement;
import fodot.util.CollectionPrinter;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotFunctionDeclaration extends FodotArgumentListDeclaration implements IFodotVocabularyElement {

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
	
	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		Set<IFodotElement> superElements = new HashSet<IFodotElement>(super.getDirectFodotElements());
		superElements.add(returnType);
		return superElements;
	}
	
	public boolean isPartial() {
		return isPartial;
	}

	@Override
	public int getArity() {
		return super.getArity() + 1; //Return type also counts
	}
	
	@Override
	public String toCode() {
		return (isPartial() ? "partial " : "") + getName()
				+ (hasArguments() ? CollectionPrinter.toCoupleAsCode(getArgumentTypes()) : "")
				+ " : " + getReturnType().getName();
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

	@Override
	public String toString() {
		return "FodotFunctionDeclaration [returnType=" + returnType
				+ ", isPartial=" + isPartial + ", getName()=" + getName()
				+ ", getArgumentTypes()=" + getArgumentTypes() + "]";
	}
	
	
    
}
