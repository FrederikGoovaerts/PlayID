package playid.domain.gdl_transformers.strategy;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.output.MoveSequence;

public class SinglePlayerStrategy extends AbstractGameStrategy {
	
	public SinglePlayerStrategy(Game game, Role role) {
		super(game, role);
	}

	@Override
	public MoveSequence calculateNextMove(MoveSequence movesSoFar) {
		return null;
	}

}
