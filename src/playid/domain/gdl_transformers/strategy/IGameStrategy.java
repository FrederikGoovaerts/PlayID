package playid.domain.gdl_transformers.strategy;

import playid.domain.communication.output.MoveSequence;

public interface IGameStrategy {
	MoveSequence calculateNextMove(MoveSequence movesSoFar);
	
}
