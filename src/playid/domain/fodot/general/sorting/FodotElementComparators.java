package playid.domain.fodot.general.sorting;

import java.util.Comparator;

import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.structure.elements.IFodotEnumerationElement;
import playid.domain.fodot.theory.elements.terms.FodotConstant;
import playid.domain.fodot.theory.elements.terms.FodotVariable;

public class FodotElementComparators {
	
	private static final Comparator<String> alphanumComp = new AlphanumComparator();
	
	public static final Comparator<FodotVariable> VARIABLE_NAME_COMPARATOR = new Comparator<FodotVariable>() {
		@Override
		public int compare(FodotVariable o1, FodotVariable o2) {
			return alphanumComp.compare(o1.getName(), o2.getName());
		}
	};
	
	public static final Comparator<FodotConstant> CONSTANT_VALUE_COMPARATOR = new Comparator<FodotConstant>() {
		@Override
		public int compare(FodotConstant o1, FodotConstant o2) {
			return alphanumComp.compare(o1.getValue(), o2.getValue());
		}
	};
	
	public static final Comparator<IFodotElement> ELEMENT_TOCODE_COMPARATOR = new Comparator<IFodotElement>() {
		@Override
		public int compare(IFodotElement o1, IFodotElement o2) {
			return alphanumComp.compare(o1.toCode().trim(), o2.toCode().trim());
		}
	};
	
	public static final Comparator<IFodotEnumerationElement> ENUMERATION_ELEMENT_COMPARATOR = new Comparator<IFodotEnumerationElement>() {
		@Override
		public int compare(IFodotEnumerationElement o1, IFodotEnumerationElement o2) {
			return alphanumComp.compare( o1.getValue().trim(), o2.getValue().trim());
		}
	};
	
}
