package fodot.objects.vocabulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;
import fodot.util.CollectionUtil;
import fodot.util.NameUtil;

public class FodotVocabulary implements IFodotElement {

	private static final String DEFAULT_NAME = "V";
	
	//Maybe convert the three sets in just a set of FodotDeclarations?
	private String name;
	private Set<IFodotVocabularyElement> elements;
	
	public FodotVocabulary(String name, Collection<? extends IFodotVocabularyElement> elements) {
		setName(name);
		setElements(elements);
	}
	
	@Deprecated
	public FodotVocabulary(String name, Set<FodotTypeDeclaration> types, Set<FodotPredicateDeclaration> predicates,
			Set<FodotFunctionDeclaration> functions) {
		setName(name);
		setElements(null);
		addAllElements(types);
		addAllElements(predicates);
		addAllElements(functions);
	}

	public FodotVocabulary(String name) {
		this(name, null);
	}

	public FodotVocabulary() {
		this(null, null);
	}


	/**********************************************
	 *  Elements methods
	 ***********************************************/
	private void setElements(Collection<? extends IFodotVocabularyElement> argElements) {
		this.elements = (isValidElements(argElements) ? new LinkedHashSet<IFodotVocabularyElement>(argElements)
				: new LinkedHashSet<IFodotVocabularyElement>());
	}

	private boolean isValidElements(Collection<? extends IFodotVocabularyElement> argElements) {
		return argElements != null;
	}

	public Set<IFodotVocabularyElement> getElements() {
		return new LinkedHashSet<IFodotVocabularyElement>(elements);
	}

	public void addElement(IFodotVocabularyElement argElement) {
		if (!containsElement(argElement) && containsElementWithName(argElement.getName()))
			throw new RuntimeException("Vocabulary " + this + " already contains an element with name " + argElement.getName());
		this.elements.add(argElement);
	}

	public void addAllElements(Collection<? extends IFodotVocabularyElement> argElements) {
		if (argElements != null) {
			for (IFodotVocabularyElement el : argElements) {
				this.elements.add(el);				
			}
		}
	}

	public boolean containsElement(IFodotVocabularyElement element) {
		return this.elements.contains(element);
	}

	public boolean containsElementWithName(String name) {
		if (name == null) {
			return false;
		}
		for (IFodotVocabularyElement el : elements) {
			if (el.getName() != null && name.equals(el.getName())) {
				return true;
			}
		}
		return false;
	}	

	public boolean hasElements() {
		return !elements.isEmpty();
	}

	public void removeElement(IFodotVocabularyElement argElement) {
		this.elements.remove(argElement);
	}

	public int getAmountOfElements() {
		return this.elements.size();
	}
	
	/**********************************************/

	
	/* TYPES */
	@Deprecated
	public void addType(FodotTypeDeclaration type) {
		addElement(type);
	}

	@Deprecated
	public void removeType(FodotTypeDeclaration type) {
		removeElement(type);
	}

	@Deprecated
	public boolean containsTypeWithName(String name) {
		return containsElementWithName(name);
	}

	@Deprecated
	public boolean containsType(FodotTypeDeclaration type) {
		return elements.contains(type);
	}

	/* PREDICATES */

	@Deprecated
	public void addPredicate(FodotPredicateDeclaration predicate) {
		elements.add(predicate);
	}

	@Deprecated
	public void removePredicate(FodotPredicateDeclaration predicate) {
		elements.remove(predicate);
	}


	/* FUNCTIONS */


	@Deprecated
	public void addFunction(FodotFunctionDeclaration function) {
		elements.add(function);
	}

