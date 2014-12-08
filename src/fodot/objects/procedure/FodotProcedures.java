package fodot.objects.procedure;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.IFodotElement;
import fodot.objects.util.CollectionUtil;

public class FodotProcedures implements IFodotElement {
	private String name;
	private List<String> arguments;
	private List<FodotProcedure> procedures;
	
	public FodotProcedures(String name, List<String> arguments, List<FodotProcedure> procedures) {
		super();
		this.name = name;
		this.arguments = arguments;
		this.procedures = procedures;
	}

	public FodotProcedures(String name) {
		this(name, new ArrayList<String>(), new ArrayList<FodotProcedure>());
	}
	
	public String getName() {
		return name;
	}

	public List<FodotProcedure> getProcedures() {
		return new ArrayList<FodotProcedure>(procedures);
	}
	
	public List<String> getArguments() {
		return new ArrayList<String>(arguments);
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
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
