package fodot.objects.procedure;

import java.util.Collection;
import java.util.HashSet;

import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;

public class FodotProcedureStatement extends FodotElement implements IFodotElement {
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

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return new HashSet<IFodotElement>();
	}
	
}
