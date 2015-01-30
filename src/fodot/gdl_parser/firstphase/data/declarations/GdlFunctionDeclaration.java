package fodot.gdl_parser.firstphase.data.declarations;

import org.ggp.base.util.gdl.grammar.GdlFunction;

public class GdlFunctionDeclaration extends GdlArgumentListDeclaration {
	public GdlFunctionDeclaration(GdlFunction function) {
		super(function.getName(), function.arity());
	}
}
