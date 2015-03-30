package playid.domain.patterns.gdl_vocabulary;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.gdl.grammar.GdlConstant;

import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.structure.elements.IFodotStructureElement;
import playid.domain.fodot.structure.elements.typeenum.FodotNumericalTypeRangeEnumeration;
import playid.domain.fodot.theory.elements.terms.FodotConstant;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.gdl_transformers.GdlVocabulary;
import playid.util.IntegerTypeUtil;

public class GdlIntegerTypeRecognizer implements IGdlVocabularyOptimizer {

	@Override
	public GdlVocabulary improve(GdlVocabulary voc) {
		
		//TODO eventueel moeten we nieuwe types aanmaken en zo de oude gegeven GDL vocabulary onveranderd laten.
		// Dan moeten wel weer alle types overal geupdate worden en dat is momenteel niet interessant gezien de oude voc weggegooid wordt.
		
		Set<IFodotStructureElement> structureElements = new LinkedHashSet<>(voc.getStructureElements());
		Map<GdlConstant, Map<FodotType, FodotConstant>> oldConstants = new HashMap<>(voc.getConstants());
		
		//Find all types
		for (FodotType type : voc.getOtherTypes()) {

			// If it is integer already, ignore.
			if (!type.isRelatedTo(FodotType.INTEGER)
					//It should only contain converted integers
					&& IntegerTypeUtil.onlyContainsConvertedIntegers(type)) {

				FodotNumericalTypeRangeEnumeration range = IntegerTypeUtil.getRangeEnumeration(type);
				
				if (range != null) {
					//Set Integer as superclass for these types
					type.addSupertype(FodotType.INTEGER);
					structureElements.add(range);

					//Replace occurrences in constants list
					for (GdlConstant gdlConst : oldConstants.keySet()) {
						Map<FodotType, FodotConstant> map = oldConstants.get(gdlConst);
						if (map.containsKey(type)) {
							FodotConstant oldConstant  = map.get(type);
							map.put(type, new FodotConstant(Integer.toString(IntegerTypeUtil.extractValue(oldConstant)),type));									
						}
					}
					
					
					//Replace in domain of type: shouldn't be visible, but is safer I suppose
					for (IFodotElement el : type.getAllInnerElementsOfClass(FodotConstant.class)) {
						FodotConstant constant = (FodotConstant) el;
						if (constant.getType().equals(type)) {
							type.removeDomainElement(constant.toDomainElement());
							type.addDomainElement(new FodotConstant(Integer.toString(IntegerTypeUtil.extractValue(constant)),type).toDomainElement());
						}
					}
					
					
				}

			}
		}
		


		GdlVocabulary newVocabulary =
				new GdlVocabulary(
						voc.getTimeType(), voc.getPlayerType(), voc.getActionType(), voc.getScoreType(),
						voc.getOtherTypes(),
						voc.getConstants(), voc.getVariablesPerRule(),
						voc.getFunctionDeclarations(), voc.getPredicateDeclarations(), voc.getPropositions(),
						voc.getGdlDynamicPredicates(), structureElements);
		
		
		return newVocabulary;		
	}

}
