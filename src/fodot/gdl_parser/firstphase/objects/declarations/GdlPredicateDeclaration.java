package fodot.gdl_parser.firstphase.objects.declarations;

import org.ggp.base.util.gdl.grammar.GdlRelation;

public class GdlPredicateDeclaration extends GdlArgumentedListDeclaration {
	public GdlPredicateDeclaration(GdlRelation relation) {
		super(relation.getName(), relation.arity());
	}
}
