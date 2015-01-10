package fodot.objects.theory.elements.terms;

import java.util.HashSet;
import java.util.Set;

import fodot.exceptions.InvalidTermNameException;
import fodot.objects.theory.elements.IFodotSentenceElement;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.NameUtil;

public class FodotVariable implements IFodotTerm {

	private static final int BINDING_ORDER = -1;
	
	private String name;
	private FodotType type;
	
	public FodotVariable(String name, FodotType type) {
		super();
		this.type = type;
		setName(name);
	}

	public void setName(String name) {
		if (!NameUtil.isValidName(name)) {
			throw new InvalidTermNameException(name);
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public FodotType getType() {
		return type;
	}
	
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
		Set<FodotVariable> result = new HashSet<FodotVariable>();
		result.add(this);
		return result;
	}

	@Override
	public String toCode() {
		return name;
	}

	@Override
	public String toString() {
		return "[variable: "+getName()+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		FodotVariable other = (FodotVariable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
	}
	
}
