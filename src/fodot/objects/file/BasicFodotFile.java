package fodot.objects.file;

import java.util.Arrays;
import java.util.Collection;

import fodot.exceptions.fodot.FodotException;
import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotTheory;
import fodot.objects.vocabulary.FodotVocabulary;

@Deprecated
public class BasicFodotFile extends FodotElement implements IFodotFile {
	private FodotIncludeHolder includes;
	private FodotVocabulary vocabulary;
	private FodotTheory theory;
	private FodotStructure structure;
	private FodotProcedures procedures;

	public BasicFodotFile(FodotVocabulary vocabulary, FodotTheory theory,
			FodotStructure structure, FodotProcedures procedures, FodotIncludeHolder includes) {
		super();
		setIncludes(includes);
		setVocabulary(vocabulary);
		setTheory(theory);
		setStructure(structure);
		setProcedures(procedures);
	}

	public BasicFodotFile(FodotVocabulary vocabulary, FodotTheory theory,
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

	public void setIncludes(FodotIncludeHolder includes) {
		this.includes = (includes == null ? new FodotIncludeHolder() : includes);
	}

	//FODOT ELEMENT
	@Override
	public String toCode() {
		return getIncludes().toCode() + "\n"
				+ getVocabulary().toCode() + "\n\n"
				+ getTheory().toCode() + "\n\n"
				+ getStructure().toCode() + "\n\n"
				+ getProcedures().toCode();
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

	@Override
	public void addElement(IFodotFileElement element) {
		if (element instanceof FodotVocabulary) {
			setVocabulary((FodotVocabulary) element);
		} else if (element instanceof FodotStructure) {
			setStructure((FodotStructure) element);
		} else if (element instanceof FodotProcedures) {
			setProcedures((FodotProcedures) element);
		} else if (element instanceof FodotTheory) {
			setTheory((FodotTheory) element);
		} else {
			throw new FodotException("Can't add " + element + " to a BasicFodotFile");
		}
	}

	@Override
	public void addIncludes(FodotIncludeHolder argIncludes) {
		if (getIncludes() == null) {
			setIncludes(argIncludes);				
		} else {
			this.includes.mergeWith(argIncludes);
		}
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return Arrays.asList(includes,procedures,structure,theory,vocabulary);
	}
}
