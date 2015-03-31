package playid.domain.gdl_transformers.strategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.output.MoveSequence;
import playid.domain.exceptions.idp.IdpConnectionException;
import playid.domain.exceptions.idp.IdpNonOptimalSolutionException;
import playid.domain.exceptions.idp.NoValidModelsException;
import playid.domain.exceptions.idp.OutOfResourcesException;
import playid.domain.exceptions.idp.UnsatisfiableIdpFileException;
import playid.domain.fodot.file.IFodotFile;
import playid.domain.fodot.structure.FodotStructure;
import playid.util.IntegerTypeUtil;

public class SinglePlayerStrategy extends AbstractGameStrategy {

	public SinglePlayerStrategy(Game game, Role role) {
		super(game, role);
	}

	@Override
	public MoveSequence calculateNextMove(MoveSequence movesSoFar) {
		
		//TODO manier om de movessofar mee te geven aan de fodottransformer.
		return null;
	}

	public MoveSequence calculateBestSolution(File fodotFileOutput) throws IdpConnectionException, IOException {
		MoveSequence moves = null;
		int ownScore = -1;
		int amountOfTurns = 1;
		int incrementValue = 1;
		boolean foundAnswer = false;

		while (!foundAnswer) {
			IFodotFile parsedFodotFile = getFodotTransformer().buildFodot(amountOfTurns); 

			// Process results
			try {

				List<FodotStructure> models = generateModels(parsedFodotFile, fodotFileOutput);	
				
				// Check if we found a model
				if (models.size() > 0) {
					
					// Transform a solution
					
					FodotStructure firstModel = models.iterator().next();
					
					moves = getAnswerCalculator().extractMoveSequence(firstModel);
					ownScore = getAnswerCalculator().getScoreOf(getRole(), firstModel);
					if (ownScore == getFodotTransformer().getMaximumScoreInteger()) {
						foundAnswer = true;
					} else {
						//Increment maximum amount of turns
						amountOfTurns += incrementValue++;
					}
				} else {
					throw new NoValidModelsException();					
				}
			} catch (UnsatisfiableIdpFileException e) {
				amountOfTurns += incrementValue++;
			} catch (OutOfResourcesException e) {
				if (moves == null) {
					throw e;
				} else {
					throw new IdpNonOptimalSolutionException(
							ownScore, IntegerTypeUtil.extractValue(getFodotTransformer().getMaximumScore()));
				}
			}
		}

		return moves;
	}
}
