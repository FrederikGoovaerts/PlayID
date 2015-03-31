package playid.domain.gdl_transformers.movesequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.statemachine.Move;

import playid.domain.fodot.FodotElementBuilder;
import playid.domain.fodot.structure.elements.predicateenum.FodotPredicateEnumeration;
import playid.domain.fodot.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.gdl_transformers.second_phase.GdlFodotSentenceTransformer;
import playid.domain.gdl_transformers.second_phase.GdlFodotTransformer;

public class GdlMoveSequenceTransformer {
	private final GdlFodotTransformer transformer;

	public GdlMoveSequenceTransformer(GdlFodotTransformer vocabulary) {
		this.transformer = vocabulary;
	}

	public FodotPredicateEnumeration translateMoveSequenceToFodotActions(
			MoveSequence moves) {

		List<IFodotPredicateEnumerationElement> fodotMoves = new ArrayList<>();

		GdlFodotSentenceTransformer actionTranslator = new GdlFodotSentenceTransformer(
				transformer);

		for (int i = 0; i < moves.getAmountOfMoves(); i++) {
			IFodotTypeEnumerationElement time = FodotElementBuilder
					.createInteger(i).toEnumerationElement();
			Collection<Move> movesAtMoment = moves.getMoves(i);
			for (Move move : movesAtMoment) {
				GdlTerm doPred = move.getContents();
				GdlConstant player = (GdlConstant) doPred.toSentence().get(0);
				GdlTerm action = doPred.toSentence().get(1);

				IFodotTypeEnumerationElement fodotPlayer = actionTranslator
						.generateConstant(player, transformer.getPlayerType()).toEnumerationElement();
				IFodotTypeEnumerationElement fodotAction =
						actionTranslator.generateTerm(action, transformer.getActionType()).toEnumerationElement();
			}
		}

		return new FodotPredicateEnumeration(
				transformer.getDoPredicateDeclaration(), fodotMoves);
	}
}
