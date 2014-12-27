package fodot.objects.procedure;

import fodot.objects.general.IFodotElement;

public class FodotProcedureStatement implements IFodotElement {
	//TODO: better structure for this?
	private String procedure;

	public FodotProcedureStatement(String procedure) {
		super();
		this.procedure = procedure;
	}
	
	public String getProcedure() {
		return procedure;
	}

	@Override
	public String toCode() {
		return getProcedure();
	}
	
}
