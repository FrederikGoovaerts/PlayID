package fodot.gdl_parser;

import fodot.objects.Fodot;
import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.grammar.*;

import java.util.List;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlInspector extends GdlVisitor{

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public GdlInspector(List<Gdl> rules){
        this.rules = rules;
        this.transformer = new GdlFodotTransformer();
        GdlRootVisitors.visitAll(rules, this);
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private GdlTransformer transformer;

    private GdlTransformer getTransformer() {
        return transformer;
    }

    private List<Gdl> rules;

    private List<Gdl> getRules() {
        return rules;
    }

    public Fodot getFodot(){
        return this.getTransformer().builFodot();
    }

    /***************************************************************************
     * Visitor Methods
     **************************************************************************/

    @Override
    public void visitRelation(GdlRelation relation) {
        String relationType = relation.getName().getValue();
        switch (relationType) {
            case "role":
                this.getTransformer().processRoleRelation(relation);
                break;
            case "init":
                this.getTransformer().processInitRelation(relation);
                break;
            default:
                this.getTransformer().processStaticPredicateRelation(relation);
                break;
        }
    }

    @Override
    public void visitRule(GdlRule rule) {
        String ruleType = rule.getHead().getName().getValue();
        switch (ruleType) {
            case "next":
                this.getTransformer().processNextRule(rule);
                break;
            case "legal":
                this.getTransformer().processLegalRule(rule);
                break;
            case "goal":
                this.getTransformer().processGoalRule(rule);
                break;
            case "terminal":
                this.getTransformer().processTerminalRule(rule);
                break;
            default:
                this.getTransformer().processDefinitionRule(rule);
                break;
        }
    }
}
