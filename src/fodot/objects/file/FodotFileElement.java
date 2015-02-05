package fodot.objects.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.general.FodotElementContainer;
import fodot.objects.general.IFodotElement;
import fodot.util.CollectionPrinter;
import fodot.util.NameUtil;

public abstract class FodotFileElement<E extends IFodotElement> extends FodotElementContainer<E> implements IFodotFileElement {

	private String name;
	private Set<IFodotFileElement> prerequired;

	public FodotFileElement(String name, Collection<? extends E> elements,
			Collection<? extends IFodotFileElement> prerequired, int minSize, int maxSize) {
		super(elements, minSize, maxSize);
		setName(name);
		setPrerequiredElements(prerequired);		
	}
	
	public FodotFileElement(String name, Collection<? extends E> elements,
			Collection<? extends IFodotFileElement> prerequired) {
		this(name, elements, prerequired, -1, -1);
	}
	
	public FodotFileElement(String name, Collection<? extends E> elements, IFodotFileElement... prerequired) {
		this(name, elements, Arrays.asList(prerequired));
	}
	
	public FodotFileElement(String name, Collection<? extends E> elements) {
		this(name, elements, new HashSet<IFodotFileElement>());
	}
	
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
	/**
	 * For example: vocabulary, theory, structure etc
	 */
	public abstract String getFileElementName();
		

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = (NameUtil.isValidName(name)? name : getDefaultName());
	}
	
	public abstract String getDefaultName();
	
	//Tocode
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
		builder.append(CollectionPrinter.toNewLinesWithTabsAsCode(getElements(),1));
		builder.append("}");
		return builder.toString();
	}
		
	//Hashcode&Equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((prerequired == null) ? 0 : prerequired.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FodotFileElement<?> other = (FodotFileElement<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (prerequired == null) {
			if (other.prerequired != null)
				return false;
		} else if (!prerequired.equals(other.prerequired))
			return false;
		return true;
	}

	
}
