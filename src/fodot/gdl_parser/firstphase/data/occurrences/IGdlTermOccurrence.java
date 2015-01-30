package fodot.gdl_parser.firstphase.data.occurrences;

import org.ggp.base.util.gdl.grammar.Gdl;

public interface IGdlTermOccurrence {

	Gdl getDirectParent();
	int getArgumentIndex();
}
