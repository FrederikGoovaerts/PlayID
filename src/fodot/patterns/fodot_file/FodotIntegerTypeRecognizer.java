package fodot.patterns.fodot_file;


//@Deprecated
//public class FodotIntegerTypeRecognizer implements IFodotOptimizer {
//
//	@Override
//	public IFodotFile improve(IFodotFile file) {
//		//Do it for all vocabularies
//		for (IFodotElement el : file.getDirectElementsOfClass(FodotVocabulary.class)) {
//			FodotVocabulary voc = (FodotVocabulary) el;
//
//			//Find all types
//			for (IFodotElement el2 : voc.getDirectElementsOfClass(FodotTypeDeclaration.class)) {
//				FodotType type = ((FodotTypeDeclaration) el2).getType();
//
//				// If it is integer already, ignore.
//				if (!type.isRelatedTo(FodotType.INTEGER)
//						//It should only contain converted integers
//						&& IntegerTypeUtil.onlyContainsConvertedIntegers(type)) {
//
//					FodotNumericalTypeRangeEnumeration range = IntegerTypeUtil.getRangeEnumeration(type);
//
//					if (range != null) {
//						//Set Integer as superclass for these types
//						type.addSupertype(FodotType.INTEGER);
//
//
//						for (IFodotElement el3 : file.getDirectFodotElements()) {
//							if (el3 instanceof IFodotFileElement) {
//								IFodotFileElement fileElement = (IFodotFileElement) el3;
//
//
//								//All structures of this vocabulary has to add the range
//								if (fileElement instanceof FodotStructure) {
//									FodotStructure structure = (FodotStructure) fileElement;
//									if (structure.getVocabulary().equals(voc)) {
//										structure.addElement(range);
//									}
//								}
//
//								//Check if it requires the vocabulary and update its constants of the type
//								if (fileElement.getPrerequiredElements() != null
//										&& fileElement.getPrerequiredElements().contains(voc)) {
//
//									for (IFodotElement el4 : fileElement.getAllInnerElementsOfClass(FodotInteger.class)) {
//										FodotInteger constant = (FodotInteger) el4;
//										if (constant.getType().equals(type)) {
//											constant.setValue(Integer.toString(IntegerTypeUtil.extractValue(constant)));
//										}
//									}
//								}
//							}
//						}
//					}
//
//				}
//			}
//		}
//		return file;		
//	}
//}
