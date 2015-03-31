package playid.domain.communication.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.output.MoveSequence.MoveSequenceBuilder;
import playid.domain.fodot.structure.FodotStructure;
import playid.domain.fodot.structure.elements.functionenum.FodotFunctionEnumeration;
import playid.domain.fodot.structure.elements.functionenum.elements.FodotFunctionEnumerationElement;
import playid.domain.fodot.structure.elements.predicateenum.FodotPredicateEnumeration;
import playid.domain.fodot.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.gdl_transformers.FodotGameFactory;
import playid.domain.gdl_transformers.GdlVocabulary;
import playid.domain.gdl_transformers.second_phase.GdlFodotTransformer;

public class GdlAnswerCalculator {

	private final GdlVocabulary vocabulary;
	private final Role role;

	public GdlAnswerCalculator(Role role, GdlVocabulary vocabulary) {
		this.role = role;
		this.vocabulary = vocabulary;
	}


	/**********************************************
	 *  Translator
	 ***********************************************/

	public GdlVocabulary getVocabulary() {
		return vocabulary;
	}

	/**********************************************/

	/**********************************************/

	private static final String ACTION_PREDICATE_NAME = GdlFodotTransformer.ACTION_PREDICATE_NAME;
	private static final String SCORE_FUNCTION_NAME = FodotGameFactory.SCORE_FUNCTION_NAME;

	@Deprecated
	public GdlActions generateActionSequence(Collection<? extends FodotStructure> models) {		
		FodotStructure bestModel = getBestModel(models);
		if (bestModel == null) {
			return new GdlActions(new ArrayList<GdlAction>(), 0);
		}
		FodotPredicateEnumeration actionEnum = (FodotPredicateEnumeration) bestModel.getElementWithName(ACTION_PREDICATE_NAME);
		FodotFunctionEnumeration scoreEnum = (FodotFunctionEnumeration) bestModel.getElementWithName(SCORE_FUNCTION_NAME);

		int score = 0;
		for (FodotFunctionEnumerationElement el : scoreEnum.getElements()) {
			if (el.getElement(0).equals(vocabulary.getConstant(role.getName(), vocabulary.getPlayerType()).toEnumerationElement())) {//Find player 
				score = Integer.parseInt(el.getReturnValue().toTerm().toCode());
			}
		}

		List<GdlAction> actions = new ArrayList<GdlAction>();
		for (IFodotPredicateEnumerationElement c : actionEnum.getElements()) {
			int time = Integer.valueOf(c.getElement(0).getValue());
			IFodotTypeEnumerationElement player = c.getElement(1);
			IFodotTypeEnumerationElement action = c.getElement(2);

			actions.add(new GdlAction(getVocabulary(), time, player, action));
		}
		return new GdlActions(actions, score);
	}

	public MoveSequence extractMoveSequence(FodotStructure model) {		
		FodotPredicateEnumeration actionEnum = (FodotPredicateEnumeration) model.getElementWithName(ACTION_PREDICATE_NAME);
		

		MoveSequenceBuilder moveSeqBuilder = MoveSequence.createBuilder();
		for (IFodotPredicateEnumerationElement c : actionEnum.getElements()) {
			int time = Integer.valueOf(c.getElement(0).getValue());
			IFodotTypeEnumerationElement player = c.getElement(1);
			IFodotTypeEnumerationElement action = c.getElement(2);


			GdlTerm gdlPlayer = getVocabulary().translate(player);
			GdlTerm gdlAction = getVocabulary().translate(action);

			moveSeqBuilder.addMove(time, gdlPlayer, gdlAction);
		}
		return moveSeqBuilder.buildMoveSequence();
	}

	public int getScoreOf(Role role, FodotStructure model) {
		FodotFunctionEnumeration scoreEnum = (FodotFunctionEnumeration) model.getElementWithName(SCORE_FUNCTION_NAME);

		int score = -1;
		for (FodotFunctionEnumerationElement el : scoreEnum.getElements()) {
			if (el.getElement(0).equals(vocabulary.getConstant(role.getName(), vocabulary.getPlayerType()).toEnumerationElement())) {//Find player 
				score = Integer.parseInt(el.getReturnValue().toTerm().toCode());
			}
		}
		return score;
	}
	
	public FodotStructure getBestModel(Collection<? extends FodotStructure> models) {
		if (models.isEmpty()) {
			return null;
		}
		return models.iterator().next();
	}

}
