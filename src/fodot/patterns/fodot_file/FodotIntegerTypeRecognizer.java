package fodot.patterns.fodot_file;

import fodot.objects.file.IFodotFile;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.typeenum.FodotNumericalTypeRangeEnumeration;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.patterns.util.IntegerTypeUtil;

@Deprecated
public class FodotIntegerTypeRecognizer implements IFodotOptimizer {

	@Override
	public IFodotFile improve(IFodotFile file) {
		//Do it for all vocabularies
		for (IFodotElement el : file.getDirectElementsOfClass(FodotVocabulary.class)) {
			FodotVocabulary voc = (FodotVocabulary) el;

			//Find all types
			for (IFodotElement el2 : voc.getDirectElementsOfClass(FodotTypeDeclaration.class)) {
				FodotType type = ((FodotTypeDeclaration) el2).getType();

				// If it is integer already, ignore.
				if (!type.isRelatedTo(FodotType.INTEGER)
						//It should only contain converted integers
						&& IntegerTypeUtil.onlyContainsConvertedIntegers(type)) {

					FodotNumericalTypeRangeEnumeration range = IntegerTypeUtil.getRangeEnumeration(type);

					if (range != null) {
						//Set Integer as superclass for these types
						type.addSupertype(FodotType.INTEGER);


						for (IFodotElement el3 : file.getDirectFodotElements()) {
							if (el3 instanceof IFodotFileElement) {
								IFodotFileElement fileElement = (IFodotFileElement) el3;


								//All structures of this vocabulary has to add the range
								if (fileElement instanceof FodotStructure) {
									FodotStructure structure = (FodotStructure) fileElement;
									if (structure.getVocabulary().equals(voc)) {
										structure.addElement(range);
									}
								}

								//Check if it requires the vocabulary and update its constants of the type
								if (fileElement.getPrerequiredElements() != null
										&& fileElement.getPrerequiredElements().contains(voc)) {

									for (IFodotElement el4 : fileElement.getAllInnerElementsOfClass(FodotConstant.class)) {
										FodotConstant constant = (FodotConstant) el4;
										if (constant.getType().equals(type)) {
											constant.setValue(Integer.toString(IntegerTypeUtil.extractValue(constant)));
										}
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
}
