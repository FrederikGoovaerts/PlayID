package playid;

import java.io.IOException;

import org.ggp.base.player.gamer.exception.GamePreviewException;
import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.match.Match;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.StateMachine;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.statemachine.implementation.prover.ProverStateMachine;

import playid.domain.PlayIdProcessor;
import playid.domain.gdl_transformers.movesequence.MoveSequence;

public class PlayIdPlayer extends StateMachineGamer {

	private PlayIdProcessor processor;
	
	@Override
	public StateMachine getInitialStateMachine() {
		 return new ProverStateMachine();
	}

	@Override
	public void stateMachineAbort() {
		//EMPTY
	}

	@Override
	public void stateMachineMetaGame(long arg0)
			throws TransitionDefinitionException, MoveDefinitionException,
			GoalDefinitionException {
		Match match = getMatch();
		processor = new PlayIdProcessor(match.getGame(), getRole());
	}

	@Override
	public Move stateMachineSelectMove(long arg0)
			throws TransitionDefinitionException, MoveDefinitionException,
			GoalDefinitionException {
		MoveSequence moveSequence;
		try {
			moveSequence = processor.calculateNextMove(MoveSequence.MoveSequenceBuilder.fromTermListList(getMatch().getMoveHistory()));
		} catch (IOException e) {
			throw new RuntimeException("PLAYID HAD AN EXCEPTION!", e);
		}
		return moveSequence.getMove(moveSequence.getAmountOfMoves()-1, getRoleName());
	}

	@Override
	public void stateMachineStop() {
		//EMPTY
	}

	@Override
	public String getName() {
		return "PlayId v0.1";
	}

	@Override
	public void preview(Game arg0, long arg1) throws GamePreviewException {
		//EMPTY
	}

}
