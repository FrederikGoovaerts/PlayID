package fodot.objects.general;

import java.util.Comparator;

import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;

public class FodotElementComparators {
	public static final Comparator<FodotVariable> VARIABLE_NAME_COMPARATOR = new Comparator<FodotVariable>() {
		@Override
		public int compare(FodotVariable o1, FodotVariable o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	public static final Comparator<FodotConstant> CONSTANT_VALUE_COMPARATOR = new Comparator<FodotConstant>() {
		@Override
		public int compare(FodotConstant o1, FodotConstant o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};
	
	public static final Comparator<IFodotElement> ELEMENT_TOCODE_COMPARATOR = new Comparator<IFodotElement>() {
		@Override
		public int compare(IFodotElement o1, IFodotElement o2) {
			return o1.toCode().trim().compareTo(o2.toCode().trim());
		}
	};
	
	public static final Comparator<IFodotEnumerationElement> ENUMERATION_ELEMENT_COMPARATOR = new Comparator<IFodotEnumerationElement>() {
		@Override
		public int compare(IFodotEnumerationElement o1, IFodotEnumerationElement o2) {
			return o1.getValue().trim().compareTo(o2.getValue().trim());
		}
	};
	
}
