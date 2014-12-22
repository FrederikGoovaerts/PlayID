package fodot.objects.file;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.util.CollectionUtil;

public abstract class FodotFileElement<E extends IFodotElement> implements IFodotFileElement {
	private Set<E> elements;
	private String name;
	private Set<IFodotFileElement> prerequired;

	public FodotFileElement(String name, Set<E> elements,
			Collection<? extends IFodotFileElement> prerequired) {
		super();
		setElements(elements);
		setName(name);
		setPrerequiredElements(prerequired);
	}
	
	public FodotFileElement(String name, Set<E> elements) {
		this(name, elements, null);
	}
	
	

	/**********************************************
	 *  Elements methods
	 ***********************************************/
	private void setElements(Collection<? extends E> argElements) {
		this.elements = (isValidElements(argElements) ? new LinkedHashSet<E>(argElements)
				: new LinkedHashSet<E>());
	}

	private boolean isValidElements(Collection<? extends E> argElements) {
		return argElements != null;
	}

	public Set<E> getElements() {
		return new LinkedHashSet<E>(elements);
	}

	public void addElement(E argElement) {
		this.elements.add(argElement);
	}

	public void addAllElements(Collection<? extends E> argElements) {
		if (argElements != null) {
			this.elements.addAll(argElements);
		}
	}

	public boolean containsElement(E element) {
		return this.elements.contains(element);
	}

	public boolean hasElements() {
		return !elements.isEmpty();
	}

	public void removeElement(E argElement) {
		this.elements.remove(argElement);
	}

	public int getAmountOfElements() {
		return this.elements.size();
	}
	/**********************************************/
	
	//Prerequisites
	@Override
	public Set<IFodotFileElement> getPrerequiredElements() {
		return prerequired;
	}

	public void setPrerequiredElements(Collection<? extends IFodotFileElement> prerequired) {
		this.prerequired = (prerequired == null ? new HashSet<IFodotFileElement>() : new HashSet<IFodotFileElement>(prerequired));
	}
	
	//Name

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = (name == null? getDefaultName() : name);
	}
	
	public abstract String getDefaultName();

	/**
	 * For example: vocabulary, theory, structure etc
	 */
	public abstract String getFileElementName();
	
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(getFileElementName() + " " + getName());
		if (!getPrerequiredElements().isEmpty()) {
			builder.append(" : " + CollectionUtil.toNakedList(CollectionUtil.toCode(getElements())));
		}
		builder.append(" {\n");
		builder.append(CollectionUtil.toNewLinesWithTabsAsCode(elements,1));
		builder.append("}");
		return builder.toString();
	}
	
}
