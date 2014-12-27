package fodot.objects.structure.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.util.CollectionPrinter;

public class FodotPredicateEnumeration extends FodotEnumeration {
	private FodotPredicateDeclaration declaration;
	private List<IFodotEnumerationElement[]> values;
	
	public FodotPredicateEnumeration(FodotPredicateDeclaration predicate) {
		this(predicate, new ArrayList<IFodotEnumerationElement[]>());
	}
	
	public FodotPredicateEnumeration(FodotPredicateDeclaration declaration, List<IFodotEnumerationElement[]> values) {
		super();
		this.declaration = declaration;
		this.values = values;
	}

	public FodotPredicateDeclaration getDeclaration() {
		return declaration;
	}

	/* VALUES */
	
	public void addValue(IFodotEnumerationElement[] value) {
		//TODO: some kind of validator for the amount of and type of arguments
		values.add(value);
	}
	
	public void removeValue(IFodotEnumerationElement[] value) {
		values.remove(value);
	}
	
	public boolean containsValue(IFodotEnumerationElement[] value) {
		return values.contains(value);
	}
	
	public List<IFodotEnumerationElement[]> getValues() {
		return new ArrayList<IFodotEnumerationElement[]>(values);
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(declaration.getName() + " = {");
		List<IFodotEnumerationElement[]> keys = new ArrayList<IFodotEnumerationElement[]>(getValues());
		for (int i = 0; i < keys.size(); i++) {
			if (i > 0) {
				builder.append(";");
			}
			builder.append(CollectionPrinter.toNakedList(CollectionPrinter.toCode(Arrays.asList(keys.get(i)))));
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
		FodotPredicateEnumeration other = (FodotPredicateEnumeration) obj;
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
		return "FodotPredicateEnumeration [predicate=" + declaration
				+ ", values=" + values + "]";
	}
		
}
