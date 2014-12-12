package fodot.objects.procedure;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.IFodotElement;
import fodot.objects.util.CollectionUtil;
import fodot.objects.util.NameUtil;

public class FodotProcedures implements IFodotElement {
	private String name;
	private List<String> arguments;
	private List<FodotProcedure> procedures;
	private static final String DEFAULT_NAME = "Proc";
	
	public FodotProcedures(String name, List<String> arguments, List<FodotProcedure> procedures) {
		super();
		setName(name);
		setArguments(arguments);
		setProcedures(procedures);
	}

	public FodotProcedures(String name) {
		this(name, null, null);
	}
	
	//NAME
	public String getName() {
		return name;
	}

	private void setName(String newName) {
		this.name = (!NameUtil.isValidName(newName) ? DEFAULT_NAME : newName);
	}

	//PRODECURES
	public List<FodotProcedure> getProcedures() {
		return new ArrayList<FodotProcedure>(procedures);
	}

	private void setProcedures(List<FodotProcedure> procs) {
		this.procedures = (procs == null ? new ArrayList<FodotProcedure>() : procs);
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
				(getArguments() != null ? CollectionUtil.toCouple(getArguments()) : "") + " {\n"
				+ CollectionUtil.printStringList("", "", "\n", CollectionUtil.toCode(getProcedures()))
				+ "}";
	}

	public void merge(FodotProcedures other) {
		procedures.addAll(other.getProcedures());
	}
	
}
