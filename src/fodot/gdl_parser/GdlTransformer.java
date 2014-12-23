package fodot.gdl_parser;

import fodot.objects.Fodot;
import fodot.objects.structure.enumerations.IFodotEnumerationElement;

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

    void processDefinitionRule(GdlRule rule);

    Fodot buildFodot();
    
    //Translators
    String translateToGdl(IFodotEnumerationElement fodot);
    IFodotEnumerationElement translateToFodot(String gdl);
}
