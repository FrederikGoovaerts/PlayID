package fodot.gdl_parser;

import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlTerm;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public interface GdlTransformer {

    void processRole(GdlRelation relation);

    void processInit(GdlRelation relation);

    void processLegal(GdlRelation relation);

    void processTerminal(GdlRelation relation);

    void processGoal(GdlRelation relation);

    void processStaticPredicate(GdlRelation relation);

    void processPredicate(GdlTerm gdlTerm);

    void processAction(GdlTerm gdlTerm);
}
