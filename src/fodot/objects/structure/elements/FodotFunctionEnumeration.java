package fodot.objects.structure.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.util.CollectionPrinter;

public class FodotFunctionEnumeration extends FodotEnumeration {
	private FodotFunctionDeclaration declaration;
	private Map<IFodotEnumerationElement[], IFodotEnumerationElement> values;

	public FodotFunctionEnumeration(FodotFunctionDeclaration function) {
		this(function, new HashMap<IFodotEnumerationElement[], IFodotEnumerationElement>());
	}

	public FodotFunctionEnumeration(FodotFunctionDeclaration declaraion, Map<IFodotEnumerationElement[], IFodotEnumerationElement> values) {
		super();
		this.declaration = declaraion;
		this.values = values;
	}

	public FodotFunctionDeclaration getDeclaration() {
		return declaration;
	}

	/* VALUES */

	public void addValue(IFodotEnumerationElement[] input, IFodotEnumerationElement value) {
		//TODO: some kind of validator for the amount of and type of arguments
		values.put(input, value);
	}

	public void removeValue(IFodotEnumerationElement[] input) {
		values.remove(input);
	}

	public boolean containsValue(IFodotEnumerationElement[] input) {
		return values.containsKey(input);
	}

	public Map<IFodotEnumerationElement[], IFodotEnumerationElement> getValues() {
		return new HashMap<IFodotEnumerationElement[], IFodotEnumerationElement>(values);
	}

	public boolean hasValues() {
		return !values.isEmpty();
	}

	//SENTENCE ELEMENT

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(declaration.getName() + " = {");
		List<IFodotEnumerationElement[]> keys = new ArrayList<IFodotEnumerationElement[]>(getValues().keySet());
		for (int i = 0; i < keys.size(); i++) {
			if (i > 0) {
				builder.append(";");
			}
			IFodotEnumerationElement[] key = keys.get(i);
			builder.append(CollectionPrinter.toNakedList(CollectionPrinter.toCode(Arrays.asList(key))) + " -> " + values.get(key).toCode());
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