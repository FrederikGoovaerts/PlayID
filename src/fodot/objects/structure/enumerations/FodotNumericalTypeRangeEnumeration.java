package fodot.objects.structure.enumerations;

import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

public class FodotNumericalTypeRangeEnumeration extends FodotEnumeration {
	private FodotType type;
	private FodotConstant headValue;
	private FodotConstant lastValue;

	public FodotNumericalTypeRangeEnumeration(FodotType type,
											  FodotConstant head,
											  FodotConstant last) {
		super();
		this.type = type;
		this.headValue = head;
		this.lastValue = last;
	}

	public FodotType getType() {
		return type;
	}

	/* VALUES */
	
	public void setHeadValue(FodotConstant value) {
		this.headValue = value;
	}

	public void setLastValue(FodotConstant value) {
		this.lastValue = value;
	}

	public FodotConstant getHeadValue() {
		return headValue;
	}

	public FodotConstant getLastValue() {
		return lastValue;
	}

	@Override
	public String toCode() {
		return getType().getName()
				+ " = "
				+ CollectionUtil.toDomain(headValue.toCode(),lastValue.toCode());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((headValue == null) ? 0 : headValue.hashCode());
		result = prime * result + ((lastValue == null) ? 0 : lastValue.hashCode());
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
		FodotNumericalTypeRangeEnumeration other = (FodotNumericalTypeRangeEnumeration) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (headValue == null) {
			if (other.headValue != null)
				return false;
		} else if (!headValue.equals(other.headValue))
			return false;
		if (lastValue == null) {
			if (other.lastValue != null)
				return false;
		} else if (!lastValue.equals(other.lastValue))
			return false;
		return true;
	}
		
}
