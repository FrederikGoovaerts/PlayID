package fodot.objects.procedure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fodot.objects.file.IFodotFileElement;
import fodot.util.CollectionPrinter;
import fodot.util.NameUtil;

public class FodotProcedures implements IFodotFileElement {
	private String name;
	private List<String> arguments;
	private List<FodotProcedureStatement> procedures;
	private static final String DEFAULT_NAME = "main";
	
	public FodotProcedures(String name, List<String> arguments, List<FodotProcedureStatement> procedures) {
		super();
		setName(name);
		setArguments(arguments);
		setProcedures(procedures);
	}

	public FodotProcedures(String name) {
		this(name, null, null);
	}
	
	public FodotProcedures() {
		this(null, null, null);
	}
	
	//NAME
	public String getName() {
		return name;
	}

	private void setName(String newName) {
		this.name = (!NameUtil.isValidName(newName) ? DEFAULT_NAME : newName);
	}

	//PRODECURES
	public List<FodotProcedureStatement> getProcedures() {
		return new ArrayList<FodotProcedureStatement>(procedures);
	}

	private void setProcedures(List<FodotProcedureStatement> procs) {
		this.procedures = (procs == null ? new ArrayList<FodotProcedureStatement>() : procs);
	}
	
	//ARGUMENTS
	public List<String> getArguments() {
		return new ArrayList<String>(arguments);
	}

	public void setArguments(List<String> args) {
		this.arguments = (args == null ? new ArrayList<String>() : args);
	}

	@Override
	public String toCode() {
		return "procedure " + getName() +
				(getArguments() != null ? CollectionPrinter.toCouple(getArguments()) : "") + " {\n"
				+ CollectionPrinter.toNewLinesWithTabsAsCode(getProcedures(),1)
				+ "}";
	}

	public void merge(FodotProcedures other) {
		procedures.addAll(other.getProcedures());
	}

	@Override
	public Set<IFodotFileElement> getPrerequiredElements() {
		return null;
	}

	@Override
	public void mergeWith(IFodotFileElement other) {
		if (this.getClass().equals(other.getClass())) {
			merge((FodotProcedures) other);
		}
	}
	
}
