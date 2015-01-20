package fodot.objects.theory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import fodot.objects.file.FodotFileElement;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.theory.elements.IFodotTheoryElement;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.util.CollectionPrinter;

public class FodotTheory extends FodotFileElement<IFodotTheoryElement> implements IFodotFileElement {

	private static final String DEFAULT_NAME = "T";
	private FodotVocabulary vocabulary;

	/**********************************************
	 *  Constructors
	 ***********************************************/

	public FodotTheory(String name,
					   FodotVocabulary vocabulary,
					   Collection<? extends IFodotTheoryElement> elements) {
		super(name, elements, vocabulary);
		setVocabulary(vocabulary);
	}
	
	public FodotTheory(String name, FodotVocabulary vocabulary) {
		this(name, vocabulary, null);
	}
	
	public FodotTheory(FodotVocabulary voc) {
		this(DEFAULT_NAME, voc);	
	}

	/**********************************************/	

	/* VOCABULARY */
	private void setVocabulary(FodotVocabulary voc) {
		this.vocabulary = (voc == null? new FodotVocabulary() : voc);
	}

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	// OBLIGATORY METHODS

	@Override
	public String getFileElementName() {
		return "theory";
	}

	@Override
	public String getDefaultName() {
		return DEFAULT_NAME;
	}

	@Override
	public boolean isValidElement(IFodotTheoryElement argElement) {
		return argElement != null;
	}
	
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("theory " + getName() + " : " + getVocabulary().getName() + " {\n");
		builder.append(CollectionPrinter.toNewLinesWithTabsAsCode(getElements(),1));		
		builder.append("}");
		return builder.toString();
	}

	public void merge(FodotTheory other) {
		addAllElements(other.getElements());
	}

	@Override
	public Set<IFodotFileElement> getPrerequiredElements() {
		return new HashSet<IFodotFileElement>(Arrays.asList(getVocabulary()));
	}

	@Override
	public void mergeWith(IFodotFileElement other) {
		if (this.getClass().equals(other.getClass())) {
			merge((FodotTheory) other);
		}
	}
	
}
