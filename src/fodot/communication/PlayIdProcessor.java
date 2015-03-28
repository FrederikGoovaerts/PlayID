package fodot.communication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import fodot.communication.input.IIdpCaller;
import fodot.communication.input.IdpCaller;
import fodot.communication.input.IdpFileWriter;
import fodot.communication.output.GdlActions;
import fodot.communication.output.GdlAnswerCalculator;
import fodot.communication.output.IdpResultTransformer;
import fodot.communication.output.MoveSequence;
import fodot.communication.output.MoveSequence.MoveSequenceBuilder;
import fodot.exceptions.idp.IdpConnectionException;
import fodot.exceptions.idp.IdpErrorException;
import fodot.exceptions.idp.IdpNonOptimalSolutionException;
import fodot.exceptions.idp.NoValidModelsException;
import fodot.exceptions.idp.OutOfResourcesException;
import fodot.exceptions.idp.UnsatisfiableIdpFileException;
import fodot.exceptions.playid.PlayIdArgumentException;
import fodot.gdl_parser.GdlParser;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.FodotStructure;

public class PlayIdProcessor {
	/**********************************************
	 *  Constructors
	 ***********************************************/

	private Game game;
	private Role role;
	private MoveSequence movesSoFar = new MoveSequenceBuilder().buildMoveSequence();
	//TODO een lijst dat de gespeelde moves bijhoudt

	public PlayIdProcessor(Game argGame, Role argRole) {
		this.game = argGame;
		this.role = argRole != null ? argRole : GdlParser.findFirstRole(argGame);
	}	

	public PlayIdProcessor(File gdlFile) {
		this(GdlParser.parseGame(gdlFile), null);
	}

	/**********************************************/




	/**********************************************
	 *  Process
	 ***********************************************/

	public GdlActions process(File gdlFile)
			throws IOException, IdpConnectionException,
			IdpErrorException, UnsatisfiableIdpFileException, IllegalStateException,
			NoValidModelsException 
	{
		List<FodotStructure> models = null;
		GdlParser parser = null;
		GdlActions actions = null;
		int amountOfTurns = 1;
		int incrementValue = 1;
		boolean foundAnswer = false;

		while(!foundAnswer) {
			//Convert GDL to IDP
			parser = new GdlParser(game, amountOfTurns);
			parser.run();
			IFodotFile parsedFodotFile = parser.getFodotFile();

			//Create IDPfile in same location as GDL file
			File idpFile = IdpFileWriter.createIDPFileBasedOn(gdlFile);
			IdpFileWriter.writeToIDPFile(parsedFodotFile, idpFile);

			//Make IDP solve it
			String idpResult = callIdp(idpFile);;

			//TEMPORAL IDP BUG FIX TODO delete me when warning is fixed
			idpResult = fixResult(idpResult);

			//Process results
			try {
				IdpResultTransformer resultTransformer = new IdpResultTransformer(parsedFodotFile, idpResult);
				models = resultTransformer.getModels();
				if(models.size()>0){
					//Transform a solution
					GdlAnswerCalculator answerer = new GdlAnswerCalculator(parser.getFodotTransformer(), parser.getFodotTransformer().getGdlVocabulary(), models);
					actions = answerer.generateActionSequence();
					if(actions.getScore() == actions.getMaximumScore()){
						foundAnswer = true;
					}
				}
				amountOfTurns += incrementValue++;
			} catch (UnsatisfiableIdpFileException e) {
				amountOfTurns += incrementValue++;
			} catch (OutOfResourcesException e) {
				if(actions == null){
					throw e;
				} else {
					throw new IdpNonOptimalSolutionException(actions.getScore() + "/" + actions.getMaximumScore());
				}
			}
		}

		//Check if we found a model
		if (models.size() == 0) {
			throw new NoValidModelsException();
		}

		return actions;
	}

	private String callIdp(File idpFile) throws IdpConnectionException, IOException {
		IIdpCaller caller = new IdpCaller(false);
		String idpResult = caller.callIDP(idpFile);
		return idpResult;
	}

	private String fixResult(String idpResult) {
		String stupidWarning = "Warning: XSB support is not available. Option xsb is ignored.\n\n";
		if (idpResult.contains(stupidWarning)) {
			idpResult = idpResult.replaceAll(stupidWarning, "");
		}
		return idpResult;
	}

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

		PlayIdProcessor processor = new PlayIdProcessor(gdlFile);
		System.out.println(processor.process(gdlFile));
	}


	/**********************************************/


}
