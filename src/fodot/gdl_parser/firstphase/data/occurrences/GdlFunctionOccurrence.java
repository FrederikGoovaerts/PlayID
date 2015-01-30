package fodot.gdl_parser.firstphase.data.occurrences;

import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlRule;

public class GdlFunctionOccurrence extends GdlArgumentListOccurrence implements IGdlTermOccurrence{

	private Gdl parent;
	private int argumentIndex;
	
	public GdlFunctionOccurrence(GdlRule rule, Gdl directParent, int argumentIndex, GdlFunction function) {
		super(rule, function.getBody());
		setDirectParent(directParent);
		setArgumentIndex(argumentIndex);
	}

	public Gdl getDirectParent() {
		return parent;
	}

	public void setDirectParent(Gdl parent) {
		this.parent = parent;
	}

	public int getArgumentIndex() {
		return argumentIndex;
	}

	private void setArgumentIndex(int argumentIndex) {
		this.argumentIndex = argumentIndex;
	}

	
}
