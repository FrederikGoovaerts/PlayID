package fodot.objects.structure.enumerations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.util.CollectionUtil;

public class FodotFunctionEnumeration extends FodotEnumeration {
	private FodotFunctionDeclaration declaration;
	private Map<FodotConstant[], FodotConstant> values;

	public FodotFunctionEnumeration(FodotFunctionDeclaration function) {
		this(function, new HashMap<FodotConstant[], FodotConstant>());
	}

	public FodotFunctionEnumeration(FodotFunctionDeclaration declaraion, Map<FodotConstant[], FodotConstant> values) {
		super();
		this.declaration = declaraion;
		this.values = values;
	}

	public FodotFunctionDeclaration getDeclaration() {
		return declaration;
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

	public boolean hasValues() {
		return !values.isEmpty();
	}

	//SENTENCE ELEMENT

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(declaration.getName() + " = {");
		List<FodotConstant[]> keys = new ArrayList<FodotConstant[]>(getValues().keySet());
		for (int i = 0; i < keys.size(); i++) {
			if (i > 0) {
				builder.append(";");
			}
			FodotConstant[] key = keys.get(i);
			builder.append(CollectionUtil.toCoupleAsCode(Arrays.asList(key)) + " -> " + values.get(key).toCode());
		}
		builder.append("}");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((declaration == null) ? 0 : declaration.hashCode());
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
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
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
		return "FodotFunctionEnumeration [function=" + declaration + ", values="
				+ values + "]";
	}



}