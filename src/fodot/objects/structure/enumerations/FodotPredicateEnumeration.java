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
		
}
