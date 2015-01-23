package fodot.objects.includes.elements;

import java.util.Collection;
import java.util.HashSet;

import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;

public abstract class FodotIncludeStatement extends FodotElement implements IFodotElement {

	private String toInclude;
	
	public FodotIncludeStatement(String toInclude) {
		super();
		this.toInclude = toInclude;
	}
	
	public String getToInclude() {
		return toInclude;
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return new HashSet<IFodotElement>();
	}
	
	@Override
	public abstract String toCode();

	@Override
	public abstract String toString() ;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((toInclude == null) ? 0 : toInclude.hashCode());
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
		FodotIncludeStatement other = (FodotIncludeStatement) obj;
		if (toInclude == null) {
			if (other.toInclude != null)
				return false;
		} else if (!toInclude.equals(other.toInclude))
			return false;
		return true;
	}

}
