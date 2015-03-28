package fodot.communication.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ggp.base.util.gdl.grammar.GdlTerm;

import fodot.communication.gdloutput.IFodotGdlTranslator;
import fodot.communication.output.MoveSequence.MoveSequenceBuilder;
import fodot.gdl_parser.FodotGameFactory;
import fodot.gdl_parser.second_phase.GdlFodotTransformer;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.functionenum.FodotFunctionEnumeration;
import fodot.objects.structure.elements.functionenum.elements.FodotFunctionEnumerationElement;
import fodot.objects.structure.elements.predicateenum.FodotPredicateEnumeration;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import fodot.util.IntegerTypeUtil;

public class GdlAnswerCalculator {

	private List<FodotStructure> models;
	private IFodotGdlTranslator translator;
	private GdlFodotTransformer transformer;

	public GdlAnswerCalculator(GdlFodotTransformer transformer, IFodotGdlTranslator translator, Collection<? extends FodotStructure> models) {
		this.transformer = transformer;
		setTranslator(translator);
		setModels(models);
	}

	
	/**********************************************
	 *  Translator
	 ***********************************************/

	public IFodotGdlTranslator getTranslator() {
		return translator;
	}

	public void setTranslator(IFodotGdlTranslator translator) {
		this.translator = translator;
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
		int maximumScore = IntegerTypeUtil.extractValue(transformer.getMaximumScore());
		
		FodotStructure bestModel = getBestModel();
		if (bestModel == null) {
			return new GdlActions(new ArrayList<GdlAction>(), 0, maximumScore);
		}
		FodotPredicateEnumeration actionEnum = (FodotPredicateEnumeration) bestModel.getElementWithName(ACTION_PREDICATE_NAME);
		FodotFunctionEnumeration scoreEnum = (FodotFunctionEnumeration) bestModel.getElementWithName(SCORE_FUNCTION_NAME);
		
		int score = 0;
		for (FodotFunctionEnumerationElement el : scoreEnum.getElements()) {
			if (el.getElement(0).equals(transformer.getOwnRole().toEnumerationElement())) {//Find player 
				score = Integer.parseInt(el.getReturnValue().toTerm().toCode());
			}
		}
		
		List<GdlAction> actions = new ArrayList<GdlAction>();
		MoveSequenceBuilder moveSeqBuilder = MoveSequence.createBuilder();
		for (IFodotPredicateEnumerationElement c : actionEnum.getElements()) {
			int time = Integer.valueOf(c.getElement(0).getValue());
			IFodotTypeEnumerationElement player = c.getElement(1);
			IFodotTypeEnumerationElement action = c.getElement(2);
					

			GdlTerm gdlPlayer = getTranslator().translate(player);
			GdlTerm gdlAction = getTranslator().translate(action);
			
			moveSeqBuilder.addMove(time, gdlPlayer, gdlAction);
			actions.add(new GdlAction(getTranslator(), time, player, action));
		}
//		return moveSeqBuilder.buildMoveSequence();
		return new GdlActions(actions, score, maximumScore);
	}
	
	
	public FodotStructure getBestModel() {
		if (getModels().isEmpty()) {
			return null;
		}
		return getModels().get(0);
	}

}
