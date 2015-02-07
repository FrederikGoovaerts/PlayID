package fodot.gdl_parser.firstphase.data.occurrences;

import fodot.gdl_parser.firstphase.data.declarations.IGdlArgumentListDeclaration;

public interface IGdlTermOccurrence {
	IGdlArgumentListDeclaration getDirectParent();
	int getArgumentIndex();
}
