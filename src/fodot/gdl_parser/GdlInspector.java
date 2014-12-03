package fodot.gdl_parser;

import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.GdlVisitors;
import org.ggp.base.util.gdl.grammar.*;

import java.util.List;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlInspector extends GdlVisitor{

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public GdlInspector(List<Gdl> rules, GdlTransformer transformer){
        this.transformer = transformer;
        GdlVisitors.visitAll(rules, this);
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private GdlTransformer transformer;

    private GdlTransformer getTransformer() {
        return transformer;
    }

    /***************************************************************************
     * Visitor Methods
     **************************************************************************/

    @Override
    public void visitRelation(GdlRelation relation) {
        String relationName = relation.getName().getValue();
        switch (relationName) {
            case "role": this.getTransformer().processRole(relation);
                break;
            case "init": this.getTransformer().processInit(relation);
                break;
            case "legal": this.getTransformer().processLegal(relation);
                break;
            case "terminal": this.getTransformer().processTerminal(relation);
                break;
            case "goal": this.getTransformer().processGoal(relation);
                break;
            default: this.getTransformer().processStaticRelation(relation);
                break;
        }
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
        //Add this stuff as a predicate to the theory
    }

    @Override
    public void visitDistinct(GdlDistinct distinct) {
        //This has to be picked up earlier in the recursive tree?
        //Needs a second run?
    }

    @Override
    public void visitRule(GdlRule rule) {
        // Important. Declares next, goals, terminals and more
    }
}
