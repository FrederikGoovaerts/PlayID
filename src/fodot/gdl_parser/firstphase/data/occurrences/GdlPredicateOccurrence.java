package fodot.gdl_parser.firstphase.data.occurrences;

import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;

public class GdlPredicateOccurrence extends GdlArgumentListOccurrence {

	public GdlPredicateOccurrence(GdlRule rule, GdlRelation predicate) {
		super(rule, predicate.getBody());
	}

}
