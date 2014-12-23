package fodot.gdl_parser;

import fodot.objects.Fodot;
import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.grammar.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlInspector extends GdlVisitor{

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public GdlInspector(List<Gdl> rules){
        this.transformer = new GdlFodotTransformer();
        this.gdlRules = new ArrayList<>();
        this.gdlNonRules = new ArrayList<>();
        this.splitRules(rules);
        GdlRootVisitors.visitAll(gdlNonRules, this);
        GdlRootVisitors.visitAll(gdlRules, this);
    }

    private void splitRules(List<Gdl> rules) {
        for (Gdl rule : rules) {
            if(rule instanceof GdlRule){
                gdlRules.add(rule);
            } else {
                gdlNonRules.add(rule);
            }
        }
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private GdlTransformer transformer;

    public GdlTransformer getTransformer() {
        return transformer;
    }

    private List<Gdl> gdlRules;

    private List<Gdl> gdlNonRules;

    private List<Gdl> getRules() {
        List<Gdl> toReturn = new ArrayList<>(gdlNonRules);
        toReturn.addAll(gdlRules);
        return toReturn;
    }

    public Fodot getFodot(){
        return this.getTransformer().buildFodot();
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
            case "base":
                //ignore this for now
                break;
            case "input":
                //ignore this for now
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
            case "base":
                //ignore this for now
                break;
            case "input":
                //ignore this for now
                break;
            default:
                this.getTransformer().processDefinitionRule(rule);
                break;
        }
    }
}
