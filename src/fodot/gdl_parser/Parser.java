package fodot.gdl_parser;

import java.io.File;
import java.util.List;

import org.ggp.base.util.files.FileUtils;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.grammar.Gdl;

import fodot.communication.input.IdpFileWriter;
import fodot.objects.Fodot;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * Parsing class for reading a basic .kif file containing a gdl specification
 * and forming data objects which can be used to inspect and adapt the game.
 */
public class Parser {

    private static boolean outputToFile = true;
    private static boolean printBuiltFodot = false;
    
    /***************************************************************************
     * Main Method
     **************************************************************************/

    public static void main(String[] args) {
        File file = new File("resources/games/maze.kif");
        Parser test = new Parser(file);
        test.run();
    }

    /***************************************************************************
     * Constructor
     **************************************************************************/

    private Fodot parsedFodot;
    private GdlInspector inspector;
    
    public Parser(File inputFile) {
        if(inputFile == null || !inputFile.exists())
            throw new IllegalArgumentException("Bad file!");
        input = inputFile;
        String fileContents = FileUtils.readFileAsString(inputFile);
        game = Game.createEphemeralGame(Game.preprocessRulesheet(fileContents));
    }

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    public void run() {
        List<Gdl> rules = game.getRules();

        setInspector(new GdlInspector(rules));
        Fodot builtFodot = getInspector().getFodot();
        setParsedFodot(builtFodot);

        if (printBuiltFodot) {
            System.out.println(builtFodot.toCode());
        }
        if (outputToFile) {
            File outputFile = IdpFileWriter.createIDPFileBasedOn(input);
            IdpFileWriter.writeToIDPFile(builtFodot, outputFile);
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

	public GdlInspector getInspector() {
		return inspector;
	}

	private void setInspector(GdlInspector inspector) {
		this.inspector = inspector;
	}
	
	public GdlTransformer getTransformer() {
		return getInspector().getTransformer();
	}

    private final File input;

    /* The game for this parser */
    private final Game game;
}
