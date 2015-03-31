package playid.domain.gdl_transformers.strategy;

import playid.domain.gdl_transformers.movesequence.MoveSequence;

public interface IGameStrategy {
	MoveSequence calculateNextMove(MoveSequence movesSoFar);
	
}
