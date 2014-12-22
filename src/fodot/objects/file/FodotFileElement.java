package fodot.objects.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.util.CollectionPrinter;

public abstract class FodotFileElement<E extends IFodotElement> implements IFodotFileElement {
	private Set<E> elements;
	private String name;
	private Set<IFodotFileElement> prerequired;

	public FodotFileElement(String name, Collection<? extends E> elements,
			Collection<? extends IFodotFileElement> prerequired) {
		super();
		setElements(elements);
		setName(name);
		setPrerequiredElements(prerequired);
	}
	
	public FodotFileElement(String name, Collection<? extends E> elements, IFodotFileElement... prerequired) {
		this(name, elements, Arrays.asList(prerequired));
	}
	
	public FodotFileElement(String name, Collection<? extends E> elements) {
		this(name, elements, new HashSet<IFodotFileElement>());
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
	
	public Set<E> getElementsOfClass(Class<?> clazz) {
		if (clazz == null) {
			return new HashSet<E>();
		}
		Set<E> result = new LinkedHashSet<E>();
		for (E el : elements) {
			if (clazz.isInstance(el)) {
				result.add(el);
			}
		}
		return result;
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

	public void setPrerequiredElements(IFodotFileElement... prerequired) {
		setPrerequiredElements(Arrays.asList(prerequired));
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
		
		//Print prerequisites
		if (getPrerequiredElements() != null && !getPrerequiredElements().isEmpty()) {
			List<String> names = new ArrayList<String>();
			for (IFodotFileElement e :getPrerequiredElements()) {
				names.add(e.getName());
			}
			builder.append(" : " + CollectionPrinter.toNakedList(names));
		}
		
		//Print elements
		builder.append(" {\n");
		builder.append(CollectionPrinter.toNewLinesWithTabsAsCode(elements,1));
		builder.append("}");
		return builder.toString();
	}
	
}
