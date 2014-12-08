package fodot.objects;

import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotTheory;
import fodot.objects.vocabulary.FodotVocabulary;

public class Fodot {
	private FodotVocabulary vocabulary;
	private FodotTheory theory;
	private FodotStructure structure;
	private FodotProcedures procedures;
	
	public Fodot(FodotVocabulary vocabulary, FodotTheory theory,
			FodotStructure structure, FodotProcedures procedures) {
		super();
		this.vocabulary = vocabulary;
		this.theory = theory;
		this.structure = structure;
		this.procedures = procedures;
	}
	
	public static Fodot getNewEmptyFodot() {
		FodotVocabulary voc = new FodotVocabulary();
		return new Fodot(voc, new FodotTheory(voc), new FodotStructure(voc), new FodotProcedures("main"));
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
	public FodotProcedures getProcedures() {
		return procedures;
	}	
	
//	public void merge(Fodot other) {
//		getVocabulary().merge(other.getVocabulary());
//		getTheory().merge(other.getTheory());
//		getStructure().merge(other.getStructure());
//		getProcedures().merge(other.getProcedures());
//	}
	
}
