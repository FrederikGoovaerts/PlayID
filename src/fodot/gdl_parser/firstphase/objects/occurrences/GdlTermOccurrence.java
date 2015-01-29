package fodot.gdl_parser.firstphase.objects.occurrences;

import org.ggp.base.util.gdl.grammar.Gdl;

public abstract class GdlTermOccurrence {
	private Gdl directParent;

	public GdlTermOccurrence(Gdl directParent) {
		this.directParent = directParent;
	}

	public Gdl getDirectParent() {
		return directParent;
	}

	public void setDirectParent(Gdl directParent) {
		this.directParent = directParent;
	}
}
