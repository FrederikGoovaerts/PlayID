package fodot.objects.structure.elements.functionenum;

import java.util.Arrays;
import java.util.Collection;

import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.structure.elements.IFodotStructureElement;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.vocabulary.elements.FodotFunctionFullDeclaration;

public class FodotConstantFunctionEnumeration extends FodotElement implements IFodotStructureElement {
	private FodotFunctionFullDeclaration declaration;
	private IFodotTypeEnumerationElement value;

	public FodotConstantFunctionEnumeration(FodotFunctionFullDeclaration declaration, IFodotTypeEnumerationElement value) {
		super();
		this.declaration = declaration;
		this.value = value;
	}

	public FodotFunctionFullDeclaration getDeclaration() {
		return declaration;
	}

	/* VALUE */
	
	public void setValue(IFodotTypeEnumerationElement value) {
		this.value = value;
	}
	
	public IFodotTypeEnumerationElement getValue() {
		return value;
	}

	@Override
	public String toCode() {
		return getName() + " = " + value.getValue();
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

	@Override
	public String getName() {
		return getDeclaration().getName();
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return Arrays.asList(value);
	}
	
	
		
}