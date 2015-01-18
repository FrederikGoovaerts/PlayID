package fodot.objects.vocabulary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.file.FodotFileElement;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;

public class FodotVocabulary extends FodotFileElement<IFodotVocabularyElement> implements IFodotFileElement {

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

	//Named stuff
	public List<IFodotVocabularyElement> getElementsWithName(String name) {
		List<IFodotVocabularyElement> result = new ArrayList<IFodotVocabularyElement>();
		if (name != null) {
			for (IFodotVocabularyElement el : getElements()) {
				if (name.equals(el.getName())) {
					result.add(el);
				}
			}
		}
		return result;
	}

	public boolean containsElementWithName(String name) {
		return !getElementsWithName(name).isEmpty();
	}
	
	
	@Override
	public boolean isValidElement(IFodotVocabularyElement argEl) {
		if (argEl == null) {
			return false;
		}
		if (containsElement(argEl)) {
			return true;
		}
		//Types with the same name is okay as long as the arity differst!
		if (containsElementWithName(argEl.getName())) {
			for(IFodotVocabularyElement el : getElementsWithName(argEl.getName())) {
				if (el.getArity() == argEl.getArity()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/* FODOT ELEMENT */
	@Override
	public String toCode() {
		VocabularyElementSorter sorter = new VocabularyElementSorter(getElements());
		setElements(sorter.getSortedElements());
		return super.toCode();
	}

	/* MERGE */

	@Override
	public String getFileElementName() {
		return "vocabulary";
	}

	@Override
	public String getDefaultName() {
		return DEFAULT_NAME;
	}
	

	@Override
	public String toString() {
		return "[FodotVocabulary: " + toCode() + "]";
	}
	
	
}
