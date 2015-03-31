package playid.domain.fodot.theory.elements.terms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import playid.domain.exceptions.fodot.FodotException;
import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.structure.elements.typeenum.elements.FodotInteger;
import playid.domain.fodot.structure.elements.typeenum.elements.FodotTypeFunctionEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.FodotTypeFunctionDeclaration;
import playid.domain.fodot.vocabulary.elements.IFodotDomainElement;

public class FodotConstant extends FodotElement implements IFodotTerm {

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
			throw new FodotException("Not a legal type");
		}
		this.type = type;
		if (!type.containsDomainElement(this.toDomainElement())) {
			this.type.addDomainElement(this.toDomainElement());
		}
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
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return new HashSet<IFodotElement>();
	}

	private FodotTypeFunctionDeclaration functionDeclaration;
	private IFodotTypeEnumerationElement enumerationElement;
	
	private void initDerivedElements() {
		if (functionDeclaration == null) {
			functionDeclaration = new FodotTypeFunctionDeclaration(getValue(), new ArrayList<FodotType>(), getType());
			enumerationElement =  new FodotTypeFunctionEnumerationElement(functionDeclaration, new ArrayList<IFodotTypeEnumerationElement>());
		}		
	}
	
	@Override
	public IFodotTypeEnumerationElement toEnumerationElement() {
		if (getType().isRelatedTo(FodotType.INTEGER)) {
			return new FodotInteger(this);
		} else {
			initDerivedElements();
			return enumerationElement;
		}
	}
	
	public IFodotDomainElement toDomainElement() {
		if (getType().isRelatedTo(FodotType.INTEGER)) {
			return new FodotInteger(this);
		} else {
			initDerivedElements();
			return functionDeclaration;
		}
	}
	
	

}
