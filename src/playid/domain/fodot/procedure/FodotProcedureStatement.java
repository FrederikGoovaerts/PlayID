package playid.domain.fodot.procedure;

import java.util.Collection;
import java.util.HashSet;

import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;

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
