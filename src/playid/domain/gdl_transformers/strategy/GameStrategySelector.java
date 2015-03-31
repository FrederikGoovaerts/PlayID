package playid.domain.gdl_transformers.strategy;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

public class GameStrategySelector {
	public IGameStrategy selectStrategy(Game game, Role role) {
		return new SinglePlayerStrategy(game, role);
	}
}
