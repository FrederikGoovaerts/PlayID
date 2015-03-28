package fodot.communication.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.statemachine.Move;

public class MoveSequence {
	private List<Map<GdlTerm, Move>> moves = new ArrayList<>();

	private MoveSequence(List<Map<GdlTerm, Move>> moves) {
		this.moves = moves;
	}
	
	public Move getMove(int time, GdlConstant player) {
		return this.moves.get(time).get(player);
	}
	
	public static MoveSequenceBuilder createBuilder() {
		return new MoveSequenceBuilder();
	}
	
	public static class MoveSequenceBuilder {
		private List<Map<GdlTerm, Move>> moves = new ArrayList<>();

		public void addMove(int time, GdlTerm player, GdlTerm action) {
			//CREATE IF NOT EXIST
			if (!hasInitiatedMovesOn(time)) {
				initiateMovesUntil(time);
			}
			
			Move move = createMove(player, action);
			
			//CHECK IF PLAYER DIDN'T DO A MOVE YET
			if (moves.get(time).containsKey(player) && !moves.get(time).get(player).equals(move))
			
			moves.get(time).put(player, move);
		}
		
		public MoveSequence buildMoveSequence() {
			return new MoveSequence(moves);
		}
		
		private Move createMove(GdlTerm player, GdlTerm action) {
			return new Move(GdlPool.getRelation(GdlPool.getConstant("does"), Arrays.asList(player, action)).toTerm());
		}
		
		private void initiateMovesUntil(int time) {
			for (int i = 0; i < time; i++) {
				if (moves.get(i) == null) {
					moves.set(i, new HashMap<GdlTerm, Move>());
				}
			}
		}

		private boolean hasInitiatedMovesOn(int time) {
			return moves.size() > time && moves.get(time) != null;
		}
		
	}
}
