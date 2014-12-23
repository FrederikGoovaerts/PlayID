package fodot.communication.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.enumerations.FodotPredicateEnumeration;
import fodot.objects.vocabulary.FodotVocabulary;

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
		FodotStructure struc = model.getStructure();
		FodotVocabulary voc = model.getVocabulary();
		FodotPredicateEnumeration actionEnum = (FodotPredicateEnumeration) struc.getElementWithName(ACTION_PREDICATE_NAME);
		
		List<GdlAction> actions = new ArrayList<GdlAction>();
		for (FodotConstant[] c : actionEnum.getValues()) {
			int time = Integer.valueOf(c[0].getValue());
			FodotConstant player = c[1];
			FodotConstant action = c[2];
			
			actions.add(new GdlAction(time, player,action));
		}
		return actions;
	}
	
	public IdpModel getBestModel() {
		return getModels().get(0);
	}

}
