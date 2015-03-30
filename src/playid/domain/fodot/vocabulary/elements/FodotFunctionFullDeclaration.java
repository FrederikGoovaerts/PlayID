package playid.domain.fodot.vocabulary.elements;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import playid.domain.fodot.general.IFodotElement;

public class FodotFunctionFullDeclaration extends FodotFunctionDeclaration implements IFodotVocabularyElement {

	private final boolean isPartial;

	public FodotFunctionFullDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType, boolean isPartial) {
		super(name, argumentTypes, returnType);
		this.isPartial = isPartial;
	}

	public FodotFunctionFullDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		this(name, argumentTypes, returnType, false);
	}

	public FodotType getReturnType() {
		return getType();
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		Set<IFodotElement> superElements = new HashSet<IFodotElement>(getDirectFodotElements());
		superElements.add(getReturnType());
		return superElements;
	}

	public boolean isPartial() {
		return isPartial;
	}

	@Override
	public int getArity() {
		return super.getArity() + 1; //Return type also counts
	}


	/**********************************************
	 *  Obligatory methods
	 ***********************************************/	
	@Override
	 public String toCode() {
		 return (isPartial() ? "partial " : "")
				 + super.toCode()
				 + " : " + getReturnType().getName();
	 }

	 @Override
	 public int hashCode() {
		 final int prime = 31;
		 int result = super.hashCode();
		 result = prime * result + (isPartial ? 1231 : 1237);
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
		 FodotFunctionFullDeclaration other = (FodotFunctionFullDeclaration) obj;
		 if (isPartial != other.isPartial)
			 return false;
		 return true;
	 }

	 @Override
	 public String toString() {
		 return "FodotFunctionDeclaration [returnType=" + getReturnType()
				 + ", isPartial=" + isPartial + ", getName()=" + getName()
				 + ", getArgumentTypes()=" + getArgumentTypes() + "]";
	 }	

	 /**********************************************/



}
