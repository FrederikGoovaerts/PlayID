package fodot.communication.output;

import fodot.objects.structure.FodotStructure;
import fodot.objects.vocabulary.FodotVocabulary;

public class IdpModel {

	private FodotStructure structure;
	private FodotVocabulary vocabulary;

	public IdpModel(FodotVocabulary vocabulary) {
		super();
		this.vocabulary = vocabulary;
		this.structure = new FodotStructure(vocabulary);
	}

	public FodotStructure getStructure() {
		return structure;
	}

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}

}
