package fodot.gdl_parser.firstphase.data.occurrences;

import org.ggp.base.util.gdl.grammar.Gdl;

public abstract class GdlTermOccurrence implements IGdlTermOccurrence {
	private Gdl directParent;
	private int argumentIndex;

	public GdlTermOccurrence(Gdl directParent, int argumentIndex) {
		setDirectParent(directParent);
		setArgumentIndex(argumentIndex);
	}

	public Gdl getDirectParent() {
		return directParent;
	}

	private void setDirectParent(Gdl directParent) {
		this.directParent = directParent;
	}

	public int getArgumentIndex() {
		return argumentIndex;
	}

	private void setArgumentIndex(int argumentIndex) {
		this.argumentIndex = argumentIndex;
	}
}
