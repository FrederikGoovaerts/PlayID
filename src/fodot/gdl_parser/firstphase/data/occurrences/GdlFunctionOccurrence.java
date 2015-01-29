package fodot.gdl_parser.firstphase.data.occurrences;

import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlRule;

public class GdlFunctionOccurrence extends GdlArgumentListOccurrence {

	private Gdl parent;
	
	public GdlFunctionOccurrence(GdlRule rule, Gdl directParent, GdlFunction function) {
		super(rule, function.getBody());
		setDirectParent(directParent);
	}

	public Gdl getDirectParent() {
		return parent;
	}

	public void setDirectParent(Gdl parent) {
		this.parent = parent;
	}
	
}
