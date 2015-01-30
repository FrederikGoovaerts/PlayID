package fodot.gdl_parser.firstphase.data.declarations;

import org.ggp.base.util.gdl.grammar.GdlConstant;

public abstract class GdlArgumentListDeclaration implements IGdlArgumentListDeclaration {
	private GdlConstant name;
	private int arity;

	public GdlArgumentListDeclaration(GdlConstant name, int arity) {
		super();
		this.name = name;
		this.arity = arity;
	}
	
	/**********************************************
	 *  Class properties
	 ***********************************************/
	public GdlConstant getName() {
		return name;
	}

	public int getArity() {
		return arity;
	}
	/**********************************************/
	
	
	/**********************************************
	 *  Hashcode & Equals
	 ***********************************************/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + arity;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		GdlArgumentListDeclaration other = (GdlArgumentListDeclaration) obj;
		if (arity != other.arity)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
	
	@Override 
	public String toString() {
		return getName().toString() + "/" + getArity();
	}

	/**********************************************/

}
