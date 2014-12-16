package fodot.gdl_parser;

import fodot.objects.Fodot;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public interface GdlTransformer {

    void processRoleRelation(GdlRelation relation);

    void processInitRelation(GdlRelation relation);

    void processStaticPredicateRelation(GdlRelation relation);

    void processNextRule(GdlRule rule);

    void processLegalRule(GdlRule rule);

    void processGoalRule(GdlRule rule);

    void processTerminalRule(GdlRule rule);

    void processDefinitionRule(Object rule);

    Fodot buildFodot();
}
