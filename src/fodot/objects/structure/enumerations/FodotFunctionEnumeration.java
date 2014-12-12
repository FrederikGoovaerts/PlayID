package fodot.objects.structure.enumerations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.util.CollectionUtil;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;

public class FodotFunctionEnumeration extends FodotEnumeration {
	public FodotFunctionDeclaration function;
	public Map<FodotConstant[], FodotConstant> values;
	
	public FodotFunctionEnumeration(FodotFunctionDeclaration function) {
		this(function, new HashMap<FodotConstant[], FodotConstant>());
	}
	
	public FodotFunctionEnumeration(FodotFunctionDeclaration function, Map<FodotConstant[], FodotConstant> values) {
		super();
		this.function = function;
		this.values = values;
	}

	public FodotFunctionDeclaration getFunctionType() {
		return function;
	}

	/* VALUES */
	
	public void addValue(FodotConstant[] input, FodotConstant value) {
		//TODO: some kind of validator for the amount of and type of arguments
		values.put(input, value);
	}
	
	public void removeValue(FodotConstant[] input) {
		values.remove(input);
	}
	
	public boolean containsValue(FodotConstant[] input) {
		return values.containsKey(input);
	}
	
	public Map<FodotConstant[], FodotConstant> getValues() {
		return new HashMap<FodotConstant[], FodotConstant>(values);
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(function.getName() + " = {");
		for (FodotConstant[] key : getValues().keySet()) {
			builder.append(CollectionUtil.toCoupleAsCode(Arrays.asList(key)) + " -> " + values.get(key) + "; \n");
		}
		builder.append("}");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		FodotFunctionEnumeration other = (FodotFunctionEnumeration) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FodotFunctionEnumeration [function=" + function + ", values="
				+ values + "]";
	}
	
	
		
}