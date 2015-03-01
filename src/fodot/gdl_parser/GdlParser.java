package fodot.gdl_parser;

import java.io.File;

import org.ggp.base.util.files.FileUtils;
import org.ggp.base.util.game.Game;

import fodot.communication.input.IdpFileWriter;
import fodot.exceptions.gdl.GdlTransformationException;
import fodot.gdl_parser.first_phase.GdlTypeIdentifier;
import fodot.gdl_parser.second_phase.GdlFodotTransformer;
import fodot.gdl_parser.visitor.GdlInspector;
import fodot.objects.file.IFodotFile;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * Parsing class for reading a basic .kif file containing a gdl specification
 * and forming data objects which can be used to inspect and adapt the game.
 */
public class GdlParser {

	private static boolean outputToFile = true;
	private static boolean printBuiltFodot = false;

	/***************************************************************************
	 * Main Method
	 **************************************************************************/

	public static void main(String[] args) {
		File file = new File("resources/games/singleplayer/hanoi.kif");
		GdlParser test = new GdlParser(file);
		test.run();
	}

	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	//Input
	private final File input;
	private final Game game;
	
	//Processing
	private GdlTypeIdentifier identifier = new GdlTypeIdentifier();
	private GdlVocabulary gdlVocabulary;
	private GdlFodotTransformer fodotTransformer;
    private int turnLimit = -1;
	
	//Output
	private IFodotFile parsedFodot;

	public GdlParser(File inputFile) {
		if(inputFile == null || !inputFile.exists()) {
			throw new GdlTransformationException("The given GDL file does not exist: " + inputFile);
		}
		
		//Initialize final variables
		this.input = inputFile;
		String fileContents = FileUtils.readFileAsString(inputFile);
		this.game = Game.createEphemeralGame(Game.preprocessRulesheet(fileContents));
		
	}

    public GdlParser(File inputFile, int turnLimit) {
        this(inputFile);
        this.turnLimit = turnLimit;
    }

	/***************************************************************************
	 * Class Methods
	 **************************************************************************/

	public void run() {
		/*
		 * First phase:
		 * Create datastructure containing basic info about the game:
		 * The types, the predicates with their correct typing, sorting of
		 * the dynamic and static predicates...
		 */
		
		GdlInspector.inspect(game, getIdentifier().createTransformer());
		this.gdlVocabulary = getIdentifier().generateTranslationData();
		
		/*
		 * Second phase:
		 * Visit every rule and translate it
		 */
		setFodotTransformer( new GdlFodotTransformer(this.gdlVocabulary) );
		GdlInspector.inspect( game, getFodotTransformer() );
        if(this.turnLimit >0) {
            setFodot( getFodotTransformer().buildFodot(turnLimit) );
        } else {
            setFodot(getFodotTransformer().buildFodot());
        }

		
		//Do some extra debugging stuff if necessary
		if ( printBuiltFodot ) {
			System.out.println(getFodotFile().toCode());
		}
		if ( outputToFile ) {
			File outputFile = IdpFileWriter.createIDPFileBasedOn(input);
			IdpFileWriter.writeToIDPFile(getFodotFile(), outputFile);
		}
	}

	/***************************************************************************
	 * Class Properties
	 **************************************************************************/

	//Parsed Fodot
	public IFodotFile getFodotFile() {
		return parsedFodot;
	}

	private void setFodot(IFodotFile fodot) {
		this.parsedFodot = fodot;
	}

	//Identifier

	public GdlTypeIdentifier getIdentifier() {
		return identifier;
	}	
	
	//Transformer
	public GdlFodotTransformer getFodotTransformer() {
		return fodotTransformer;
	}
	
	public void setFodotTransformer(GdlFodotTransformer transformer) {
		this.fodotTransformer = transformer;
	}

	//Game
	public Game getGame() {
		return game;
	}
}
