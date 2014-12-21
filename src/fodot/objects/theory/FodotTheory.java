package fodot.objects.theory;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.theory.definitions.FodotInductiveDefinitionBlock;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.util.CollectionUtil;

public class FodotTheory implements IFodotElement {

	private String name;
	private FodotVocabulary vocabulary;
	private Set<IFodotTheoryElement> elements;

	public FodotTheory(String name,
					   FodotVocabulary vocabulary,
					   Collection<? extends IFodotTheoryElement> elements) {
		super();
		setName(name);
		setVocabulary(vocabulary);
		setElements(elements);
	}
	
	public FodotTheory(String name, FodotVocabulary vocabulary) {
		this(name, vocabulary, null);
	}
	
	private static final String DEFAULT_NAME = "T";
	
	public FodotTheory(FodotVocabulary voc) {
		this(DEFAULT_NAME, voc);
	}

	/* VOCABULARY */
	private void setVocabulary(FodotVocabulary voc) {
		this.vocabulary = (voc == null? new FodotVocabulary() : voc);
	}

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	/* ELEMENTS */
	private void setElements(Collection<? extends IFodotTheoryElement> argElements) {
		this.elements = (isValidElements(argElements) ? new LinkedHashSet<IFodotTheoryElement>(argElements) : new LinkedHashSet<IFodotTheoryElement>());
	}
	
	private boolean isValidElements(Collection<? extends IFodotTheoryElement> argElements) {
		return argElements != null;
	}
	
	
	public Set<IFodotTheoryElement> getElements() {
		return new LinkedHashSet<IFodotTheoryElement>(elements);
	}
	public void addElement(IFodotTheoryElement argElement) {
		this.elements.add(argElement);
	}
	
	public void addAllElements(Set<IFodotTheoryElement> argElements) {
		elements.addAll(argElements);
	}
	
	public boolean containsElement(IFodotTheoryElement element){
		return this.elements.contains(element);
	}
	
	public boolean hasElements() {
		return !elements.isEmpty();
	}
	
	public void removeElements(IFodotTheoryElement argElement) {
		this.elements.remove(argElement);
	}
	
	/* DEFINITIONS */
	@Deprecated
	public void addInductiveDefinition(FodotInductiveDefinitionBlock definition) {
		elements.add(definition);
	}

	@Deprecated
	public void removeInductiveDefinition(FodotInductiveDefinitionBlock definition) {
		elements.remove(definition);
	}
	
	/* SENTENCES */
	@Deprecated
	public void addSentence(FodotSentence sentence) {
		elements.add(sentence);
	}

	@Deprecated
	public void removeSentence(FodotSentence sentence) {
		elements.remove(sentence);
	}

	/* NAME */
	public void setName(String name) {
		this.name = (name == null ? DEFAULT_NAME : name);
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("theory " + getName() + ": " + getVocabulary().getName() + " {\n");
		builder.append(CollectionUtil.toNewLinesWithTabsAsCode(getElements(),1));		
		builder.append("}");
		return builder.toString();
	}

	public void merge(FodotTheory other) {
		addAllElements(other.getElements());
	}
	
}
