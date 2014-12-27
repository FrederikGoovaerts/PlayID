package fodot.objects.procedure;

import fodot.objects.general.IFodotElement;

public class FodotProcedure implements IFodotElement {
	//TODO: better structure for this?
	private String procedure;

	public FodotProcedure(String procedure) {
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
