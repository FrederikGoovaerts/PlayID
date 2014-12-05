package fodot.objects.vocabulary;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.formulas.FodotPredicate;
import fodot.objects.terms.FodotFunction;
import fodot.objects.type.FodotType;

public class FodotVocabulary {
	private Set<FodotType> types;
	private Set<FodotPredicate> predicates;
	private Set<FodotFunction> functions;
	
	public FodotVocabulary() {
		this(new HashSet<FodotType>(), new HashSet<FodotPredicate>(), new HashSet<FodotFunction>());
	}
	
	public FodotVocabulary(Set<FodotType> types, Set<FodotPredicate> predicates,
			Set<FodotFunction> functions) {
		this.types = types;
		this.predicates = predicates;
		this.functions = functions;
	}
	
	/* TYPES */
	
	public void addType(FodotType type) {
		if (containsType(type))
			return;
		if (containsTypeWithName(type.getTypeName()))
			throw new RuntimeException("Vocabulary " + this + " already contains a type with name " + type.getTypeName());
		types.add(type);
	}
	
	public void removeType(FodotType type) {
		types.remove(type);
	}
	
	public boolean containsTypeWithName(String name) {
		for (FodotType type : types) {
			if (type.getTypeName() == name) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsType(FodotType type) {
		return types.contains(type);
	}
	
	public Set<FodotType> getTypes() {
		return new HashSet<FodotType>(types);
	}

	/* PREDICATES */
	
	public void addPredicate(FodotPredicate predicate) {
		predicates.add(predicate);
	}
	
	public void removePredicate(FodotPredicate predicate) {
		predicates.remove(predicate);
	}
	
	public Set<FodotPredicate> getPredicate() {
		return new HashSet<FodotPredicate>(predicates);
	}
	
	
	/* FUNCTIONS */
	
	public void addFunction(FodotFunction function) {
		functions.add(function);
	}
	
	public void removeFunction(FodotFunction function) {
		functions.remove(function);
	}
	
	public Set<FodotFunction> getFunction() {
		return new HashSet<FodotFunction>(functions);
	}
}
