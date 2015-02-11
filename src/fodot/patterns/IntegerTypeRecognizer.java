package fodot.patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fodot.objects.file.IFodotFile;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.typeenum.FodotNumericalTypeRangeEnumeration;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.IFodotDomainElement;
import fodot.util.NameUtil;

public class IntegerTypeRecognizer implements IFodotOptimizer {

	@Override
	public IFodotFile improve(IFodotFile file) {
		//Do it for all vocabularies
		for (IFodotElement el : file.getDirectElementsOfClass(FodotVocabulary.class)) {

			FodotVocabulary voc = (FodotVocabulary) el;

			//Find all types
			for (IFodotElement el2 : voc.getDirectElementsOfClass(FodotType.class)) {
				FodotType type = (FodotType) el2;

				// If it is integer already, ignore.
				if (!type.isRelatedTo(FodotType.INTEGER)
						//It should only contain converted integers
						&& onlyContainsConvertedIntegers(type)) {

					FodotNumericalTypeRangeEnumeration range = getRangeEnumeration(type);

					if (range != null) {
						//Set Integer as superclass for these types
						type.addSupertype(FodotType.INTEGER);


						for (IFodotElement el3 : file.getDirectFodotElements()) {
							IFodotFileElement fileElement = (IFodotFileElement) el3;
							

							//All structures of this vocabulary has to add the range
							if (fileElement instanceof FodotStructure) {
								FodotStructure structure = (FodotStructure) fileElement;
								if (structure.getVocabulary().equals(voc)) {
									structure.addElement(range);
								}
							}
							
							//Check if it requires the vocabulary and update its constants of the type
							if (fileElement.getPrerequiredElements().contains(voc)) {
								for (IFodotElement el4 : fileElement.getAllInnerElementsOfClass(FodotConstant.class)) {
									FodotConstant constant = (FodotConstant) el4;
									if (constant.getType().equals(type)) {
										constant.setValue(Integer.toString(extractValue(constant)));
									}
								}
							}
						}
					}

				}
			}
		}
		return file;		
	}


	/**
	 * Checks if all domainelements of a type only consists of FodotConstants that are converted integers
	 * @param type
	 * @return
	 */
	private boolean onlyContainsConvertedIntegers(FodotType type) {
		for (IFodotDomainElement dom : type.getDomainElements()) {
			if (dom instanceof FodotConstant) {
				FodotConstant constant = (FodotConstant) dom;
				if (!constant.getValue().startsWith("i_")) {
					return false;
				}
			} else {
				//It contains a non-constant: not a integer!
				return false;
			}
		}
		return true;
	}

	private FodotNumericalTypeRangeEnumeration getRangeEnumeration(FodotType type) {
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

	private int extractValue(FodotConstant constant) {
		return NameUtil.convertConstantNameToInteger(constant.getValue());
	}

	private boolean containsOnlySequentialNumbers(List<Integer> values) {
		for (int i = 0; i < values.size() - 1; i++) {
			if (values.get(i) != values.get(i+1) - 1) {
				return false;
			}
		}		
		return true;
	}
}
