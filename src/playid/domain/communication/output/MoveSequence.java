package playid.domain.communication.output;

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
		if (this.moves.size() < time) {
			throw new IllegalStateException("No moves recorded for time "
					+ time + " yet.");
		}
		if (!this.moves.get(time).containsKey(player)) {
			throw new IllegalStateException("No moves recorded for " + player
					+ " at time " + time + " yet.");
		}

		return this.moves.get(time).get(player);
	}

	public MoveSequence plus(int time, GdlTerm player, GdlTerm action) {
		MoveSequenceBuilder builder = createBuilder(this);
		builder.addMove(time, player, action);
		return builder.buildMoveSequence();
	}

	public static MoveSequenceBuilder createBuilder() {
		return new MoveSequenceBuilder();
	}

	public static MoveSequenceBuilder createBuilder(MoveSequence seq) {
		List<Map<GdlTerm, Move>> copyMoves = new ArrayList<>();
		for (int i = 0; i < seq.moves.size(); i++) {
			copyMoves.set(i, new HashMap<GdlTerm, Move>());
			for (Map.Entry<GdlTerm, Move> entry : seq.moves.get(i).entrySet()) {
				copyMoves.get(i).put(entry.getKey(), entry.getValue());
			}
		}
		return new MoveSequenceBuilder(copyMoves);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.size(); i++) {
			for (Move move : moves.get(i).values()) {
				builder.append(i + ": " + move.getContents() + "\n");
			}
		}
		return builder.toString();
	}

	// BUILDER
	public static class MoveSequenceBuilder {
		private List<Map<GdlTerm, Move>> moves;

		public MoveSequenceBuilder(List<Map<GdlTerm, Move>> moves) {
			this.moves = moves;
		}

		public MoveSequenceBuilder() {
			this(new ArrayList<>());
		}

		public void addMove(int time, GdlTerm player, GdlTerm action) {
			// CREATE IF NOT EXIST
			if (!hasInitiatedMovesOn(time)) {
				initiateMovesUntil(time);
			}

			Move move = createMove(player, action);

			// CHECK IF PLAYER DIDN'T DO A MOVE YET
			if (moves.size() > time && moves.get(time).containsKey(player)
					&& !moves.get(time).get(player).equals(move))

				moves.get(time).put(player, move);
		}

		public MoveSequence buildMoveSequence() {
			return new MoveSequence(moves);
		}

		private Move createMove(GdlTerm player, GdlTerm action) {
			return new Move(GdlPool.getRelation(GdlPool.getConstant("does"),
					Arrays.asList(player, action)).toTerm());
		}

		private void initiateMovesUntil(int time) {
			for (int i = 0; i < time; i++) {
				if (moves.size() <= i || moves.get(i) == null) {
					if (moves.size() == i) {
						moves.add(new HashMap<GdlTerm, Move>());
					} else {
						moves.set(i, new HashMap<GdlTerm, Move>());
					}
				}
			}
		}

		private boolean hasInitiatedMovesOn(int time) {
			return moves.size() > time && moves.get(time) != null;
		}

	}
}
