package fodot.gdl_parser.first_phase.data.declarations;

import org.ggp.base.util.gdl.grammar.GdlConstant;

public interface IGdlArgumentListDeclaration {
	public GdlConstant getName();
	public int getArity();
}
