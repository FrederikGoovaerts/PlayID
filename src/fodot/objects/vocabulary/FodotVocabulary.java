package fodot.objects.vocabulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.util.CollectionUtil;
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
		if (containsTypeWithName(type.getType().getName()))
			throw new RuntimeException("Vocabulary " + this + " already contains a type declaration with name " + type.getType().getName());
		types.add(type);
	}

	public void removeType(FodotTypeDeclaration type) {
		types.remove(type);
	}

	public boolean containsTypeWithName(String name) {
		for (FodotTypeDeclaration type : types) {
			if (type.getType().getName() == name) {
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

		//TO CODE TYPES: IN THE RIGHT ORDER
		TypeDeclarationCodifier typeDeclCod = new TypeDeclarationCodifier(getTypes());
		builder.append(typeDeclCod.createDeclarationBlock());
//		LinkedList<FodotTypeDeclaration> allTypes = new LinkedList<FodotTypeDeclaration>(getTypes());
//		Set<FodotType> printedTypes = new HashSet<FodotType>();
//		while (!allTypes.isEmpty()) {
//			FodotTypeDeclaration current = allTypes.poll();
//			FodotType currentType = current.getType();
//			if (printedTypes.containsAll(currentType.getPrerequisiteTypes())) {
//				builder.append(current.toCode() + "\n");
//				printedTypes.add(currentType);
//			} else {
//				allTypes.add(current);
//			}
//		}		
		
		
		//STRINGIFY FUNCTIONS&PREDICATES
		List<IFodotElement> toStringify = new ArrayList<IFodotElement>();
		toStringify.addAll(getFunctions());
		toStringify.addAll(getPredicates());

		//Codify inductive definitions
		for (IFodotElement el : toStringify) {
			builder.append(el.toCode() + "\n");
		}

		builder.append("}");
		return builder.toString();
	}
 
	private class TypeDeclarationCodifier {
		private List<FodotTypeDeclaration> toPrint;
		private Set<FodotTypeDeclaration> alreadyTriedThisRound = new HashSet<FodotTypeDeclaration>();
		private StringBuilder builder = new StringBuilder();
		//Natural number and int does not have to be printed: add them as "printed"
		private Set<FodotType> printed = new HashSet<FodotType>(Arrays.asList(FodotType.INTEGER, FodotType.NATURAL_NUMBER));
				
		public TypeDeclarationCodifier(Collection<? extends FodotTypeDeclaration> types) {
			this.toPrint = new LinkedList<FodotTypeDeclaration>(types);
		}
		
		
		public String createDeclarationBlock() {
			while (!toPrint.isEmpty()) {
				tryPrinting(toPrint.get(0));
			}		
			
			return builder.toString();
		}
		
		private void tryPrinting(FodotTypeDeclaration current) {
			FodotType currentType = current.getType();
			/*Check if there's a loop like:
			 * type A constructed from {u(B)}
			 * type B constructed from {v(A)}
			 */
			if (alreadyTriedThisRound.contains(current)) {
				throw new IllegalStateException(
						"A loop has been detected in the order in which TypeDeclarations should be printed: \n"
						+ CollectionUtil.toCoupleAsCode(alreadyTriedThisRound));
			}
			
			//Check if printable
			if (printed.containsAll(currentType.getPrerequisiteTypes())) {
				print(current);
			}
			else {
				alreadyTriedThisRound.add(current);
				//Try printing the next type that printed does not contain
				tryPrinting(getFirstNotPrinted(currentType));
			}
		}
		
		private FodotTypeDeclaration getFirstNotPrinted(FodotType type) {
			Iterator<FodotType> it = type.getPrerequisiteTypes().iterator();
			FodotType current = it.next();
			while (printed.contains(current) && it.hasNext()) {
				current = it.next();
			}
			FodotTypeDeclaration decl = current.getDeclaration();
			
			//Check for errors
			if (printed.contains(current)) {
				throw new IllegalStateException("Something has gone wrong in the typedeclaration block with " + current);
			}
			if (!toPrint.contains(decl)) {
				throw new IllegalStateException("A type that wasn't declared is needed to be printed: " + decl);
			}
			
			//return
			return decl;
		}


		private void print(FodotTypeDeclaration decl) {
			builder.append(decl.toCode() + "\n");
			
			//Add to printed, remove from toPrint
			toPrint.remove(decl);
			printed.add(decl.getType());
			
			//New round!
			alreadyTriedThisRound.clear();
		}

	}
	
	/* MERGE */

	public void merge(FodotVocabulary other) {
		types.addAll(other.getTypes());
		predicates.addAll(getPredicates());
		functions.addAll(getFunctions());
	}
}
