package playid.domain.fodot.structure.elements.typeenum.elements;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.theory.elements.terms.FodotConstant;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.IFodotDomainElement;

public class FodotInteger extends FodotElement implements IFodotTypeEnumerationElement, IFodotDomainElement {
	private FodotConstant constantInteger;
	
	public FodotInteger(FodotConstant constantInteger) {
		this.constantInteger = constantInteger;
	}

	@Override
	public String getValue() {
		return constantInteger.getValue();
	}

	public FodotType getType() {
		return constantInteger.getType();
	}
	
	@Override
	public String toCode() {
		return constantInteger.toCode();
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return Arrays.asList(constantInteger);
	}

	@Override
	public IFodotTerm toTerm() {
		return constantInteger;
	}


	@Override
	public Set<FodotType> getRequiredTypes() {
		return new HashSet<FodotType>();
	}	
	
	public FodotConstant getConstant() {
		return constantInteger;
	}

	/**********************************************
	 *  Default methods
	 ***********************************************/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constantInteger == null) ? 0 : constantInteger.hashCode());
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
		FodotInteger other = (FodotInteger) obj;
		if (constantInteger == null) {
			if (other.constantInteger != null)
				return false;
		} else if (!constantInteger.equals(other.constantInteger))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[ FodotIntegerEnumerationElement: "
				+ toCode() + "]";
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public String getName() {
		return getValue();
	}

	/**********************************************/

}
