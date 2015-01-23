package fodot.objects.general.sorting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fodot.util.CollectionPrinter;

public class PrerequisiteSorter<E> {

	private PrerequisiteExtractor<E> prereqExtractor;
	
	private Set<E> base;
	
	public PrerequisiteSorter(Collection<? extends E> alreadyAdded, PrerequisiteExtractor<E> extractor) {
		this.base = (alreadyAdded == null? new LinkedHashSet<E>() : new LinkedHashSet<E>(alreadyAdded));
		this.prereqExtractor = extractor;
	}
	
	public PrerequisiteSorter(PrerequisiteExtractor<E> extractor) {
		this(null, extractor);
	}
	
	
	public List<E> sort(Collection<? extends E> types) {
		Sorter sorter = new Sorter(base);
		return sorter.sort(types);
	}
	
	private class Sorter {

		private List<E> sorted = new ArrayList<E>();
		private List<E> toSort;
		private Set<E> alreadyTriedThisRound = new LinkedHashSet<E>();
		private Set<E> alreadyAdded;
		
		public Sorter(Collection<? extends E> alreadyAdded) {
			this.alreadyAdded = (alreadyAdded == null? new LinkedHashSet<E>() : new LinkedHashSet<E>(alreadyAdded));			
		}
		
		public List<E> sort(Collection<? extends E> types) {
			toSort = new ArrayList<E>(types);
			while (!toSort.isEmpty()) {
				tryAddingToSorted(toSort.get(0));
			}		
			return sorted;
		}
		
		private void tryAddingToSorted(E current) {
			/*Check if there's a loop like:
			 * type A constructed from {u(B)}
			 * type B constructed from {v(A)}
			 */
			if (alreadyTriedThisRound.contains(current)) {
				throw new IllegalStateException(
						"A loop has been detected in the order in which VocabularyElements should be sorted: \n"
						+ CollectionPrinter.toString(alreadyTriedThisRound));
			}
			
			//Check if printable
			Collection<? extends E> prereqs = prereqExtractor.getPrerequisitesOf(current);
			if (prereqs == null || alreadyAdded.containsAll(prereqs)) {
				addToSorted(current);
			}
			else {
				alreadyTriedThisRound.add(current);
				//Try printing the next type that printed does not contain
				tryAddingToSorted(getFirstNotPrinted(current));
			}
		}
		
		private E getFirstNotPrinted(E el) {
			Iterator<? extends E> it = prereqExtractor.getPrerequisitesOf(el).iterator();
			E current = it.next();
			while (alreadyAdded.contains(current) && it.hasNext()) {
				current = it.next();
			}
			
			//Check for errors
			if (alreadyAdded.contains(current)) {
				throw new IllegalStateException("Something has gone wrong in the vocabularyelements block with " + current
						+ "\nsorted:\n" + CollectionPrinter.toString(sorted)
						+ "\ntoSort:\n" + CollectionPrinter.toString(toSort)
						+ "\nalreadyAdded:\n" + CollectionPrinter.toString(alreadyAdded));
			}
			if (!toSort.contains(current)) {
				throw new IllegalStateException("A type that wasn't declared is needed to be sorted: " + current
						+ "\nsorted:\n" + CollectionPrinter.toString(sorted)
						+ "\ntoSort:\n" + CollectionPrinter.toString(toSort));
			}
			
			//return
			return current;
		}


		private void addToSorted(E decl) {
			sorted.add(decl);
			
			//Add to sorted, remove from toSort
			toSort.remove(decl);
			alreadyAdded.add(decl);
			
			//New round!
			alreadyTriedThisRound.clear();
		}
	}
	
}
