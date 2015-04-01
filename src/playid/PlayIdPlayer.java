package playid;

import java.io.IOException;
import java.util.List;

import org.ggp.base.player.gamer.exception.GamePreviewException;
import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.match.Match;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
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
		try {
			List<List<GdlTerm>> matchHistory = getMatch().getMoveHistory();
			List<Role> roles = getStateMachine().getRoles();
			
			MoveSequence moveHistory = MoveSequence.MoveSequenceBuilder.fromTermListList(matchHistory, roles);
			MoveSequence plannedMoveSequence = processor.calculateNextMove(moveHistory);
			
			log(plannedMoveSequence);
			
			Move moveToPlay = plannedMoveSequence.getMove(matchHistory.size(), getRole());
			return new Move(moveToPlay.getContents());
		} catch (IOException e) {
			throw new RuntimeException("PLAYID HAD AN EXCEPTION!", e);
		}
	}

	@Override
	public void stateMachineStop() {
		//EMPTY
	}

	@Override
	public String getName() {
		return "PlayID v0.1";
	}

	@Override
	public void preview(Game arg0, long arg1) throws GamePreviewException {
		//EMPTY
	}
	
	
	private void log(Object msg) {
		System.out.println("PLAYID: " + msg.toString());
	} 

}
