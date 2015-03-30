package playid.domain.gdl_transformers.first_phase.data.declarations;

import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlRelation;

public class GdlPredicateDeclaration extends GdlArgumentListDeclaration {
	public GdlPredicateDeclaration(GdlConstant name, int arity) {
		super(name, arity);
	}
	public GdlPredicateDeclaration(GdlRelation relation) {
		this(relation.getName(), relation.arity());
	}
	@Override
	public String toString() {
		return "[Predicate: " + getName().getValue() + "]";
	}
}
