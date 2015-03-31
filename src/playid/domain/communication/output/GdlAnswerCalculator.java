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
import playid.util.IntegerTypeUtil;

public class GdlAnswerCalculator {

	private List<FodotStructure> models;
	private GdlVocabulary vocabulary;
	private Role role;

	public GdlAnswerCalculator(Role role, GdlVocabulary vocabulary, Collection<? extends FodotStructure> models) {
		this.role = role;
		setVocabulary(vocabulary);
		setModels(models);
	}

	
	/**********************************************
	 *  Translator
	 ***********************************************/

	public GdlVocabulary getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(GdlVocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}	

	/**********************************************/
	
	
	/**********************************************
	 *  Models methods
	 ***********************************************/
	private void setModels(Collection<? extends FodotStructure> argModels) {
		this.models = (isValidModels(argModels) ? new ArrayList<FodotStructure>(argModels)
				: new ArrayList<FodotStructure>());
	}

	private boolean isValidModels(Collection<? extends FodotStructure> argModels) {
		return argModels != null;
	}

	public List<FodotStructure> getModels() {
		return new ArrayList<FodotStructure>(models);
	}

	public void addModel(FodotStructure argModel) {
		this.models.add(argModel);
	}

	public void addAllModels(Collection<? extends FodotStructure> argModels) {
		if (argModels != null) {
			this.models.addAll(argModels);
		}
	}

	public boolean containsModel(FodotStructure model) {
		return this.models.contains(model);
	}

	public boolean hasModels() {
		return !models.isEmpty();
	}

	public void removeModel(FodotStructure argModel) {
		this.models.remove(argModel);
	}

	public int getAmountOfModels() {
		return this.models.size();
	}
	/**********************************************/

	private static final String ACTION_PREDICATE_NAME = GdlFodotTransformer.ACTION_PREDICATE_NAME;
	private static final String SCORE_FUNCTION_NAME = FodotGameFactory.SCORE_FUNCTION_NAME;
	
	public GdlActions generateActionSequence() {		
		FodotStructure bestModel = getBestModel();
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
		MoveSequenceBuilder moveSeqBuilder = MoveSequence.createBuilder();
		for (IFodotPredicateEnumerationElement c : actionEnum.getElements()) {
			int time = Integer.valueOf(c.getElement(0).getValue());
			IFodotTypeEnumerationElement player = c.getElement(1);
			IFodotTypeEnumerationElement action = c.getElement(2);
					

			GdlTerm gdlPlayer = getVocabulary().translate(player);
			GdlTerm gdlAction = getVocabulary().translate(action);
			
			moveSeqBuilder.addMove(time, gdlPlayer, gdlAction);
			actions.add(new GdlAction(getVocabulary(), time, player, action));
		}
//		return moveSeqBuilder.buildMoveSequence();
		return new GdlActions(actions, score);
	}
	
	
	public FodotStructure getBestModel() {
		if (getModels().isEmpty()) {
			return null;
		}
		return getModels().get(0);
	}

}
