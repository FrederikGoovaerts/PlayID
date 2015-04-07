package playid.domain.gdl_transformers.visitor;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;

import playid.domain.gdl_transformers.GdlTransformer;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlInspector extends GdlVisitor{

	private Game game;
    private GdlTransformer transformer;
    private List<Gdl> gdlRules;
    private List<Gdl> gdlRelations;

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public GdlInspector(Game gdlGame, GdlTransformer transformer) {
    	setGame(gdlGame);
    	setTransformer(transformer);

        this.gdlRules = new ArrayList<>();
        this.gdlRelations = new ArrayList<>();

        for (Gdl rule : getGame().getRules()) {
            if(rule instanceof GdlRule){
                gdlRules.add(rule);
            } else {
                gdlRelations.add(rule);
            }
        }     
        
        GdlRootVisitors.visitAll(gdlRelations, this);
        GdlRootVisitors.visitAll(gdlRules, this);
    }
    
    /***************************************************************************
     * Class Properties
     **************************************************************************/

    //Transformer
    public GdlTransformer getTransformer() {
        return transformer;
    }
    
    public void setTransformer(GdlTransformer transformer) {
    	this.transformer = transformer;
    }

    //Game
    public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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
            case "legal":
            	this.getTransformer().processLegalRelation(relation);
                break;
            //In case a rule is specified without body:
            case "next":
            case "goal":
            case "terminal":
            	visitRule(GdlPool.getRule(relation));            	
            	break;
            	//Redundant info:
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
            case "init":
            	this.getTransformer().processInitRule(rule);
            	break;
            default:
                this.getTransformer().processDefinitionRule(rule);
                break;
        }
    }

	public static void inspect(Game argGame, GdlTransformer argTransformer) {
		new GdlInspector(argGame, argTransformer);
	}
}
