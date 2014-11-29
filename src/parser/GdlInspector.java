package parser;

import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.grammar.*;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlInspector extends GdlVisitor{

    public GdlInspector (){

    }

    @Override
    public void visitTerm(GdlTerm term) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitConstant(GdlConstant constant) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitVariable(GdlVariable variable) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitFunction(GdlFunction function) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitLiteral(GdlLiteral literal) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitSentence(GdlSentence sentence) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitRelation(GdlRelation relation) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitProposition(GdlProposition proposition) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitNot(GdlNot not) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitDistinct(GdlDistinct distinct) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitOr(GdlOr or) {
        // Do nothing; override in a subclass to do something.
    }

    @Override
    public void visitRule(GdlRule rule) {
        // Do nothing; override in a subclass to do something.
    }
}
