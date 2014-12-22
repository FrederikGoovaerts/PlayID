package fodot.gdl_parser;

import java.io.File;
import java.util.List;

import org.ggp.base.util.files.FileUtils;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.grammar.Gdl;

import fodot.communication.IDPFileWriter;
import fodot.objects.Fodot;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * Parsing class for reading a basic .kif file containing a gdl specification
 * and forming data objects which can be used to inspect and adapt the game.
 */
public class Parser {
    
    /* The game for this parser */
    private final Game game;

    private static boolean outputToFile = true;
    private static boolean printBuiltFodot = true;
    
    /***************************************************************************
     * Main Method
     **************************************************************************/

    public static void main(String[] args) {
        File file = new File("resources/games/blocks.kif");
        Parser test = new Parser(file);
    }

    /***************************************************************************
     * Constructor
     **************************************************************************/

    private Fodot parsedFodot;
    
    public Parser(File inputFile) {
        String fileContents = FileUtils.readFileAsString(inputFile);
        game = Game.createEphemeralGame(Game.preprocessRulesheet(fileContents));
        List<Gdl> rules = game.getRules();
        GdlInspector inspector = new GdlInspector(rules);
        Fodot builtFodot = inspector.getFodot();
        setParsedFodot(builtFodot);
        
        if (printBuiltFodot) {
        	System.out.println(builtFodot.toCode());
        }        
        if(outputToFile) {
        	File outputFile = IDPFileWriter.createIDPFileBasedOn(inputFile);
        	IDPFileWriter.writeToIDPFile(builtFodot, outputFile);
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
}
