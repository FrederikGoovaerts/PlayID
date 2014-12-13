package fodot.objects.vocabulary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.util.NameUtil;

public class FodotVocabulary implements IFodotElement {
	//Maybe convert the three sets in just a set of FodotDeclarations?
	private String name;
	private Set<FodotTypeDeclaration> types;
	private Set<FodotPredicateDeclaration> predicates;
	private Set<FodotFunctionDeclaration> functions;

	public FodotVocabulary(String name, Set<FodotTypeDeclaration> types, Set<FodotPredicateDeclaration> predicates,
			Set<FodotFunctionDeclaration> functions) {
		setName(name);
		setTypes(types);
		setPredicates(predicates);
		setFunctions(functions);
	}

	public FodotVocabulary(String name) {
		this(name, null, null, null);
	}

	private static final String DEFAULT_NAME = "V";

	public FodotVocabulary() {
		this(null);
	}

	/* TYPES */
	private void setTypes(Set<FodotTypeDeclaration> types) {
		if (types == null) {
			this.types = new HashSet<FodotTypeDeclaration>();
		} else {
			this.types = types;
		}
	}

	public void addType(FodotTypeDeclaration type) {
		if (containsType(type))
			return;
		if (containsTypeWithName(type.getType().getTypeName()))
			throw new RuntimeException("Vocabulary " + this + " already contains a type declaration with name " + type.getType().getTypeName());
		types.add(type);
	}

	public void removeType(FodotTypeDeclaration type) {
		types.remove(type);
	}

	public boolean containsTypeWithName(String name) {
		for (FodotTypeDeclaration type : types) {
			if (type.getType().getTypeName() == name) {
				return true;
			}
		}
		return false;
	}

	public boolean containsType(FodotTypeDeclaration type) {
		return types.contains(type);
	}

	public Set<FodotTypeDeclaration> getTypes() {
		return new HashSet<FodotTypeDeclaration>(types);
	}

	/* PREDICATES */

	private void setPredicates(Set<FodotPredicateDeclaration> predicates) {
		if (predicates == null) {
			this.predicates = new HashSet<FodotPredicateDeclaration>();
		} else {
			this.predicates = predicates;
		}
	}

	public void addPredicate(FodotPredicateDeclaration predicate) {
		predicates.add(predicate);
	}

	public void removePredicate(FodotPredicateDeclaration predicate) {
		predicates.remove(predicate);
	}

	public Set<FodotPredicateDeclaration> getPredicates() {
		return new HashSet<FodotPredicateDeclaration>(predicates);
	}


	/* FUNCTIONS */

	private void setFunctions(Set<FodotFunctionDeclaration> functions) {
		if (functions == null) {
			this.functions = new HashSet<FodotFunctionDeclaration>();
		} else {
			this.functions = functions;
		}
	}

	public void addFunction(FodotFunctionDeclaration function) {
		functions.add(function);
	}

	public void removeFunction(FodotFunctionDeclaration function) {
		functions.remove(function);
	}

	public Set<FodotFunctionDeclaration> getFunctions() {
		return new HashSet<FodotFunctionDeclaration>(functions);
	}

	/* NAMES */

	public String getName() {
		return name;
	}

	private void setName(String name) {
		if (NameUtil.isValidName(name)) {
			this.name = name;
		} else {
			this.name = DEFAULT_NAME;
		}


	}

	/* FODOT ELEMENT */
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("vocabulary " + getName() + " {\n");

		List<IFodotElement> toStringify = new ArrayList<IFodotElement>();
		toStringify.addAll(getTypes());
		toStringify.addAll(getFunctions());
		toStringify.addAll(getPredicates());

		//Codify inductive definitions
		for (IFodotElement el : toStringify) {
			builder.append(el.toCode() + "\n");
		}

		builder.append("}");
		return builder.toString();
	}

	/* MERGE */

	public void merge(FodotVocabulary other) {
		types.addAll(other.getTypes());
		predicates.addAll(getPredicates());
		functions.addAll(getFunctions());
	}
}
