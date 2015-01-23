package fodot.objects.vocabulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import fodot.objects.file.FodotFileElement;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.general.sorting.PrerequisiteExtractor;
import fodot.objects.general.sorting.PrerequisiteSorter;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.objects.vocabulary.elements.IFodotDomainElement;
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
	
	private static final PrerequisiteExtractor<IFodotVocabularyElement> EXTRACTOR = 
			new PrerequisiteExtractor<IFodotVocabularyElement>() {
		@Override
		public Collection<? extends IFodotVocabularyElement> getPrerequisitesOf(
				IFodotVocabularyElement element) {
			List<IFodotVocabularyElement> preqs = new ArrayList<IFodotVocabularyElement>();
			for (FodotType t : element.getPrerequiredTypes()) {
				preqs.add(t.getDeclaration());
			}
			return preqs;
		}
	};
	private static final PrerequisiteSorter<IFodotVocabularyElement> SORTER = 
			new PrerequisiteSorter<IFodotVocabularyElement>(
					Arrays.asList(FodotType.INTEGER.getDeclaration(), FodotType.NATURAL_NUMBER.getDeclaration()), EXTRACTOR);
	
	@Override
	public String toCode() {
		setElements(SORTER.sort(getElements()));
		return super.toCode();
	}
	
	/* HELPERS */
	public List<String> getAllClaimedNames() {
		List<String> result = new ArrayList<String>();
		for (IFodotElement t : getDirectElementsOfClass(FodotTypeDeclaration.class)) {
			FodotType type = ((FodotTypeDeclaration) t).getType();
			for (IFodotDomainElement el : type.getDomainElements()) {
				if (el instanceof FodotConstant) {
					result.add(((FodotConstant) el).getValue());					
				}
			}
		}
		return result;
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
