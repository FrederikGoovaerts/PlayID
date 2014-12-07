package fodot.objects.procedure;

import java.util.List;

import fodot.objects.IFodotElement;
import fodot.objects.util.CollectionUtil;

public class FodotProcedures implements IFodotElement {
	private String name;
	private List<FodotProcedure> procedures;
	
	public FodotProcedures(String name, List<FodotProcedure> procedures) {
		super();
		this.name = name;
		this.procedures = procedures;
	}

	public String getName() {
		return name;
	}

	public List<FodotProcedure> getProcedures() {
		return procedures;
	}

	@Override
	public String toCode() {
		return CollectionUtil.printStringList("", "", "\n", CollectionUtil.toCode(procedures));
	}
	
}
