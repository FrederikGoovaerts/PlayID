package playid.domain.gdl_transformers.strategy;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

public class TranslationStrategySelector {
	public IGameTranslationStrategy selectStrategy(Game game, Role role) {
		return new SinglePlayerTranslationStrategy(game, role);
	}
}
