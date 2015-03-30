package playid.domain.gdl_transformers;

import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public interface GdlTransformer {

	/**********************************************
	 *  Relations
	 ***********************************************/
	
	void processRoleRelation(GdlRelation relation);

    void processInitRelation(GdlRelation relation);

    void processStaticPredicateRelation(GdlRelation relation);

	void processLegalRelation(GdlRelation relation);   
	

	/**********************************************/

	
	
	/**********************************************
	 *  Rules
	 ***********************************************/
    
    void processNextRule(GdlRule rule);

    void processLegalRule(GdlRule rule);

    void processGoalRule(GdlRule rule);

    void processTerminalRule(GdlRule rule);

    void processDefinitionRule(GdlRule rule); 	

	/**********************************************/

    
}
