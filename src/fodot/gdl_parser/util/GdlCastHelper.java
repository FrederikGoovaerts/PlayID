package fodot.gdl_parser.util;

import fodot.gdl_parser.GdlFodotTransformer;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;
import org.ggp.base.util.gdl.grammar.GdlLiteral;

import java.util.HashMap;
import java.util.List;

import static fodot.helpers.FodotPartBuilder.*;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlCastHelper {

    public static IFodotFormula generateFodotFormulaFrom(
            List<GdlLiteral> originalFormulas,
            HashMap<String,FodotVariable> variables,
            GdlFodotTransformer trans){
        return null;
    }
}
