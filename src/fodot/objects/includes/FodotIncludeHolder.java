package fodot.objects.includes;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import fodot.objects.general.IFodotElement;
import fodot.util.CollectionPrinter;

public class FodotIncludeHolder implements IFodotElement {

	private Set<FodotInclude> includes;
		
	public FodotIncludeHolder(Collection<? extends FodotInclude> includes) {
		super();
		setIncludes(includes);
	}
	
	public FodotIncludeHolder() {
		this(null);
	}

	//Includes
	public Set<FodotInclude> getIncludes() {
		return includes;
	}	

	private void setIncludes(Collection<? extends FodotInclude> includes) {
		this.includes = (includes == null ? new LinkedHashSet<FodotInclude>() : new LinkedHashSet<FodotInclude>(includes));
	}
	
	//IFodotElement

	@Override
	public String toCode() {		
		return CollectionPrinter.printStringList("", "", "\n", CollectionPrinter.toCode(getIncludes())) + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((includes == null) ? 0 : includes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FodotIncludeHolder other = (FodotIncludeHolder) obj;
		if (includes == null) {
			if (other.includes != null)
				return false;
		} else if (!includes.equals(other.includes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FodotIncludesHolder [includes=" + includes + "]";
	}
	
	public void merge(FodotIncludeHolder other) {
		includes.addAll(other.getIncludes());
	}

}
