package playid.util;

import org.ggp.base.util.gdl.grammar.*;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlClassCorrectionUtil {

    //Cast helper
    public static GdlRelation convertToPredicate(GdlTerm term) {
        GdlSentence sentence = term.toSentence();
        return convertToPredicate(sentence);
    }

    public static GdlRelation convertToPredicate(GdlSentence sentence) {
        GdlRelation relation = GdlPool.getRelation(sentence.getName(), sentence.getBody());
        return relation;
    }

    public static GdlProposition convertToProposition(GdlConstant constant) {
        return GdlPool.getProposition(constant);
    }

    public static GdlProposition convertToProposition(GdlRelation relation) {
        return GdlPool.getProposition(relation.getName());
    }
}
