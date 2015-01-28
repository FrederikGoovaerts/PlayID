package fodot.gdl_parser;

import java.io.File;

import org.ggp.base.util.files.FileUtils;
import org.ggp.base.util.game.Game;

import fodot.communication.input.IdpFileWriter;
import fodot.exceptions.gdl.GdlTransformationException;
import fodot.gdl_parser.visitor.GdlInspector;
import fodot.objects.file.IFodotFile;

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


	private final File input;
	private final Game game;
	private GdlFodotTransformer transformer;
	private IFodotFile parsedFodot;

	public Parser(File inputFile) {
		if(inputFile == null || !inputFile.exists()) {
			throw new GdlTransformationException("The given GDL file does not exist: " + inputFile);
		}
		
		//Initialize final variables
		this.input = inputFile;
		String fileContents = FileUtils.readFileAsString(inputFile);
		this.game = Game.createEphemeralGame(Game.preprocessRulesheet(fileContents));
		
		//Initialise other variables
		setTransformer( new GdlFodotTransformer() );
		
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
		
		//TODO inspector gebruiken om een eerstefase visitor te bezoeken
		
		/*
		 * Second phase:
		 * Visit every rule and translate it
		 */
		GdlInspector.inspect( game, getTransformer() );
		setFodot( getTransformer().buildFodot() );

		
		//Do some extra debugging stuff if necessary
		if ( printBuiltFodot ) {
			System.out.println(getFodot().toCode());
		}
		if ( outputToFile ) {
			File outputFile = IdpFileWriter.createIDPFileBasedOn(input);
			IdpFileWriter.writeToIDPFile(getFodot(), outputFile);
		}
	}

	/***************************************************************************
	 * Class Properties
	 **************************************************************************/

	//Parsed Fodot
	public IFodotFile getFodot() {
		return parsedFodot;
	}

	private void setFodot(IFodotFile fodot) {
		this.parsedFodot = fodot;
	}

	//Transformer
	public GdlFodotTransformer getTransformer() {
		return transformer;
	}

	public void setTransformer(GdlFodotTransformer transformer) {
		this.transformer = transformer;
	}

	//Game
	public Game getGame() {
		return game;
	}
}
