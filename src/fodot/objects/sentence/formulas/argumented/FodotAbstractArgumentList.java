package fodot.objects.sentence.formulas.argumented;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.sentence.IFodotSentenceElement;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;
import fodot.util.CollectionUtil;

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
		return getName() + CollectionUtil.toCouple(CollectionUtil.toCode(getArguments()));
	}
	
	protected String argumentsToString() {
		return CollectionUtil.toNakedList(CollectionUtil.toString(getArguments()));
	}
	
	@Override
	public abstract String toString();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result
				+ ((declaration == null) ? 0 : declaration.hashCode());
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
		FodotAbstractArgumentList other = (FodotAbstractArgumentList) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		return true;
	}

}
