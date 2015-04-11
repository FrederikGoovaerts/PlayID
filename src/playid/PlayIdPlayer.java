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
	
	public PlayIdPlayer() {
		importantLog("A NEW PLAYER WAS CREATED");
	}
	
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
		createProcessor(match.getGame());
	}
	
	@Override
	public Move stateMachineSelectMove(long arg0)
			throws TransitionDefinitionException, MoveDefinitionException,
			GoalDefinitionException {
		log("A new move is requested");
		try {
			List<List<GdlTerm>> matchHistory = getMatch().getMoveHistory();
			List<Role> roles = getStateMachine().getRoles();
			
			MoveSequence moveHistory = MoveSequence.MoveSequenceBuilder.fromTermListList(matchHistory, roles);
			log("HISTORY:", moveHistory.toString().trim());
			MoveSequence plannedMoveSequence = processor.calculateNextMove(moveHistory);
			
			log("PLANNED:", plannedMoveSequence.toString().trim());
			
			Move moveToPlay = plannedMoveSequence.getMove(matchHistory.size(), getRole());
			
			log("SUBMIT MOVE", moveToPlay.getContents());
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
	public void preview(Game game, long time) throws GamePreviewException {
		
		createProcessor(game);
	}
	
	public void createProcessor(Game game) {
		if (processor == null || !processor.getGame().equals(game)) {
			importantLog(" A NEW PROCESSOR HAS BEEN SET");
			processor = new PlayIdProcessor(game, getRole());
		}
	}
	
	private void importantLog(String message) {
		System.err.println("PLAYID: " + message);
	}
	
	private void log(Object... messages) {
		System.out.println("______________PLAYID______________");
		for (Object msg : messages) {
			System.out.println( msg.toString());
		}
		System.out.println("같같같같같같같ENDMSG같같같같같같같\n\n");
	} 

}
