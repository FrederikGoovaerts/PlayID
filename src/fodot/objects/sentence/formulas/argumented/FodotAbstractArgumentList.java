package fodot.objects.sentence.formulas.argumented;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.exceptions.InvalidTermNameException;
import fodot.objects.sentence.IFodotSentenceElement;
import fodot.objects.sentence.terms.FodotTerm;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.util.TermsUtil;

public abstract class FodotAbstractArgumentList implements IFodotSentenceElement {

	private String name;
	private List<FodotTerm> arguments;
	
	public FodotAbstractArgumentList(String name, List<FodotTerm> arguments) {
		super();
		setName(name);
		this.arguments = arguments;
	}

	public void setName(String name) {
		if (!TermsUtil.isValidName(name)) {
			throw new InvalidTermNameException(name);
		}
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<FodotTerm> getArguments() {
		return new ArrayList<FodotTerm>(arguments);
	}

	@Override
	public List<FodotVariable> getFreeVariables() {
		List<FodotVariable> result = new ArrayList<FodotVariable>();
		for (FodotTerm arg : getArguments()) {
			result.addAll(arg.getFreeVariables());
		}
		return result;
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(name + "(");
		for (int i = 0; i < arguments.size(); i++) {
			if (i>0) {
				builder.append(", ");
			}
			builder.append(arguments.get(i).toCode());
		}
		builder.append(")");
		return builder.toString();
	}
	
	protected String argumentsToString() {
		StringBuilder builder = new StringBuilder();
		for (FodotTerm term : getArguments()) {
			builder.append(term.toString() + ", ");
		}
		String builderStr = builder.toString();
		return builderStr.substring(0, builderStr.length()-2);
	}
	
	@Override
	public abstract String toString();

}
