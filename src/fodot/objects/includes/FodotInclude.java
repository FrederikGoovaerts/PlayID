package fodot.objects.includes;

import fodot.objects.general.IFodotElement;

public abstract class FodotInclude implements IFodotElement {

	private String toInclude;
	
	public FodotInclude(String toInclude) {
		super();
		this.toInclude = toInclude;
	}
	
	public String getToInclude() {
		return toInclude;
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
		FodotInclude other = (FodotInclude) obj;
		if (toInclude == null) {
			if (other.toInclude != null)
				return false;
		} else if (!toInclude.equals(other.toInclude))
			return false;
		return true;
	}

}
