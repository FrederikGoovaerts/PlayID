package parser;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ggp.base.util.Pair;
import org.ggp.base.util.files.FileUtils;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.GdlVisitors;
import org.ggp.base.util.gdl.grammar.Gdl;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * Parsing class for reading a basic .kif file containing a gdl specification
 * and forming data objects which can be used to inspect and adapt the game.
 */
public class Parser {

    /***************************************************************************
     * Main Method
     **************************************************************************/

    public static void main(String[] args) {
        File file = new File("resources/games/choice.kif");
        Parser test = new Parser(file);
        List<Gdl> rules = test.game.getRules();
        GdlInspector inspector = new GdlInspector(rules);
    }

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public Parser(File file) {
        String fileContents = FileUtils.readFileAsString(file);
        game = Game.createEphemeralGame(Game.preprocessRulesheet(fileContents));
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    /* The game for this parser */
    private final Game game;


}
