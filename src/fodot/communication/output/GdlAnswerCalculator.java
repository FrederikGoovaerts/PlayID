package fodot.communication.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.communication.gdloutput.IFodotGdlTranslator;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.predicateenum.FodotPredicateEnumeration;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;

public class GdlAnswerCalculator {

	private List<FodotStructure> models;
	private IFodotGdlTranslator translator;

	public GdlAnswerCalculator(IFodotGdlTranslator translator, Collection<? extends FodotStructure> models) {
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
	
	private static final String ACTION_PREDICATE_NAME = "does";
	
	public List<GdlAction> generateActionSequence() {
		FodotStructure bestModel = getBestModel();
		if (bestModel == null) {
			return new ArrayList<GdlAction>();
		}
		FodotPredicateEnumeration actionEnum = (FodotPredicateEnumeration) bestModel.getElementWithName(ACTION_PREDICATE_NAME);
		
		List<GdlAction> actions = new ArrayList<GdlAction>();
		for (IFodotPredicateEnumerationElement c : actionEnum.getElements()) {
			int time = Integer.valueOf(c.getElement(0).getValue());
			IFodotTypeEnumerationElement player = c.getElement(1);
			IFodotTypeEnumerationElement action = c.getElement(2);
			
			//TODO: use GdlPredicate instead of own GDL action
			actions.add(new GdlAction(getTranslator(), time, player, action));
		}
		return actions;
	}
	
	public FodotStructure getBestModel() {
		if (getModels().isEmpty()) {
			return null;
		}
		return getModels().get(0);
	}

}
