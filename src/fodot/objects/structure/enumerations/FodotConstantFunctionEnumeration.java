package fodot.objects.structure.enumerations;

import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.util.CollectionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FodotConstantFunctionEnumeration extends FodotEnumeration {
	public FodotFunctionDeclaration function;
	public FodotConstant value;

	public FodotConstantFunctionEnumeration(FodotFunctionDeclaration function, FodotConstant value) {
		super();
		this.function = function;
		this.value = value;
	}

	public FodotFunctionDeclaration getFunctionType() {
		return function;
	}

	/* VALUE */
	
	public void setValue(FodotConstant value) {
		this.value = value;
	}
	
	public FodotConstant getValue() {
		return value;
	}

	@Override
	public String toCode() {
		return function.getName() + " = " + value.getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
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
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
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
		return "FodotConstantFunctionEnumeration [function=" + function + ", value="
				+ value + "]";
	}
	
	
		
}