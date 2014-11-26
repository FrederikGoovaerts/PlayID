package parser.gdl;

import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlRule;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * Class to inspect Gdl objects and cast them appropriately to their actual Gdl subclasses
 */
public class GdlClassInspector {

    public static boolean isGdlRule(Gdl gdl){
        if(gdl == null)
            return false;
        if(gdl instanceof GdlRule)
            return true;
        return false;
    }

    public static GdlRule getGdlRule(Gdl gdl){
        if(isGdlRule(gdl))
            return (GdlRule) gdl;
        throw new IllegalGdlCastException("Was not a GdlRule!");
    }


}
