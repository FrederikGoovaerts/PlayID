package fodot.objects.includes;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.util.CollectionUtil;

public class FodotIncludeHolder implements IFodotElement {

	private Set<FodotInclude> includes;
		
	public FodotIncludeHolder(Set<FodotInclude> includes) {
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

	private void setIncludes(Set<FodotInclude> includes) {
		this.includes = (includes == null ? new HashSet<FodotInclude>() : includes);
	}
	
	//IFodotElement

	@Override
	public String toCode() {		
		return CollectionUtil.printStringList("", "", "\n", CollectionUtil.toCode(getIncludes())) + "\n";
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
