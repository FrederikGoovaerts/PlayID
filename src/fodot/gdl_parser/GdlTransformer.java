package fodot.gdl_parser;

import org.ggp.base.util.gdl.grammar.GdlRelation;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public interface GdlTransformer {

    void processRole(GdlRelation relation);

    void processInit(GdlRelation relation);

    void processLegal(GdlRelation relation);

    void processTerminal(GdlRelation relation);

    void processGoal(GdlRelation relation);

    void processStaticRelation(GdlRelation relation);
}
