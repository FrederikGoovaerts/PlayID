package fodot.objects.theory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.file.FodotFileElement;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.FodotSentence;
import fodot.objects.theory.elements.IFodotTheoryElement;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.util.CollectionPrinter;
import fodot.util.NameUtil;
import fodot.util.TermUtil;

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
	

	/**********************************************
	 *  Overrides for add methods:
	 *  Check if no namecollisions with vocabulary
	 ***********************************************/
	@Override
	public void addElement(IFodotTheoryElement element) {
		solveNameCollisions(Arrays.asList(element));
		super.addElement(element);
	}
	
	@Override
	public void addAllElements(Collection<? extends IFodotTheoryElement> elements) {
		solveNameCollisions(elements);
		super.addAllElements(elements);
	}
	
	
	private void solveNameCollisions(Collection<? extends IFodotTheoryElement> elements) {
		if (getVocabulary() != null) {
			//Get all names claimed by constants in the vocabulary
			List<String> prohibitedVarNames = getVocabulary().getAllClaimedNames();
			
			//Solve all namecollisions
			for (IFodotTheoryElement theoryElement : elements) {
				for (IFodotElement sentence : theoryElement.getAllInnerElementsOfClass(FodotSentence.class)) {
					Collection<FodotVariable> variables = TermUtil.extractVariables((FodotSentence) sentence);
					NameUtil.solveNameCollisions(prohibitedVarNames, variables);
					
				}
			}			
		}
	}

	/**********************************************/

	/* VOCABULARY */
	private void setVocabulary(FodotVocabulary voc) {
		this.vocabulary = (voc == null? new FodotVocabulary() : voc);
		solveNameCollisions(getElements());
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
