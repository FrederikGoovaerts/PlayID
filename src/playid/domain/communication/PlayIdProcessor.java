package playid.domain.communication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.input.IIdpCaller;
import playid.domain.communication.input.IdpCaller;
import playid.domain.communication.input.IdpFileWriter;
import playid.domain.communication.output.GdlActions;
import playid.domain.communication.output.GdlAnswerCalculator;
import playid.domain.communication.output.IdpResultTransformer;
import playid.domain.communication.output.MoveSequence;
import playid.domain.communication.output.MoveSequence.MoveSequenceBuilder;
import playid.domain.exceptions.idp.IdpConnectionException;
import playid.domain.exceptions.idp.IdpErrorException;
import playid.domain.exceptions.idp.IdpNonOptimalSolutionException;
import playid.domain.exceptions.idp.NoValidModelsException;
import playid.domain.exceptions.idp.OutOfResourcesException;
import playid.domain.exceptions.idp.UnsatisfiableIdpFileException;
import playid.domain.exceptions.playid.PlayIdArgumentException;
import playid.domain.fodot.file.IFodotFile;
import playid.domain.fodot.structure.FodotStructure;
import playid.domain.gdl_transformers.GdlParser;
import playid.util.IntegerTypeUtil;

public class PlayIdProcessor {
	/**********************************************
	 * Constructors
	 ***********************************************/

	private Game game;
	private Role role;
	private MoveSequence movesSoFar = new MoveSequenceBuilder()
			.buildMoveSequence();

	public PlayIdProcessor(Game argGame, Role argRole) {
		this.game = argGame;
		this.role = argRole != null ? argRole : GdlParser
				.findFirstRole(argGame);
	}

	public PlayIdProcessor(File gdlFile) {
		this(GdlParser.parseGame(gdlFile), null);
	}

	/**********************************************/

	/**********************************************
	 * Process
	 ***********************************************/

	public GdlActions process(File gdlFile) throws IOException,
			IdpConnectionException, IdpErrorException,
			UnsatisfiableIdpFileException, IllegalStateException,
			NoValidModelsException {
		List<FodotStructure> models = null;
		GdlParser parser = null;
		GdlActions actions = null;
		int amountOfTurns = 1;
		int incrementValue = 1;
		boolean foundAnswer = false;

		while (!foundAnswer) {
			// Convert GDL to IDP
			parser = new GdlParser(game, amountOfTurns);

			// TODO: Rename parser to translator.
			// Give role and moves so far along!

			parser.run();
			IFodotFile parsedFodotFile = parser.getFodotFile();

			// Create IDPfile in same location as GDL file
			File idpFile = IdpFileWriter.createIDPFileBasedOn(gdlFile);
			IdpFileWriter.writeToIDPFile(parsedFodotFile, idpFile);

			// Make IDP solve it
			String idpResult = callIdp(idpFile);

			// TEMPORAL IDP BUG FIX TODO delete me when warning is fixed
			idpResult = fixResult(idpResult);

			// Process results
			try {
				IdpResultTransformer resultTransformer = new IdpResultTransformer(
						parsedFodotFile, idpResult);
				models = resultTransformer.getModels();
				if (models.size() > 0) {
					// Transform a solution
					GdlAnswerCalculator answerer = new GdlAnswerCalculator(
							role, parser
									.getFodotTransformer().getGdlVocabulary(),
							models);
					actions = answerer.generateActionSequence();
					if (actions.getScore() == IntegerTypeUtil.extractValue(parser.getFodotTransformer().getMaximumScore())) {
						foundAnswer = true;
					}
				}
				amountOfTurns += incrementValue++;
			} catch (UnsatisfiableIdpFileException e) {
				amountOfTurns += incrementValue++;
			} catch (OutOfResourcesException e) {
				if (actions == null) {
					throw e;
				} else {
					throw new IdpNonOptimalSolutionException(
							actions.getScore(), IntegerTypeUtil.extractValue(parser.getFodotTransformer().getMaximumScore()));
				}
			}
		}

		// Check if we found a model
		if (models.size() == 0) {
			throw new NoValidModelsException();
		}

		return actions;
	}
	
	
	

	private String callIdp(File idpFile) throws IdpConnectionException,
			IOException {
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
	 * Main method
	 ***********************************************/

	public static void main(String[] args) throws IOException {
		validateArguments(args);
		File gdlFile = new File(args[0]);
		PlayIdProcessor processor = new PlayIdProcessor(gdlFile);
		System.out.println(processor.process(gdlFile));
	}
	
	public static void validateArguments(String[] args) {
		if (args.length <= 0) {
			throw new PlayIdArgumentException(
					"Please give the uri of a valid GDL file to the PlayID processor.",
					0, 1);
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
	}

	/**********************************************/

}
