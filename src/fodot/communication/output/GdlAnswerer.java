package fodot.communication.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.predicateenum.FodotPredicateEnumeration;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlAnswerer {

	private List<IdpModel> models;

	
	public GdlAnswerer(Collection<? extends IdpModel> models) {
		setModels(models);
	}

	/**********************************************
	 *  Models methods
	 ***********************************************/
	private void setModels(Collection<? extends IdpModel> argModels) {
		this.models = (isValidModels(argModels) ? new ArrayList<IdpModel>(argModels)
				: new ArrayList<IdpModel>());
	}

	private boolean isValidModels(Collection<? extends IdpModel> argModels) {
		return argModels != null;
	}

	public List<IdpModel> getModels() {
		return new ArrayList<IdpModel>(models);
	}

	public void addModel(IdpModel argModel) {
		this.models.add(argModel);
	}

	public void addAllModels(Collection<? extends IdpModel> argModels) {
		if (argModels != null) {
			this.models.addAll(argModels);
		}
	}

	public boolean containsModel(IdpModel model) {
		return this.models.contains(model);
	}

	public boolean hasModels() {
		return !models.isEmpty();
	}

	public void removeModel(IdpModel argModel) {
		this.models.remove(argModel);
	}

	public int getAmountOfModels() {
		return this.models.size();
	}
	/**********************************************/
	
	private static final String ACTION_PREDICATE_NAME = "do";
	
	public List<GdlAction> generateActionSequence() {
		IdpModel model = getBestModel();
		if (model == null) {
			return new ArrayList<GdlAction>();
		}
		FodotStructure struc = model.getStructure();
//		FodotVocabulary voc = model.getVocabulary();
		FodotPredicateEnumeration actionEnum = (FodotPredicateEnumeration) struc.getElementWithName(ACTION_PREDICATE_NAME);
		
		List<GdlAction> actions = new ArrayList<GdlAction>();
		for (IFodotPredicateEnumerationElement c : actionEnum.getElements()) {
			int time = Integer.valueOf(c.getElement(0).getValue());
			FodotConstant player = (FodotConstant) c.getElement(1);
			FodotConstant action = new FodotConstant(c.getElement(2).getValue(), new FodotType("null"));
			
			actions.add(new GdlAction(time, player, action));
		}
		return actions;
	}
	
	public IdpModel getBestModel() {
		if (getModels().isEmpty()) {
			return null;
		}
		return getModels().get(0);
	}

}
