package playid.domain.gdl_transformers.strategy;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.output.MoveSequence;
import playid.domain.fodot.file.IFodotFile;
import playid.domain.gdl_transformers.second_phase.GdlFodotTransformer;

public interface IGameTranslationStrategy {
	IFodotFile translateGame(Game game, Role ownRole, MoveSequence movesSoFar, GdlFodotTransformer transformer);
}
