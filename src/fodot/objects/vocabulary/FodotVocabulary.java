package fodot.objects.vocabulary;

import java.util.Collection;

import fodot.objects.file.FodotFileElementWithNamedElements;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;

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
		VocabularyElementSorter sorter = new VocabularyElementSorter(getElements());
		setElements(sorter.getSortedElements());
		return super.toCode();
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
