package fodot.gdl_parser;

import java.io.File;
import java.io.IOException;
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
        System.out.println(test.getParsedFodot().toCode());
    }

    /***************************************************************************
     * Constructor
     **************************************************************************/

    private Fodot parsedFodot;
    
    public Parser(File file) {
        String fileContents = FileUtils.readFileAsString(file);
        game = Game.createEphemeralGame(Game.preprocessRulesheet(fileContents));
        List<Gdl> rules = game.getRules();
        GdlInspector inspector = new GdlInspector(rules);
        Fodot builtFodot = inspector.getFodot();
        setParsedFodot(builtFodot);
        if(outputToFile) {
            String originalPath = file.getAbsolutePath();
            StringBuilder b = new StringBuilder(originalPath);
            b.replace(originalPath.lastIndexOf(".kif"), originalPath.lastIndexOf(".kif") + 4, ".idp");
            File outFile = new File(b.toString());
            try {
                FileUtils.writeStringToFile(outFile, builtFodot.toCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    public Fodot getParsedFodot() {
    	return parsedFodot;
    }
    
    private void setParsedFodot(Fodot fodot) {
    	this.parsedFodot = fodot;
    }
    
    
    /* The game for this parser */
    private final Game game;

    private static boolean outputToFile = true;
}
