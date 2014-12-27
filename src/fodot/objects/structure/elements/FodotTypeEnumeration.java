package fodot.objects.structure.elements;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;
import fodot.util.CollectionPrinter;

public class FodotTypeEnumeration extends FodotEnumeration {
	private FodotType type;
	private List<IFodotEnumerationElement> values;
	
	public FodotTypeEnumeration(FodotType type) {
		this(type, new ArrayList<IFodotEnumerationElement>());
	}
	
	public FodotTypeEnumeration(FodotType type, List<? extends IFodotEnumerationElement> values) {
		super();
		this.type = type;
		this.values = new ArrayList<IFodotEnumerationElement>(values);
	}

	public FodotType getType() {
		return type;
	}

	/* VALUES */
	
	public void addValue(IFodotEnumerationElement value) {
		values.add(value);
	}
	
	public void removeValue(String value) {
		values.remove(value);
	}
	
	public boolean containsValue(String value) {
		return values.contains(value);
	}
	
	public List<IFodotEnumerationElement> getValues() {
		return new ArrayList<IFodotEnumerationElement>(values);
	}

	@Override
	public String toCode() {
		return getType().getName() + " = " + CollectionPrinter.toDomain(CollectionPrinter.toCode(getValues()));
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

	@Override
	public IFodotVocabularyElement getDeclaration() {
		return getType().getDeclaration();
	}
		
}
