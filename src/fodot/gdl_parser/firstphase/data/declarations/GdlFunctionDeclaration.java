package fodot.gdl_parser.firstphase.data.declarations;

import org.ggp.base.util.gdl.grammar.GdlFunction;

public class GdlFunctionDeclaration extends GdlArgumentListDeclaration implements IGdlTermDeclaration {
	public GdlFunctionDeclaration(GdlFunction function) {
		super(function.getName(), function.arity());
	}
	@Override
	public String toString() {
		return "[Function: " + getName().getValue() + "]";
	}
}
