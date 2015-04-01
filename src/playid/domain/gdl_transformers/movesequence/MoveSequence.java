package playid.domain.gdl_transformers.movesequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;

public class MoveSequence {
	private static final MoveSequence EMPTY_MOVESEQUENCE = MoveSequence
			.createBuilder().buildMoveSequence();

	private List<Map<Role, Move>> moves = new ArrayList<>();

	private MoveSequence(List<Map<Role, Move>> moves) {
		this.moves = moves;
	}

	public Move getMove(int time, Role player) {
		if (this.moves.size() < time) {
			throw new IllegalStateException("No moves recorded for time "
					+ time + " yet.");
		}
		if (!this.moves.get(time).containsKey(player)) {
			throw new IllegalStateException("No moves recorded for " + player
					+ " at time " + time + " yet in " + this);
		}

		return this.moves.get(time).get(player);
	}

	public Collection<Map.Entry<Role,Move>> getMoves(int time) {
		if (this.moves.size() < time) {
			throw new IllegalStateException("No moves recorded for time "
					+ time + " yet.");
		}
		return this.moves.get(time).entrySet();
	}

	public MoveSequence plus(int time, GdlConstant player, GdlTerm action) {
		MoveSequenceBuilder builder = createBuilder(this);
		builder.addMove(time, player, action);
		return builder.buildMoveSequence();
	}

	public int getAmountOfMoves() {
		return moves.size();
	}

	public static MoveSequence empty() {
		return EMPTY_MOVESEQUENCE;
	}

	public static MoveSequenceBuilder createBuilder() {
		return new MoveSequenceBuilder();
	}

	public static MoveSequenceBuilder createBuilder(MoveSequence seq) {
		List<Map<Role, Move>> copyMoves = new ArrayList<>();
		for (int i = 0; i < seq.moves.size(); i++) {
			copyMoves.set(i, new HashMap<Role, Move>());
			for (Map.Entry<Role, Move> entry : seq.moves.get(i).entrySet()) {
				copyMoves.get(i).put(entry.getKey(), entry.getValue());
			}
		}
		return new MoveSequenceBuilder(copyMoves);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Moves:\n");
		for (int i = 0; i < moves.size(); i++) {
			for (Map.Entry<Role,Move> entry : moves.get(i).entrySet()) {
				builder.append(i + ": " + entry.getKey() + ": " + entry.getValue() + "\n");
			}
		}
		builder.append("\n");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moves == null) ? 0 : moves.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoveSequence other = (MoveSequence) obj;
		if (moves == null) {
			if (other.moves != null)
				return false;
		} else if (!moves.equals(other.moves))
			return false;
		return true;
	}



	// BUILDER
	public static class MoveSequenceBuilder {
		private List<Map<Role, Move>> tempMoves;

		public MoveSequenceBuilder(List<Map<Role, Move>> moves) {
			this.tempMoves = moves;
		}

		public MoveSequenceBuilder() {
			this(new ArrayList<>());
		}

		public static MoveSequence fromTermListList(List<List<GdlTerm>> moves, List<Role> roles) {
			MoveSequenceBuilder builder = new MoveSequenceBuilder();
			for (int i = 0; i < moves.size(); i++) {
				List<GdlTerm> actionsAtMoment = moves.get(i);
				for (int j = 0; j < moves.get(i).size(); j++) {
					GdlConstant player = roles.get(j).getName();
					GdlTerm action = actionsAtMoment.get(j);

					builder.addMove(i, player, action);
				}
			}
			return builder.buildMoveSequence();
		}

		public void addMove(int time, GdlConstant player, GdlTerm action) {
			// CREATE IF NOT EXIST
			if (!hasInitiatedMovesOn(time)) {
				initiateMovesUntil(time);
			}

			Move move = new Move(action);

			// CHECK IF PLAYER DIDN'T DO A MOVE YET
			if (tempMoves.size() > time && tempMoves.get(time).containsKey(player)
					&& !tempMoves.get(time).get(player).equals(move)) {
				throw new IllegalStateException("Player already has a move");
			}

			tempMoves.get(time).put(new Role(player), move);
		}

		public MoveSequence buildMoveSequence() {
			MoveSequence result = new MoveSequence(tempMoves);
			return result;
		}
		
		private void initiateMovesUntil(int time) {
			for (int i = 0; i <= time; i++) {
				if (tempMoves.size() <= i || tempMoves.get(i) == null) {
					if (tempMoves.size() == i) {
						tempMoves.add(new HashMap<Role, Move>());
					} else {
						tempMoves.set(i, new HashMap<Role, Move>());
					}
				}
			}
		}

		private boolean hasInitiatedMovesOn(int time) {
			return tempMoves.size() > time && tempMoves.get(time) != null;
		}

	}
}
