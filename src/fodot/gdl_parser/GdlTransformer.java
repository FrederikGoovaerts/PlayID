package fodot.gdl_parser;

import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;

import fodot.communication.gdloutput.IFodotGdlTranslator;
import fodot.objects.file.IFodotFile;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public interface GdlTransformer extends IFodotGdlTranslator {

    void processRoleRelation(GdlRelation relation);

    void processInitRelation(GdlRelation relation);

    void processStaticPredicateRelation(GdlRelation relation);

    void processNextRule(GdlRule rule);

    void processLegalRule(GdlRule rule);

    void processGoalRule(GdlRule rule);

    void processTerminalRule(GdlRule rule);

    void processDefinitionRule(GdlRule rule);

    IFodotFile buildFodot();
    
    //Translators
//    String translateToGdl(IFodotEnumerationElement fodot);
//    IFodotEnumerationElement translateToFodot(String gdl);
}
