package fodot.objects.sentence.terms;

import java.util.HashSet;
import java.util.Set;

public class FodotConstant implements IFodotTerm {

	public String value;
	
	public FodotConstant(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public Set<FodotVariable> getFreeVariables() {
		return new HashSet<FodotVariable>();
	}

	@Override
	public String toCode() {
		return value;
	}

	@Override
	public String toString() {
		return "[constant: "+getValue()+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		FodotConstant other = (FodotConstant) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
}
