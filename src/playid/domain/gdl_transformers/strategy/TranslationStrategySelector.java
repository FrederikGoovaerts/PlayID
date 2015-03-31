package playid.domain.gdl_transformers.strategy;

import org.ggp.base.util.game.Game;

public class TranslationStrategySelector {
	public IGameTranslationStrategy selectStrategy(Game game) {
		return new SinglePlayerTranslationStrategy();
	}
}
