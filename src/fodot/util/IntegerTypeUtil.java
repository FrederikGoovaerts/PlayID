package fodot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fodot.objects.structure.elements.typeenum.FodotNumericalTypeRangeEnumeration;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.IFodotDomainElement;

public class IntegerTypeUtil {
	
	public static final String INTEGER_PREFIX = "i_";
	
	/**
	 * Checks if all domainelements of a type only consists of FodotConstants that are converted integers
	 * @param type
	 * @return
	 */
	public static boolean onlyContainsConvertedIntegers(FodotType type) {
		for (IFodotDomainElement dom : type.getDomainElements()) {
			if (dom instanceof FodotConstant) {
				FodotConstant constant = (FodotConstant) dom;
				if (!constant.getValue().startsWith(INTEGER_PREFIX)) {
					return false;
				}
			} else {
				//It contains a non-constant: not a integer!
				return false;
			}
		}
		return true;
	}

	public static FodotNumericalTypeRangeEnumeration getRangeEnumeration(FodotType type) {
		if (!onlyContainsConvertedIntegers(type) || type.getDomainElements().isEmpty()) {
			return null;
		}

		List<Integer> values = new ArrayList<Integer>();
		for (IFodotDomainElement dom : type.getDomainElements()) {
			values.add(extractValue((FodotConstant)dom));	
		}

		Collections.sort(values);

		if (!containsOnlySequentialNumbers(values)) {
			return null;
		}

		int head = values.get(0);
		int last = values.get(values.size()-1);

		return new FodotNumericalTypeRangeEnumeration(type, new FodotConstant(Integer.toString(head), type), new FodotConstant(Integer.toString(last), type));
	}

	public static int extractValue(FodotConstant constant) {
		return NameUtil.convertConstantNameToInteger(constant.getValue());
	}

	public static boolean containsOnlySequentialNumbers(List<Integer> values) {
		for (int i = 0; i < values.size() - 1; i++) {
			if (values.get(i) != values.get(i+1) - 1) {
				return false;
			}
		}		
		return true;
	}
	
	public static FodotConstant getMaximum(List<FodotConstant> constants) {
		if (constants.size() == 0) {
			return null;
		}
		
		FodotConstant max = constants.get(0);
		for (FodotConstant c : constants) {
			if (extractValue(c) > extractValue(max)) {
				max = c;
			}
		}
		
		return max;
	}
	
	public static List<FodotConstant> getConstants(Collection<? extends IFodotDomainElement> domainElements) {
		List<FodotConstant> constants = new ArrayList<FodotConstant>();
		for (IFodotDomainElement el : domainElements) {
			if (el instanceof FodotConstant) {
				constants.add((FodotConstant)el);
			}
		}
		return constants;
	}
}
