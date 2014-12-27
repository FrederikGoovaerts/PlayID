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
			Collection<? extends IFodotFileElement> prerequired) {
		super(elements);
		setName(name);
		setPrerequiredElements(prerequired);
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
	
}
