package fodot.communication;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
import fodot.gdl_parser.GdlParser;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.FodotStructure;
import fodot.patterns.ChainFodotOptimizer;
import fodot.patterns.IFodotOptimizer;
import fodot.patterns.IntegerTypeRecognizer;

public class PlayIdProcessor {

	private static final IActionOutputter DEFAULT_OUTPUTTER = new GdlActionPrinter();
	private static final IFodotOptimizer DEFAULT_OPTIMIZER = new ChainFodotOptimizer(Arrays.asList(new IntegerTypeRecognizer()));
	
	private IActionOutputter outputter;
	private IFodotOptimizer optimizer;
	
	/**********************************************
	 *  Constructors
	 ***********************************************/

	public PlayIdProcessor(IActionOutputter outputter, IFodotOptimizer optimizer) {
		super();
		this.outputter = outputter;
		this.optimizer = optimizer;
	}
	
	public PlayIdProcessor(IActionOutputter outputter) {
		this(outputter, DEFAULT_OPTIMIZER);
	}

	public PlayIdProcessor() {
		this(DEFAULT_OUTPUTTER);
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
	/**********************************************/

	/**********************************************
	 *  Optimizer
	 ***********************************************/

	public IFodotOptimizer getOptimizer() {
		return optimizer;
	}

	public void setOptimizer(IFodotOptimizer optimizer) {
		this.optimizer = optimizer;
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
		GdlParser parser = new GdlParser(gdlFile);
		parser.run();
		IFodotFile parsedFodotFile = parser.getFodotFile();
		parsedFodotFile = getOptimizer().improve(parsedFodotFile);
		
		//Create IDPfile in same location as GDL file
		File idpFile = IdpFileWriter.createIDPFileBasedOn(gdlFile);
		IdpFileWriter.writeToIDPFile(parsedFodotFile, idpFile);

		//Make IDP solve it
		IIdpCaller caller = new IdpCaller(false);
		String idpResult = caller.callIDP(idpFile);
		
		//Process results
		IdpResultTransformer resultTransformer = new IdpResultTransformer(parsedFodotFile, idpResult);
		List<FodotStructure> models = resultTransformer.getModels();
		
		//Check if we found a model
		if (models.size() == 0) {
			throw new NoValidModelsException();
		}
		
		//Transform a solution 
		GdlAnswerCalculator answerer = new GdlAnswerCalculator(parser.getFodotTransformer().getGdlVocabulary(), models);
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
			throw new PlayIdArgumentException(
					"Please give the uri of a valid GDL file to the PlayID processor.", 0, 1);
		}
		if (!args[0].contains(".kif")) {
			throw new PlayIdArgumentException(
					"The given uri must lead to a .kif file.");
		}
		File gdlFile = new File(args[0]);
		if (!gdlFile.exists()) {
			throw new PlayIdArgumentException(
					"The given uri must lead to a existing file.");
		}
		
		PlayIdProcessor processor = new PlayIdProcessor();
		processor.process(gdlFile);
	}
	

	/**********************************************/
	
	
}
