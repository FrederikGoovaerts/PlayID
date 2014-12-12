package fodot.objects.structure.enumerations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fodot.objects.util.CollectionUtil;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;

public class FodotPredicateEnumeration extends FodotEnumeration {
	public FodotPredicateDeclaration predicate;
	public List<String[]> values;
	
	public FodotPredicateEnumeration(FodotPredicateDeclaration predicate) {
		this(predicate, new ArrayList<String[]>());
	}
	
	public FodotPredicateEnumeration(FodotPredicateDeclaration predicate, List<String[]> values) {
		super();
		this.predicate = predicate;
		this.values = values;
	}

	public FodotPredicateDeclaration getPredicateType() {
		return predicate;
	}

	/* VALUES */
	
	public void addValue(String[] value) {
		//TODO: some kind of validator for the amount of and type of arguments
		values.add(value);
	}
	
	public void removeValue(String[] value) {
		values.remove(value);
	}
	
	public boolean containsValue(String[] value) {
		return values.contains(value);
	}
	
	public List<String[]> getValues() {
		return new ArrayList<String[]>(values);
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(predicate.getName() + " = {");
		for (String[] strings : getValues()) {
			builder.append(CollectionUtil.toCouple(Arrays.asList(strings)) + ";\n");
		}
		builder.append("}");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
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
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}
		
}
