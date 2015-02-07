package fodot.gdl_parser.firstphase.data.occurrences;

import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlRule;

import fodot.gdl_parser.firstphase.data.declarations.IGdlArgumentListDeclaration;

public class GdlFunctionOccurrence extends GdlArgumentListOccurrence implements IGdlTermOccurrence {

	private IGdlArgumentListDeclaration parent;
	private int argumentIndex;
	
	public GdlFunctionOccurrence(GdlRule rule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlFunction function) {
		super(rule, function.getBody());
		setDirectParent(directParent);
		setArgumentIndex(argumentIndex);
	}

	public IGdlArgumentListDeclaration getDirectParent() {
		return parent;
	}

	public void setDirectParent(IGdlArgumentListDeclaration parent) {
		this.parent = parent;
	}

	public int getArgumentIndex() {
		return argumentIndex;
	}

	private void setArgumentIndex(int argumentIndex) {
		this.argumentIndex = argumentIndex;
	}

	
}
