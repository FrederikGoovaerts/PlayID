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
        System.out.println("term = [" + term + "]");
    }

    @Override
    public void visitConstant(GdlConstant constant) {
        System.out.println("constant = [" + constant + "]");
    }

    @Override
    public void visitVariable(GdlVariable variable) {
        System.out.println("variable = [" + variable + "]");
    }

    @Override
    public void visitFunction(GdlFunction function) {
        System.out.println("function = [" + function + "]");
    }

    @Override
    public void visitLiteral(GdlLiteral literal) {
        System.out.println("literal = [" + literal + "]");
    }

    @Override
    public void visitSentence(GdlSentence sentence) {
        System.out.println("sentence = [" + sentence + "]");
    }

    @Override
    public void visitRelation(GdlRelation relation) {
        System.out.println("relation = [" + relation + "]");
    }

    @Override
    public void visitProposition(GdlProposition proposition) {
        System.out.println("proposition = [" + proposition + "]");
    }

    @Override
    public void visitNot(GdlNot not) {
        System.out.println("not = [" + not + "]");
    }

    @Override
    public void visitDistinct(GdlDistinct distinct) {
        System.out.println("distinct = [" + distinct + "]");
    }

    @Override
    public void visitOr(GdlOr or) {
        System.out.println("or = [" + or + "]");
    }

    @Override
    public void visitRule(GdlRule rule) {
        System.out.println("rule = [" + rule + "]");
    }
}
