package fodot.gdl_parser;

import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.grammar.*;

import java.util.Collection;


public class GdlRootVisitors {
	/**
	 * Visits the given Gdl object as its actual class. For example, when called
	 * on a GdlRule, it actually visits the GdlRule, and not a Gdl object.
	 *
	 * @author Alex Landau, Frederik Goovaerts
	 */
	public static void visitAll(Gdl gdl, GdlVisitor visitor) {
		if (gdl instanceof GdlTerm) {
			visitTerm((GdlTerm) gdl, visitor);
		} else if (gdl instanceof GdlLiteral) {
			visitLiteral((GdlLiteral) gdl, visitor);
		} else if (gdl instanceof GdlRule) {
			visitRule((GdlRule) gdl, visitor);
		} else {
			throw new RuntimeException("Unexpected Gdl type " + gdl.getClass());
		}
	}
	public static void visitAll(Collection<? extends Gdl> collection, GdlVisitor visitor) {
		for (Gdl gdl : collection) {
			visitAll(gdl, visitor);
		}
	}
	private static void visitRule(GdlRule rule, GdlVisitor visitor) {
		visitor.visitRule(rule);
	}
	private static void visitLiteral(GdlLiteral literal, GdlVisitor visitor) {
		if (literal instanceof GdlSentence) {
			visitSentence((GdlSentence) literal, visitor);
		} else if (literal instanceof GdlNot) {
			visitNot((GdlNot) literal, visitor);
		} else if (literal instanceof GdlOr) {
			visitOr((GdlOr) literal, visitor);
		} else if (literal instanceof GdlDistinct) {
			visitDistinct((GdlDistinct) literal, visitor);
		} else {
			throw new RuntimeException("Unexpected GdlLiteral type " + literal.getClass());
		}
	}
	private static void visitDistinct(GdlDistinct distinct, GdlVisitor visitor) {
		visitor.visitDistinct(distinct);
	}
	private static void visitOr(GdlOr or, GdlVisitor visitor) {
		visitor.visitOr(or);
	}
	private static void visitNot(GdlNot not, GdlVisitor visitor) {
		visitor.visitNot(not);
	}
	private static void visitSentence(GdlSentence sentence, GdlVisitor visitor) {
		if (sentence instanceof GdlProposition) {
			visitProposition((GdlProposition) sentence, visitor);
		} else if (sentence instanceof GdlRelation) {
			visitRelation((GdlRelation) sentence, visitor);
		} else {
			throw new RuntimeException("Unexpected GdlSentence type " + sentence.getClass());
		}
	}
	private static void visitRelation(GdlRelation relation, GdlVisitor visitor) {
		visitor.visitRelation(relation);
	}
	private static void visitProposition(GdlProposition proposition,
			GdlVisitor visitor) {
		visitor.visitProposition(proposition);
	}
	private static void visitTerm(GdlTerm term, GdlVisitor visitor) {
		if (term instanceof GdlConstant) {
			visitor.visitConstant((GdlConstant) term);
		} else if (term instanceof GdlVariable) {
			visitor.visitVariable((GdlVariable) term);
		} else if (term instanceof GdlFunction) {
			visitFunction((GdlFunction) term, visitor);
		} else {
			throw new RuntimeException("Unexpected GdlTerm type " + term.getClass());
		}
	}
	private static void visitFunction(GdlFunction function, GdlVisitor visitor) {
		visitor.visitFunction(function);
	}
}
