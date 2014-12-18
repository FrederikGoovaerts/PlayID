package fodot.objects.sentence.terms;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.vocabulary.elements.FodotType;

public class FodotConstant implements IFodotTerm {

	private String value;
	private FodotType type;
	
	public FodotConstant(String value, FodotType type) {
		super();
		setValue(value);
		setType(type);
	}

	//VALUE
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	//TYPE	
	public void setType(FodotType type) {
		if (type == null) {
			throw new IllegalArgumentException("Not a legal type");
		}
		this.type = type;
		//if (type.hasDeclaration()) {
			this.type.addDomainElement(this);
		//}
	}

	public FodotType getType() {
		return type;
	}
	//FODOT SENTENCE ELEMENT
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
