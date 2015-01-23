package fodot.communication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fodot.communication.gdloutput.GdlActionPrinter;
import fodot.communication.gdloutput.IActionOutputter;
import fodot.communication.input.IIdpCaller;
import fodot.communication.input.IdpCaller;
import fodot.communication.input.IdpFileWriter;
import fodot.communication.output.GdlAction;
import fodot.communication.output.GdlAnswerCalculator;
import fodot.communication.output.IdpResultTransformer;
import fodot.exceptions.idp.IdpConnectionException;
import fodot.exceptions.idp.IdpParseException;
import fodot.exceptions.idp.NoValidModelsException;
import fodot.exceptions.idp.UnsatisfiableIdpFileException;
import fodot.exceptions.playid.PlayIdArgumentException;
import fodot.gdl_parser.Parser;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.FodotStructure;

public class PlayIdProcessor {
	
	private static final IActionOutputter DEFAULT_OUTPUTTER = new GdlActionPrinter();
	private IActionOutputter outputter;
	
	/**********************************************
	 *  Constructors
	 ***********************************************/

	public PlayIdProcessor(IActionOutputter outputter) {
		super();
		this.outputter = outputter;
	}

	public PlayIdProcessor() {
		this(getDefaultOutputter());
	}	

	/**********************************************/

	/**********************************************
	 *  Outputter
	 ***********************************************/

	public IActionOutputter getOutputter() {
		return outputter;
	}

	public void setOutputter(IActionOutputter outputter) {
		this.outputter = outputter;
	}	

	public static IActionOutputter getDefaultOutputter() {
		return DEFAULT_OUTPUTTER;
	}

	/**********************************************/

	/**********************************************
	 *  Process
	 ***********************************************/
	
	public void process(File gdlFile)
			throws IOException, IdpConnectionException,
				IdpParseException, UnsatisfiableIdpFileException, IllegalStateException,
				NoValidModelsException 
		{
		
		//Convert GDL to IDP
		Parser parser = new Parser(gdlFile);
		parser.run();
		IFodotFile parsedFodotFile = parser.getParsedFodot();
		
		
		//Create IDPfile in same location as GDL file
		File idpFile = IdpFileWriter.createIDPFileBasedOn(gdlFile);
		IdpFileWriter.writeToIDPFile(parsedFodotFile, idpFile);

		//Make IDP solve it
		IIdpCaller caller = new IdpCaller();
		String idpResult = caller.callIDP(idpFile);

		//Process results
		IdpResultTransformer resultTransformer = new IdpResultTransformer(parsedFodotFile, idpResult);
		List<FodotStructure> models = resultTransformer.getModels();
		
		//Check if we found a model
		if (models.size() == 0) {
			throw new NoValidModelsException();
		}
		
		//Transform a solution 
		GdlAnswerCalculator answerer = new GdlAnswerCalculator(models);
		List<GdlAction> actions = answerer.generateActionSequence();
		
		//Output it
		outputter.output(actions);
	}

	/**********************************************/

	/**********************************************
	 *  Main method
	 ***********************************************/
	
	public static void main(String[] args) throws IOException {
		if (args.length <= 0) {
			throw new PlayIdArgumentException("Please give the uri of a valid GDL file to the PlayID processor.");
		}
		if (!args[0].contains(".kif")) {
			throw new PlayIdArgumentException("The given uri must lead to a .kif file.");
		}
		File gdlFile = new File(args[0]);
		if (!gdlFile.exists()) {
			throw new PlayIdArgumentException("The given uri must lead to a existing file.");
		}
		
		PlayIdProcessor processor = new PlayIdProcessor();
		processor.process(gdlFile);
	}
	

	/**********************************************/
	
	
}
