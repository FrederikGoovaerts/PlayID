package playid.domain.gdl_transformers.strategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.input.IdpFileWriter;
import playid.domain.communication.output.IdpResultTransformer;
import playid.domain.communication.output.MoveSequence;
import playid.domain.exceptions.idp.IdpConnectionException;
import playid.domain.exceptions.idp.IdpNonOptimalSolutionException;
import playid.domain.exceptions.idp.NoValidModelsException;
import playid.domain.exceptions.idp.OutOfResourcesException;
import playid.domain.exceptions.idp.UnsatisfiableIdpFileException;
import playid.domain.fodot.file.IFodotFile;
import playid.domain.fodot.structure.FodotStructure;
import playid.domain.gdl_transformers.GdlParser;
import playid.util.IntegerTypeUtil;

public class SinglePlayerStrategy extends AbstractGameStrategy {
	
	public SinglePlayerStrategy(Game game, Role role) {
		super(game, role);
	}

	@Override
	public MoveSequence calculateNextMove(MoveSequence movesSoFar) {
		return null;
	}

	public MoveSequence calculateBestSolution(File fodotFileOutput) throws IdpConnectionException, IOException {
		GdlParser parser = null;
		MoveSequence actions = null;
		int ownScore = -1;
		int amountOfTurns = 1;
		int incrementValue = 1;
		boolean foundAnswer = false;

		while (!foundAnswer) {
			// Convert GDL to IDP
			parser = new GdlParser(getGame(), amountOfTurns);

			// TODO: Rename parser to translator.
			// Give role and moves so far along!

			parser.run();
			IFodotFile parsedFodotFile = parser.getFodotFile();

			// Create IDPfile in same location as GDL file
			IdpFileWriter.writeToIDPFile(parsedFodotFile, fodotFileOutput);

			// Make IDP solve it
			String idpResult = callIdp(fodotFileOutput);

			// TEMPORAL IDP BUG FIX TODO delete me when warning is fixed
			idpResult = fixResult(idpResult);

			// Process results
			try {
				IdpResultTransformer resultTransformer = new IdpResultTransformer(
						parsedFodotFile, idpResult);
				List<FodotStructure> models = resultTransformer.getModels();

				// Check if we found a model
				if (models.size() > 0) {
					// Transform a solution
					FodotStructure firstModel = models.iterator().next();
					actions = getAnswerCalculator().extractMoveSequence(firstModel);
					ownScore = getAnswerCalculator().getScoreOf(getRole(), firstModel);
					if (ownScore == IntegerTypeUtil.extractValue(parser.getFodotTransformer().getMaximumScore())) {
						foundAnswer = true;
					}
				} else {
					throw new NoValidModelsException();					
				}
				amountOfTurns += incrementValue++;
			} catch (UnsatisfiableIdpFileException e) {
				amountOfTurns += incrementValue++;
			} catch (OutOfResourcesException e) {
				if (actions == null) {
					throw e;
				} else {
					throw new IdpNonOptimalSolutionException(
							ownScore, IntegerTypeUtil.extractValue(parser.getFodotTransformer().getMaximumScore()));
				}
			}
		}

		return actions;
	}
}
