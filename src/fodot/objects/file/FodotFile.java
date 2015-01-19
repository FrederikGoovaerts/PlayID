package fodot.objects.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.general.FodotNamedElementContainer;
import fodot.objects.general.sorting.PrerequisiteExtractor;
import fodot.objects.general.sorting.PrerequisiteSorter;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.util.CollectionPrinter;

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
		return (getIncludes() == null? "" : getIncludes() + "\n\n")
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
		return !containsElementWithName(argElement.getName());
	}

}
