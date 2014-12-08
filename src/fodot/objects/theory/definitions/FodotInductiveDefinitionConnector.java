package fodot.objects.theory.definitions;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.formulas.argumented.FodotPredicate;
import fodot.objects.sentence.formulas.binary.FodotSentenceElementConnector;

public class FodotInductiveDefinitionConnector extends FodotSentenceElementConnector<IFodotFormula> {

	private static final String INDUCTIVE_ARROW = "<-";
	
	public FodotInductiveDefinitionConnector(FodotPredicate head, IFodotFormula body) {
		super(head, INDUCTIVE_ARROW, body);		
	}
	
	public FodotInductiveDefinitionConnector(FodotInductiveFunction head, IFodotFormula body) {
		super(head, INDUCTIVE_ARROW, body);
	}
	
	@Override
	public boolean isValidConnector(String connector) {
		return connector.equals(INDUCTIVE_ARROW);
	}
}