	@Deprecated
	public void removeFunction(FodotFunctionDeclaration function) {
		elements.remove(function);
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


		VocabularyElementPrerequisiteSorter sorter = new VocabularyElementPrerequisiteSorter(getElements());
		builder.append(CollectionUtil.toNewLinesWithTabsAsCode(sorter.getSortedElements(),1));		
		
		//STRINGIFY FUNCTIONS&PREDICATES
//		builder.append(CollectionUtil.toNewLinesWithTabsAsCode(getFunctions(),1));
//		builder.append(CollectionUtil.toNewLinesWithTabsAsCode(getPredicates(),1));

		builder.append("}");
		return builder.toString();
	}
	
 
	/**
	 * This is a stable sorted that will sort the list in such a way that
	 * all the types necessary in vocabulary elements were declared before they are used as arguments
	 * @author Thomas Winters
	 *
	 */
	private class VocabularyElementPrerequisiteSorter {
		private List<IFodotVocabularyElement> toSort;
		private Set<IFodotVocabularyElement> alreadyTriedThisRound = new LinkedHashSet<IFodotVocabularyElement>();
		private Set<IFodotVocabularyElement> alreadyAdded = new LinkedHashSet<IFodotVocabularyElement>(
				Arrays.asList(FodotType.INTEGER.getDeclaration(), FodotType.NATURAL_NUMBER.getDeclaration()));
		
		private List<IFodotVocabularyElement> sorted = new ArrayList<IFodotVocabularyElement>();
		
		public VocabularyElementPrerequisiteSorter(Collection<? extends IFodotVocabularyElement> types) {
			this.toSort = new ArrayList<IFodotVocabularyElement>(types);
		}
		
		
		public List<IFodotVocabularyElement> getSortedElements() {
			while (!toSort.isEmpty()) {
				tryAddingToSorted(toSort.get(0));
			}		
			return sorted;
		}
		
		private void tryAddingToSorted(IFodotVocabularyElement current) {
			/*Check if there's a loop like:
			 * type A constructed from {u(B)}
			 * type B constructed from {v(A)}
			 */
			if (alreadyTriedThisRound.contains(current)) {
				throw new IllegalStateException(
						"A loop has been detected in the order in which VocabularyElements should be sorted: \n"
						+ CollectionUtil.toCoupleAsCode(alreadyTriedThisRound));
			}
			
			//Check if printable
			if (alreadyAdded.containsAll(getDeclarations(current.getPrerequiredTypes()))) {
				addToSorted(current);
			}
			else {
				alreadyTriedThisRound.add(current);
				//Try printing the next type that printed does not contain
				tryAddingToSorted(getFirstNotPrinted(current));
			}
		}
		
		private FodotTypeDeclaration getFirstNotPrinted(IFodotVocabularyElement el) {
			Iterator<FodotType> it = el.getPrerequiredTypes().iterator();
			FodotTypeDeclaration currentDecl = it.next().getDeclaration();
			while (alreadyAdded.contains(currentDecl) && it.hasNext()) {
				currentDecl = it.next().getDeclaration();
			}
			
			//Check for errors
			if (alreadyAdded.contains(currentDecl)) {
				throw new IllegalStateException("Something has gone wrong in the vocabularyelements block with " + currentDecl
						+ "\nsorted:\n" + CollectionUtil.toNewLinesWithTabsAsCode(sorted,2)
						+ "\ntoSort:\n" + CollectionUtil.toNewLinesWithTabsAsCode(toSort,2)
						+ "\nalreadyAdded:\n" + CollectionUtil.toNewLinesWithTabsAsCode(alreadyAdded,2));
			}
			if (!toSort.contains(currentDecl)) {
				throw new IllegalStateException("A type that wasn't declared is needed to be sorted: " + currentDecl
						+ "\nsorted:\n" + CollectionUtil.toNewLinesWithTabsAsCode(sorted,2)
						+ "\ntoSort:\n" + CollectionUtil.toNewLinesWithTabsAsCode(toSort,2));
			}
			
			//return
			return currentDecl;
		}


		private void addToSorted(IFodotVocabularyElement decl) {
			sorted.add(decl);
			
			//Add to printed, remove from toPrint
			toSort.remove(decl);
			alreadyAdded.add(decl);
			
			//New round!
			alreadyTriedThisRound.clear();
		}
		
		private Set<FodotTypeDeclaration> getDeclarations(Set<FodotType> types) {
			Set<FodotTypeDeclaration> result = new HashSet<FodotTypeDeclaration>();
			for (FodotType t : types) {
				result.add(t.getDeclaration());
			}
			return result;
		}

	}
	
	/* MERGE */

	public void merge(FodotVocabulary other) {
		addAllElements(other.getElements());
	}
}
