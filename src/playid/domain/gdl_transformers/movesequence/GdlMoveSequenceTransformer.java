package playid.domain.gdl_transformers.movesequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;

import playid.domain.fodot.FodotElementBuilder;
import playid.domain.fodot.structure.elements.predicateenum.FodotPredicateEnumeration;
import playid.domain.fodot.structure.elements.predicateenum.elements.FodotPredicateEnumerationElement;
import playid.domain.fodot.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.gdl_transformers.second_phase.GdlFodotSentenceTransformer;
import playid.domain.gdl_transformers.second_phase.GdlFodotTransformer;

public class GdlMoveSequenceTransformer {
	private final GdlFodotTransformer transformer;

	public GdlMoveSequenceTransformer(GdlFodotTransformer transformer) {
		this.transformer = transformer;
	}

	public FodotPredicateEnumeration translateMoveSequenceToFodotActions(
			MoveSequence moves) {

		List<IFodotPredicateEnumerationElement> fodotMoves = new ArrayList<>();
		GdlFodotSentenceTransformer actionTranslator = new GdlFodotSentenceTransformer(
				transformer);

		for (int i = 0; i < moves.getAmountOfMoves(); i++) {
			
			//Save the current time in a variable
			IFodotTypeEnumerationElement time = FodotElementBuilder
					.createInteger(i).toEnumerationElement();			
			
			for (Map.Entry<Role, Move> entry : moves.getMoves(i)) {
				
				//Extract GDL contents: player&action
				GdlConstant gdlPlayer = entry.getKey().getName();
				GdlTerm gdlAction = entry.getValue().getContents();

				//Translate gdl player&action to fodot player&action
				IFodotTypeEnumerationElement fodotPlayer = actionTranslator
						.generateConstant(gdlPlayer,
								transformer.getPlayerType())
						.toEnumerationElement();
				IFodotTypeEnumerationElement fodotAction = actionTranslator
						.generateTerm(gdlAction, transformer.getActionType())
						.toEnumerationElement();

				List<IFodotTypeEnumerationElement> predicateElements = Arrays
						.asList(time, fodotPlayer, fodotAction);

				// Add to moves
				fodotMoves.add(new FodotPredicateEnumerationElement(transformer
						.getDoPredicateDeclaration(), predicateElements));
			}
		}

		FodotPredicateEnumeration result =  new FodotPredicateEnumeration(
				transformer.getDoPredicateDeclaration(), fodotMoves);
		result.setCertainlyTrueFlag();
		
		return result;
	}
}
