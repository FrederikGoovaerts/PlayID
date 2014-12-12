package fodot.objects.structure.enumerations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.util.CollectionUtil;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;

public class FodotPredicateEnumeration extends FodotEnumeration {
	public FodotPredicateDeclaration predicate;
	public List<FodotConstant[]> values;
	
	public FodotPredicateEnumeration(FodotPredicateDeclaration predicate) {
		this(predicate, new ArrayList<FodotConstant[]>());
	}
	
	public FodotPredicateEnumeration(FodotPredicateDeclaration predicate, List<FodotConstant[]> values) {
		super();
		this.predicate = predicate;
		this.values = values;
	}

	public FodotPredicateDeclaration getPredicateType() {
		return predicate;
	}

	/* VALUES */
	
	public void addValue(FodotConstant[] value) {
		//TODO: some kind of validator for the amount of and type of arguments
		values.add(value);
	}
	
	public void removeValue(FodotConstant[] value) {
		values.remove(value);
	}
	
	public boolean containsValue(FodotConstant[] value) {
		return values.contains(value);
	}
	
	public List<FodotConstant[]> getValues() {
		return new ArrayList<FodotConstant[]>(values);
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(predicate.getName() + " = {");
		for (FodotConstant[] constants : getValues()) {		
			builder.append(CollectionUtil.toNakedList(CollectionUtil.toCode(Arrays.asList(constants))) + ";\n");
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

	@Override
	public String toString() {
		return "FodotPredicateEnumeration [predicate=" + predicate
				+ ", values=" + values + "]";
	}
		
}
