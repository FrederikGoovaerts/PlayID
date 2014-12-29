package fodot.objects.structure.elements.functionenum;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fodot.objects.structure.elements.FodotEnumeration;
import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.structure.elements.functionenum.elements.IFodotFunctionEnumerationElement;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.util.CollectionPrinter;

public class FodotFunctionEnumeration extends FodotEnumeration<IFodotFunctionEnumerationElement> {
	private Map<IFodotEnumerationElement[], IFodotEnumerationElement> values;

	public FodotFunctionEnumeration(FodotFunctionDeclaration declaration, Collection<? extends IFodotFunctionEnumerationElement> elements) {
		super(declaration, elements);
	}
	
	@Deprecated
	public FodotFunctionEnumeration(FodotFunctionDeclaration declaration, Map<IFodotEnumerationElement[], IFodotEnumerationElement> values) {
		super(declaration);
		this.values = values;
	}

	public FodotFunctionEnumeration(FodotFunctionDeclaration function) {
		this(function, new HashMap<IFodotEnumerationElement[], IFodotEnumerationElement>());
	}

	@Override
	public FodotFunctionDeclaration getDeclaration() {
		return (FodotFunctionDeclaration) super.getDeclaration();
	}

	
	/**********************************************
	 *  Values
	 ***********************************************/
	@Deprecated
	public void addValue(IFodotEnumerationElement[] input, IFodotEnumerationElement value) {
		//TODO: some kind of validator for the amount of and type of arguments
		values.put(input, value);
	}

	@Deprecated
	public void removeValue(IFodotEnumerationElement[] input) {
		values.remove(input);
	}

	@Deprecated
	public boolean containsValue(IFodotEnumerationElement[] input) {
		return values.containsKey(input);
	}

	@Deprecated
	public Map<IFodotEnumerationElement[], IFodotEnumerationElement> getValues() {
		return new HashMap<IFodotEnumerationElement[], IFodotEnumerationElement>(values);
	}

	@Deprecated
	public boolean hasValues() {
		return !values.isEmpty();
	}

	/**********************************************/


	//SENTENCE ELEMENT

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(getDeclaration().getName() + " = ");
		builder.append(CollectionPrinter.toDomain(CollectionPrinter.toCode(getElements())));
//		List<IFodotEnumerationElement[]> keys = new ArrayList<IFodotEnumerationElement[]>(getValues().keySet());
//		for (int i = 0; i < keys.size(); i++) {
//			if (i > 0) {
//				builder.append(";");
//			}
//			IFodotEnumerationElement[] key = keys.get(i);
//			builder.append(CollectionPrinter.toNakedList(CollectionPrinter.toCode(Arrays.asList(key))) + " -> " + values.get(key).toCode());
//		}
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getDeclaration() == null) ? 0 : getDeclaration().hashCode());
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
		FodotFunctionEnumeration other = (FodotFunctionEnumeration) obj;
		if (getDeclaration() == null) {
			if (other.getDeclaration() != null)
				return false;
		} else if (!getDeclaration().equals(other.getDeclaration()))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FodotFunctionEnumeration ["+toCode()+ "]";
	}



}