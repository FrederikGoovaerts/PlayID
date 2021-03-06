package playid.domain.fodot.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import playid.domain.fodot.general.FodotNamedElementContainer;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.general.sorting.PrerequisiteExtractor;
import playid.domain.fodot.general.sorting.PrerequisiteSorter;
import playid.domain.fodot.includes.FodotIncludeHolder;
import playid.util.CollectionPrinter;

public class FodotFile extends FodotNamedElementContainer<IFodotFileElement> implements IFodotFile {
	
	private FodotIncludeHolder includes;
	
	public FodotFile(FodotIncludeHolder includes, Collection<? extends IFodotFileElement> elements) {
		super(elements);
		setIncludes(includes);
	}
	
	public FodotFile(Collection<? extends IFodotFileElement> elements) {
		this(null, elements);
	}
	
	public FodotFile() {
		this(null);
	};
	
	
	private static final PrerequisiteExtractor<IFodotFileElement> EXTRACTOR = new PrerequisiteExtractor<IFodotFileElement>() {
		@Override
		public Collection<? extends IFodotFileElement> getPrerequisitesOf(
				IFodotFileElement element) {
			return element.getPrerequiredElements();
		}
	};
	private static final PrerequisiteSorter<IFodotFileElement> SORTER = new PrerequisiteSorter<IFodotFileElement>(EXTRACTOR);
	
	@Override
	public String toCode() {
		List<IFodotFileElement> fileElements = new ArrayList<IFodotFileElement>(getElements());
		fileElements = SORTER.sort(fileElements);
		return (getIncludes() == null? "" : getIncludes().toCode() + "\n\n")
				+ CollectionPrinter.printStringList("", "", "\n\n", CollectionPrinter.toCode(fileElements));
	}

	
	
	private FodotIncludeHolder getIncludes() {
		return includes;
	}

	private void setIncludes(FodotIncludeHolder includes) {
		this.includes = includes;
	}

	@Override
	public void addIncludes(FodotIncludeHolder argIncludes) {
		if (this.includes == null) {
			setIncludes(argIncludes);
		} else {
			getIncludes().mergeWith(argIncludes);
		}		
	}

	@Override
	public boolean isValidElement(IFodotFileElement argElement) {
		return argElement != null
				&& !containsElementWithName(argElement.getName())
				&&	(
						argElement.getPrerequiredElements() == null
					||
						containsAllElements(argElement.getPrerequiredElements())
					);
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		Set<IFodotElement> result = new LinkedHashSet<IFodotElement>(getElements());
		result.add(getIncludes());
		return result;
	}

}
