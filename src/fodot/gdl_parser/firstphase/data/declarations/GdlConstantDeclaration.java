package fodot.gdl_parser.firstphase.data.declarations;

import org.ggp.base.util.gdl.grammar.GdlConstant;

public class GdlConstantDeclaration implements IGdlTermDeclaration {
	private GdlConstant constant;

	public GdlConstantDeclaration(GdlConstant constant) {
		this.constant = constant;
	}

	public GdlConstant getConstant() {
		return constant;
	}
	
	@Override
	public String toString() {
		return "[Constant: " + constant.getValue() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constant == null) ? 0 : constant.hashCode());
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
		GdlConstantDeclaration other = (GdlConstantDeclaration) obj;
		if (constant == null) {
			if (other.constant != null)
				return false;
		} else if (!constant.equals(other.constant))
			return false;
		return true;
	}
	
	
}
