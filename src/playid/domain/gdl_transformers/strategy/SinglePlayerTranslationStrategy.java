package playid.domain.gdl_transformers.strategy;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.output.MoveSequence;
import playid.domain.gdl_transformers.second_phase.GdlFodotTransformer;

public class SinglePlayerTranslationStrategy extends AbstractTranslationStrategy {
	
	public SinglePlayerTranslationStrategy(Game game, Role role) {
		super(game, role);
	}

	@Override
	public MoveSequence playGame(MoveSequence movesSoFar, GdlFodotTransformer transformer) {
		return null;
	}

}
