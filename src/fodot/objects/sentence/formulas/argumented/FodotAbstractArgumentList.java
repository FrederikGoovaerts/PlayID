package fodot.objects.sentence.formulas.argumented;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.sentence.IFodotSentenceElement;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.util.CollectionUtil;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;

public abstract class FodotAbstractArgumentList implements IFodotSentenceElement {

	private FodotArgumentListDeclaration declaration;
	private List<IFodotTerm> arguments;
	
	public FodotAbstractArgumentList(FodotArgumentListDeclaration decl, List<IFodotTerm> arguments) {
		super();
		setDeclaration(decl);
		this.arguments = arguments;
	}

	public FodotArgumentListDeclaration getDeclaration() {
		return declaration;
	}
	
	public void setDeclaration(FodotArgumentListDeclaration name) {
		this.declaration = name;
	}
	
	public FodotArgumentListDeclaration getName() {
		return declaration;
	}
	
	public List<IFodotTerm> getArguments() {
		return new ArrayList<IFodotTerm>(arguments);
	}

	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> result = new HashSet<FodotVariable>();
		for (IFodotTerm arg : getArguments()) {
			result.addAll(arg.getFreeVariables());
		}
		return result;
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append(CollectionUtil.toCouple(CollectionUtil.toCode(getArguments())));
		return builder.toString();
	}
	
	protected String argumentsToString() {
		return CollectionUtil.toNakedList(CollectionUtil.toString(getArguments()));
	}
	
	@Override
	public abstract String toString();

}
