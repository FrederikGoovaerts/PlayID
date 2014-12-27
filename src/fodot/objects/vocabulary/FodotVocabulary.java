package fodot.objects.vocabulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.file.FodotFileElementWithNamedElements;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;
import fodot.util.CollectionPrinter;

public class FodotVocabulary extends FodotFileElementWithNamedElements<IFodotVocabularyElement> implements IFodotFileElement {

	private static final String DEFAULT_NAME = "V";
	
	public FodotVocabulary(String name, Collection<? extends IFodotVocabularyElement> elements) {
		super(name, elements);
	}

	public FodotVocabulary(String name) {
		this(name, null);
	}

	public FodotVocabulary() {
		this(null, null);
	}

	/* FODOT ELEMENT */
	@Override
	public String toCode() {
		VocabularyElementPrerequisiteSorter sorter = new VocabularyElementPrerequisiteSorter(getElements());
		setElements(sorter.getSortedElements());
		return super.toCode();
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
						+ CollectionPrinter.toCoupleAsCode(alreadyTriedThisRound));
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
						+ "\nsorted:\n" + CollectionPrinter.toNewLinesWithTabsAsCode(sorted,2)
						+ "\ntoSort:\n" + CollectionPrinter.toNewLinesWithTabsAsCode(toSort,2)
						+ "\nalreadyAdded:\n" + CollectionPrinter.toNewLinesWithTabsAsCode(alreadyAdded,2));
			}
			if (!toSort.contains(currentDecl)) {
				throw new IllegalStateException("A type that wasn't declared is needed to be sorted: " + currentDecl
						+ "\nsorted:\n" + CollectionPrinter.toNewLinesWithTabsAsCode(sorted,2)
						+ "\ntoSort:\n" + CollectionPrinter.toNewLinesWithTabsAsCode(toSort,2));
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

	@Override
	public void mergeWith(IFodotFileElement other) {
		if (this.getClass().equals(other.getClass())) {
			merge((FodotVocabulary) other);
		}
	}

	@Override
	public String getFileElementName() {
		return "vocabulary";
	}

	@Override
	public String getDefaultName() {
		return DEFAULT_NAME;
	}
	
	
}
