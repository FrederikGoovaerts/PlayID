package playid.domain.gdl_transformers.strategy;

import playid.domain.communication.output.MoveSequence;
import playid.domain.gdl_transformers.second_phase.GdlFodotTransformer;

public interface IGameTranslationStrategy {
	MoveSequence playGame(MoveSequence movesSoFar,
			GdlFodotTransformer transformer);
}
