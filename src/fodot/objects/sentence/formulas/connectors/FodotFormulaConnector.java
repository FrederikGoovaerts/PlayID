package fodot.objects.sentence.formulas.connectors;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.util.CollectionUtil;

public class FodotFormulaConnector extends FodotSentenceElementConnector<IFodotFormula> implements IFodotFormula {

	
	public FodotFormulaConnector(String connector, Collection<? extends IFodotFormula> formulas) {
		super(connector, formulas);
	}
	
	public FodotFormulaConnector(String connector, IFodotFormula... formulas) {
		this(connector, Arrays.asList(formulas));
	}
	
	public FodotFormulaConnector(IFodotFormula formula1, String connector, IFodotFormula formula2) {
		this(connector, Arrays.asList(new IFodotFormula[]{formula1, formula2}));
	}

	/* VALID CONNECTORS */
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"&", "|",  "=>", "<=", "<=>"});
	private static final List<String> ASSOCIATIVE_CONNECTORS =
			Arrays.asList(new String[]{"&", "|"});

	public boolean isValidConnector(String connector) {
		return VALID_CONNECTORS.contains(connector);
	}

	@Override
	public String toString() {
		return "[formulaconnector "+getConnector()+ ":" + CollectionUtil.toNakedList(CollectionUtil.toString(getArguments())) + "]";
	}

	@Override
	protected boolean isAssociativeConnector(String connector) {
		return ASSOCIATIVE_CONNECTORS.contains(connector);
	}
	
	

}
