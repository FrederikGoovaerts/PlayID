package fodot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fodot.objects.structure.elements.typeenum.FodotNumericalTypeRangeEnumeration;
import fodot.objects.structure.elements.typeenum.elements.FodotInteger;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.objects.vocabulary.elements.IFodotDomainElement;

public class IntegerTypeUtil {
	
	public static final String INTEGER_PREFIX = "i_";
	public static final String INTEGER_REGEX = "^[0-9]+$";
	
	/**
	 * Checks if all domainelements of a type only consists of FodotConstants that are converted integers
	 * @param type
	 * @return
	 */
	public static boolean onlyContainsConvertedIntegers(FodotType type) {
		for (IFodotDomainElement dom : type.getDomainElements()) {
			if (dom instanceof FodotTypeFunctionDeclaration) {
				FodotTypeFunctionDeclaration domainElement = (FodotTypeFunctionDeclaration) dom;
				if (!domainElement.isConstant() || !domainElement.getName().startsWith(INTEGER_PREFIX)) {
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
			values.add(extractValue((FodotTypeFunctionDeclaration)dom));	
		}

		Collections.sort(values);

		if (!containsOnlySequentialNumbers(values)) {
			return null;
		}

		int head = values.get(0);
		int last = values.get(values.size()-1);

		type.addSupertype(FodotType.INTEGER);
		
		return new FodotNumericalTypeRangeEnumeration(type, new FodotConstant(Integer.toString(head), type), new FodotConstant(Integer.toString(last), type));
	}

	public static int extractValue(FodotConstant constant) {
		return NameUtil.convertConstantNameToInteger(constant.getValue());
	}
	
	public static int extractValue(FodotInteger constant) {
		return Integer.parseInt(constant.getValue());
	}
	
	public static int extractValue(FodotTypeFunctionDeclaration domainElement) {
		return NameUtil.convertConstantNameToInteger(domainElement.getName());
	}
	

	public static boolean containsOnlySequentialNumbers(List<Integer> values) {
		for (int i = 0; i < values.size() - 1; i++) {
			if (values.get(i) != values.get(i+1) - 1) {
				return false;
			}
		}		
		return true;
	}
	
	public static FodotInteger getMaximum(List<FodotInteger> constants) {
		if (constants.size() == 0) {
			return null;
		}
		
		FodotInteger max = constants.get(0);
		for (FodotInteger c : constants) {
			if (Integer.parseInt(c.getValue()) > Integer.parseInt(max.getValue())) {
				max = c;
			}
		}
		
		return max;
	}
	
	public static List<FodotInteger> getIntegers(Collection<? extends IFodotDomainElement> domainElements) {
		List<FodotInteger> constants = new ArrayList<>();
		for (IFodotDomainElement el : domainElements) {
			if (el instanceof FodotInteger) {
				constants.add((FodotInteger)el);
			}
		}
		return constants;
	}
	
	public static boolean isInteger(FodotConstant constant) {
		return constant.getValue().matches(INTEGER_REGEX);
	}
}
