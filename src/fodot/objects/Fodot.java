package fodot.objects;

import fodot.objects.procedure.FodotProcedure;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotTheory;
import fodot.objects.vocabulary.FodotVocabulary;

public class Fodot {
	private FodotVocabulary vocabulary;
	private FodotTheory theory;
	private FodotStructure structure;
	private FodotProcedure procedure;
	
	public Fodot(FodotVocabulary vocabulary, FodotTheory theory,
			FodotStructure structure, FodotProcedure procedure) {
		super();
		this.vocabulary = vocabulary;
		this.theory = theory;
		this.structure = structure;
		this.procedure = procedure;
	}
	
	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	public FodotTheory getTheory() {
		return theory;
	}
	public FodotStructure getStructure() {
		return structure;
	}
	public FodotProcedure getProcedure() {
		return procedure;
	}	
	
}
