package fodot.objects.structure.enumerations;

import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;

public class FodotConstantFunctionEnumeration extends FodotEnumeration {
	private FodotFunctionDeclaration declaration;
	private IFodotEnumerationElement value;

	public FodotConstantFunctionEnumeration(FodotFunctionDeclaration declaration, IFodotEnumerationElement value) {
		super();
		this.declaration = declaration;
		this.value = value;
	}

	public FodotFunctionDeclaration getDeclaration() {
		return declaration;
	}

	/* VALUE */
	
	public void setValue(IFodotEnumerationElement value) {
		this.value = value;
	}
	
	public IFodotEnumerationElement getValue() {
		return value;
	}

	@Override
	public String toCode() {
		return declaration.getName() + " = " + value.getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((declaration == null) ? 0 : declaration.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		FodotConstantFunctionEnumeration other = (FodotConstantFunctionEnumeration) obj;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FodotConstantFunctionEnumeration [function=" + declaration + ", value="
				+ value + "]";
	}
	
	
		
}