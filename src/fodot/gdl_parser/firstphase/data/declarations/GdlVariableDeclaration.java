package fodot.gdl_parser.firstphase.data.declarations;

import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlVariable;

public class GdlVariableDeclaration implements IGdlTermDeclaration {
	private GdlVariable variable;
	private GdlRule location;
	
	public GdlVariableDeclaration(GdlVariable variable, GdlRule location) {
		super();
		setVariable(variable);
		setLocation(location);
	}
	
	public GdlVariable getVariable() {
		return variable;
	}
	
	private void setVariable(GdlVariable variable) {
		this.variable = variable;
	}
	
	public GdlRule getLocation() {
		return location;
	}

	private void setLocation(GdlRule location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "[Variable: " + getVariable().getName() + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result
				+ ((variable == null) ? 0 : variable.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GdlVariableDeclaration other = (GdlVariableDeclaration) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (variable == null) {
			if (other.variable != null)
				return false;
		} else if (!variable.equals(other.variable))
			return false;
		return true;
	}

	
}
