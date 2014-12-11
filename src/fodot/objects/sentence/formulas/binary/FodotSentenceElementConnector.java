package fodot.objects.sentence.formulas.binary;

import java.util.Set;

import fodot.objects.exceptions.IllegalConnectorException;
import fodot.objects.sentence.IFodotSentenceElement;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public abstract class FodotSentenceElementConnector<E extends IFodotSentenceElement> implements IFodotFormula {

	private E arg1;
	private E arg2;
	private String connector;
	
	public FodotSentenceElementConnector(E arg1, String connector, E arg2) {
		super();
		if (!isValidConnector(connector)) {
			throw new IllegalConnectorException(this, connector);
		}
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.connector = connector;
	}
	
	public E getArgument1() {
		return arg1;
	}

	public E getArgument2() {
		return arg2;
	}

	public String getConnector() {
		return connector;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> form1vars = getArgument1().getFreeVariables();
		Set<FodotVariable> form2vars = getArgument2().getFreeVariables();
		form1vars.addAll(form2vars);
		return form1vars;
	}

	@Override
	public String toCode() {
		return "(" + getArgument1().toCode() + " " + getConnector() + " " + getArgument2().toCode() + ")";
	}	
	
	public abstract boolean isValidConnector(String connector);

}
