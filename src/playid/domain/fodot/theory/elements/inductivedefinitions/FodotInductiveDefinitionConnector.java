package playid.domain.fodot.theory.elements.inductivedefinitions;

import java.util.Arrays;

import playid.domain.fodot.theory.elements.formulas.FodotPredicate;
import playid.domain.fodot.theory.elements.formulas.FodotSentenceElementConnector;
import playid.domain.fodot.theory.elements.formulas.FodotTermComparator;
import playid.domain.fodot.theory.elements.formulas.IFodotFormula;
import playid.domain.fodot.theory.elements.terms.FodotFunction;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;

public class FodotInductiveDefinitionConnector extends FodotSentenceElementConnector<IFodotFormula> implements IFodotInductiveDefinitionElement {

	private static final int BINDING_ORDER = -1;
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
	}
	
	public FodotInductiveDefinitionConnector(FodotPredicate head, IFodotFormula body) {
		this(INDUCTIVE_ARROW, head, body);
	}

	public FodotInductiveDefinitionConnector(FodotInductiveFunction head, IFodotFormula body) {
		this(INDUCTIVE_ARROW, head, body);
	}

	public FodotInductiveDefinitionConnector(FodotFunction function, IFodotTerm value, IFodotFormula body) {
		this(INDUCTIVE_ARROW, new FodotTermComparator(function, "=", value), body);
	}
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
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
