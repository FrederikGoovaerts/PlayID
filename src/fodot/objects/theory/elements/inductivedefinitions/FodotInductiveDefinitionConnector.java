package fodot.objects.theory.elements.inductivedefinitions;

import java.util.Arrays;

import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.FodotSentenceElementConnector;
import fodot.objects.theory.elements.formulas.IFodotFormula;

public class FodotInductiveDefinitionConnector extends FodotSentenceElementConnector<IFodotFormula> implements IFodotInductiveDefinitionElement {

	private static final String INDUCTIVE_ARROW = "<-";
	
	/**
	 * @param arrow the reason this parameter is given is so that it would differentiate itself from the other constructors
	 * (Java compiler didn't like the inheritance of the other two first arguments with IFodotFormula)
	 * So this is a mock argument that no one will see
	 * @param head
	 * @param body
	 */
	private FodotInductiveDefinitionConnector(String arrow, IFodotFormula head, IFodotFormula body) {
		super(arrow, Arrays.asList(new IFodotFormula[]{head, body}));	
		setShouldPrintBrackets(false);
	}
	
	public FodotInductiveDefinitionConnector(FodotPredicate head, IFodotFormula body) {
		this(INDUCTIVE_ARROW, head, body);
	}
	
	public FodotInductiveDefinitionConnector(FodotInductiveFunction head, IFodotFormula body) {
		this(INDUCTIVE_ARROW, head, body);
	}
	
	@Override
	public boolean isValidConnector(String connector) {
		return connector.equals(INDUCTIVE_ARROW);
	}

	@Override
	protected boolean isAssociativeConnector(String connector) {
		return false;
	}
}
