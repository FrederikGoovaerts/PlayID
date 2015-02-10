package fodot.gdl_parser.firstphase.data.occurrences;

import fodot.gdl_parser.firstphase.data.declarations.IGdlArgumentListDeclaration;

public class GdlTermOccurrence implements IGdlTermOccurrence {
	private IGdlArgumentListDeclaration directParent;
	private int argumentIndex;

	public GdlTermOccurrence(IGdlArgumentListDeclaration directParent, int argumentIndex) {
		setDirectParent(directParent);
		setArgumentIndex(argumentIndex);
	}

	public IGdlArgumentListDeclaration getDirectParent() {
		return directParent;
	}

	private void setDirectParent(IGdlArgumentListDeclaration directParent) {
		this.directParent = directParent;
	}

	public int getArgumentIndex() {
		return argumentIndex;
	}

	private void setArgumentIndex(int argumentIndex) {
		this.argumentIndex = argumentIndex;
	}
}
