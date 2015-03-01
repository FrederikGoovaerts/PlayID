package fodot.communication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fodot.communication.gdloutput.GdlActionPrinter;
import fodot.communication.gdloutput.IActionOutputter;
import fodot.communication.input.IIdpCaller;
import fodot.communication.input.IdpCaller;
import fodot.communication.input.IdpFileWriter;
import fodot.communication.output.GdlActions;
import fodot.communication.output.GdlAnswerCalculator;
import fodot.communication.output.IdpResultTransformer;
import fodot.exceptions.idp.IdpConnectionException;
import fodot.exceptions.idp.IdpErrorException;
import fodot.exceptions.idp.NoValidModelsException;
import fodot.exceptions.idp.UnsatisfiableIdpFileException;
import fodot.exceptions.playid.PlayIdArgumentException;
import fodot.gdl_parser.GdlParser;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.FodotStructure;
import fodot.patterns.fodot_file.FodotChainOptimizer;
import fodot.patterns.fodot_file.IFodotOptimizer;

public class PlayIdProcessor {

	private static final IActionOutputter DEFAULT_OUTPUTTER = new GdlActionPrinter();
	private static final IFodotOptimizer DEFAULT_OPTIMIZER = new FodotChainOptimizer();
	
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
	 *  Process
	 ***********************************************/
	
	public void process(File gdlFile)
			throws IOException, IdpConnectionException,
				IdpErrorException, UnsatisfiableIdpFileException, IllegalStateException,
				NoValidModelsException 
	{
        List<FodotStructure> models = null;
        GdlParser parser = null;
        int amountOfTurns = 1;
        int incrementValue = 1;
        boolean foundAnswer = false;

        while(!foundAnswer) {
            //Convert GDL to IDP
            parser = new GdlParser(gdlFile, amountOfTurns);
            parser.run();
            IFodotFile parsedFodotFile = parser.getFodotFile();
            parsedFodotFile = getOptimizer().improve(parsedFodotFile);

            //Create IDPfile in same location as GDL file
            File idpFile = IdpFileWriter.createIDPFileBasedOn(gdlFile);
            IdpFileWriter.writeToIDPFile(parsedFodotFile, idpFile);

            //Make IDP solve it
            IIdpCaller caller = new IdpCaller(false);
            String idpResult = caller.callIDP(idpFile);


            //TEMPORAL IDP BUG FIX TODO delete me when warning is fixed
            String stupidWarning = "Warning: XSB support is not available. Option xsb is ignored.\n\n";
            if (idpResult.contains(stupidWarning)) {
                idpResult = idpResult.replaceAll(stupidWarning, "");
            }

            //Process results
            try {
                IdpResultTransformer resultTransformer = new IdpResultTransformer(parsedFodotFile, idpResult);
                models = resultTransformer.getModels();
                foundAnswer = true;
            } catch (UnsatisfiableIdpFileException e) {
                amountOfTurns += incrementValue++;
            }
        }
		
		//Check if we found a model
		if (models.size() == 0) {
			throw new NoValidModelsException();
		}
		
		//Transform a solution 
		GdlAnswerCalculator answerer = new GdlAnswerCalculator(parser.getFodotTransformer(), parser.getFodotTransformer().getGdlVocabulary(), models);
		GdlActions actions = answerer.generateActionSequence();
		
		//Output it
		outputter.output(actions);
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
