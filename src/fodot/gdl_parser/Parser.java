package fodot.gdl_parser;

import java.io.File;
import java.util.List;

import fodot.objects.Fodot;
import org.ggp.base.util.files.FileUtils;
import org.ggp.base.util.game.Game;
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
        File file = new File("resources/games/blocks.kif");
        Parser test = new Parser(file);
        List<Gdl> rules = test.game.getRules();
        GdlInspector inspector = new GdlInspector(rules);
        Fodot builtFodot = inspector.getFodot();
        System.out.println(builtFodot.toCode());
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
