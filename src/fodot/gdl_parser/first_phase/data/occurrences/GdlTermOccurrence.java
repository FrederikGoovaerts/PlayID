package fodot.gdl_parser.first_phase.data.occurrences;

import fodot.gdl_parser.first_phase.data.declarations.IGdlArgumentListDeclaration;

public class GdlTermOccurrence {
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
