package fodot.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import fodot.objects.file.IFodotFile;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotTheory;
import fodot.objects.vocabulary.FodotVocabulary;

public class Fodot implements IFodotFile {
	private FodotIncludeHolder includes;
	private FodotVocabulary vocabulary;
	private FodotTheory theory;
	private FodotStructure structure;
	private FodotProcedures procedures;

	public Fodot(FodotVocabulary vocabulary, FodotTheory theory,
			FodotStructure structure, FodotProcedures procedures, FodotIncludeHolder includes) {
		super();
		setVocabulary(vocabulary);
		setTheory(theory);
		setStructure(structure);
		setProcedures(procedures);
		setIncludes(includes);
	}

	public Fodot(FodotVocabulary vocabulary, FodotTheory theory,
			FodotStructure structure, FodotProcedures procedures) {
		this(vocabulary, theory, structure, procedures, new FodotIncludeHolder());
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

	public FodotIncludeHolder getIncludes() {
		return includes;
	}

	private void setVocabulary(FodotVocabulary vocabulary) {
		this.vocabulary = (vocabulary == null ? new FodotVocabulary() : vocabulary);
	}

	private void setTheory(FodotTheory theory) {
		this.theory = (theory == null? new FodotTheory(getVocabulary()) : theory);
	}

	private void setStructure(FodotStructure structure) {
		this.structure = (structure == null? new FodotStructure(getVocabulary()) : structure);
	}

	private void setProcedures(FodotProcedures procedures) {
		this.procedures = (procedures == null? new FodotProcedures() : procedures);
	}

	private void setIncludes(FodotIncludeHolder includes) {
		this.includes = (includes == null ? new FodotIncludeHolder() : includes);
	}

	//FODOT ELEMENT
	@Override
	public String toCode() {
		return getIncludes().toCode() + "\n"
				+ getVocabulary().toCode() + "\n"
				+ getTheory().toCode() + "\n"
				+ getStructure().toCode() + "\n"
				+ getProcedures().toCode() + "\n";
	}	

	public void merge(Fodot other) {
		getIncludes().mergeWith(other.getIncludes());
		getVocabulary().mergeWith(other.getVocabulary());
		getTheory().mergeWith(other.getTheory());
		getStructure().mergeWith(other.getStructure());
		getProcedures().mergeWith(other.getProcedures());
	}

	@Override
	public Collection<? extends IFodotFileElement> getElementsOf(Class<?> claz) {
		if (FodotVocabulary.class.equals(claz)) {
			return Arrays.asList(getVocabulary());
		}
		if (FodotStructure.class.equals(claz)) {
			return Arrays.asList(getStructure());
		}
		if (FodotProcedures.class.equals(claz)) {
			return Arrays.asList(getProcedures());
		}
		if (FodotTheory.class.equals(claz)) {
			return Arrays.asList(getTheory());
		}
		return new ArrayList<IFodotFileElement>();
	}

	@Override
	public IFodotFileElement getElementWithName(String name) {
		if (name == null) {
			return null;
		}
		if (name.equals(getVocabulary().getName())) {
			return getVocabulary();
		}
		if (name.equals(getStructure().getName())) {
			return getStructure();
		}
		if (name.equals(getProcedures().getName())) {
			return getProcedures();
		}
		if (name.equals(getTheory().getName())) {
			return getTheory();
		}
		
		return null;
		
	}
}
