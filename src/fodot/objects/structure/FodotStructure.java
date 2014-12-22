package fodot.objects.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.util.CollectionPrinter;

public class FodotStructure implements IFodotElement {

	private String name;
	private FodotVocabulary vocabulary;
	private Set<IFodotStructureElement> elements;

	public FodotStructure(String name, FodotVocabulary vocabulary, Collection<? extends IFodotStructureElement> elements) {
		super();
		setName(name);
		setVocabulary(vocabulary);
		setElements(elements);
	}

	private static final String DEFAULT_NAME = "S";
	
	public FodotStructure(FodotVocabulary voc) {
		this(null, voc, new ArrayList<IFodotStructureElement>());
	}

	
	/* NAME */

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = (name == null ? DEFAULT_NAME : name);
	}


	/**********************************************
	 *  Elements methods
	 ***********************************************/
	private void setElements(Collection<? extends IFodotStructureElement> argElements) {
		this.elements = (isValidElements(argElements) ? new LinkedHashSet<IFodotStructureElement>(argElements)
				: new LinkedHashSet<IFodotStructureElement>());
	}

	private boolean isValidElements(Collection<? extends IFodotStructureElement> argElements) {
		return argElements != null;
	}

	public Set<IFodotStructureElement> getElements() {
		return new LinkedHashSet<IFodotStructureElement>(elements);
	}

	public void addElement(IFodotStructureElement argElement) {
		this.elements.add(argElement);
	}

	public void addAllElements(Set<IFodotStructureElement> argElements) {
		for (IFodotStructureElement element : argElements) {
			addElement(element);
		}
	}

	public boolean containsElement(IFodotStructureElement element) {
		return this.elements.contains(element);
	}

	public boolean hasElements() {
		return !elements.isEmpty();
	}

	public void removeElements(IFodotStructureElement argElement) {
		this.elements.remove(argElement);
	}
	/**********************************************/
	
	/* ENUMERATIONS */


	@Deprecated
	public void addEnumeration(IFodotStructureElement enumeration) {
		elements.add(enumeration);
	}

	@Deprecated
	public void removeEnumeration(IFodotStructureElement enumeration) {
		elements.remove(enumeration);
	}

	/* VOCABULARY */

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	private void setVocabulary(FodotVocabulary voc) {
		this.vocabulary = (voc == null? new FodotVocabulary() : voc);
		
	}
	
	/* FODOT ELEMENT */
	
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("structure "+getName() + " : " + getVocabulary().getName() + " {\n");
		builder.append(CollectionPrinter.toNewLinesWithTabsAsCode(elements,1));
		builder.append("}");
		return builder.toString();
	}

	/* MERGE */
	public void merge(FodotStructure other) {
		elements.addAll(other.getElements());
	}

	
	
}
