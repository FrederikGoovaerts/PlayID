package fodot.objects.theory.elements.terms;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.IFodotSentenceElement;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.IFodotDomainElement;

public class FodotConstant implements IFodotTerm, IFodotDomainElement, IFodotTypeEnumerationElement {

	private static final int BINDING_ORDER = -1;
	
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
		this.type.addDomainElement(this);
	}

	public FodotType getType() {
		return type;
	}
	//FODOT SENTENCE ELEMENT
	@Override
	public Set<IFodotSentenceElement> getElementsOfClass(Class<? extends IFodotSentenceElement> clazz) {
		Set<IFodotSentenceElement> result = new HashSet<IFodotSentenceElement>();
		
		//Check for this itself
		if (clazz.isAssignableFrom(this.getClass())) {
			result.add(this);
		}
		
		return result;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		return new HashSet<FodotVariable>();
	}

	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
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
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public Set<FodotType> getRequiredTypes() {
		return new HashSet<FodotType>();
	}
	
	
}