package fodot.objects.structure.enumerations;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.CollectionUtil;

public class FodotTypeEnumeration extends FodotEnumeration {
	public FodotType type;
	public List<FodotConstant> values;
	
	public FodotTypeEnumeration(FodotType type) {
		this(type, new ArrayList<FodotConstant>());
	}
	
	public FodotTypeEnumeration(FodotType type, List<FodotConstant> values) {
		super();
		this.type = type;
		this.values = values;
	}

	public FodotType getType() {
		return type;
	}

	/* VALUES */
	
	public void addValue(FodotConstant value) {
		values.add(value);
	}
	
	public void removeValue(String value) {
		values.remove(value);
	}
	
	public boolean containsValue(String value) {
		return values.contains(value);
	}
	
	public List<FodotConstant> getValues() {
		return new ArrayList<FodotConstant>(values);
	}

	@Override
	public String toCode() {
		return getType().getTypeName() + CollectionUtil.toDomain(CollectionUtil.toCode(getValues()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		FodotTypeEnumeration other = (FodotTypeEnumeration) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}
		
}
