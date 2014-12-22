package fodot.objects.sentence.formulas.argumented;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.exceptions.IllegalAmountOfArguments;
import fodot.exceptions.IllegalTypeException;
import fodot.objects.sentence.IFodotSentenceElement;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.CollectionPrinter;

public abstract class FodotAbstractArgumentList implements IFodotSentenceElement {

	private FodotArgumentListDeclaration declaration;
	private List<IFodotTerm> arguments;
	
	public FodotAbstractArgumentList(FodotArgumentListDeclaration decl, List<IFodotTerm> arguments) {
		super();
		setDeclaration(decl);
		setArguments(arguments);
	}

	//Declaration
	public FodotArgumentListDeclaration getDeclaration() {
		return declaration;
	}
	
	public void setDeclaration(FodotArgumentListDeclaration decl) {
		this.declaration = decl;
	}
	
	//Name
	public String getName() {
		return declaration.getName();
	}
	
	
	//Arguments
	public List<IFodotTerm> getArguments() {
		return new ArrayList<IFodotTerm>(arguments);
	}
	 
	public boolean hasArguments() {
		return !arguments.isEmpty();
	}
	
	private void setArguments(List<IFodotTerm> args) {
		this.arguments = (args == null ? new ArrayList<IFodotTerm>() : args);

		
		List<FodotType> argumentTypes = getDeclaration().getArgumentTypes();
		//Check amount of arguments
		if (arguments.size() != argumentTypes.size()) {
			throw new IllegalAmountOfArguments(arguments.size(), argumentTypes.size());
		}
		
		//Check types of arguments

//		for (int i = 0; i < arguments.size(); i++) {
//			if (!arguments.get(i).getType().isASubtypeOf(argumentTypes.get(i))) {
//				throw new IllegalTypeException("Argument " + i + " in " + getName(), arguments.get(i).getType(), argumentTypes.get(i));
//			}
//		}

		for (int i = 0; i < arguments.size(); i++) {
			FodotType givenType = arguments.get(i).getType();
			FodotType expectedType = argumentTypes.get(i);
			
			//Check if the types are "linked"
			if (!givenType.isASubtypeOf(expectedType)
					&& !expectedType.isASubtypeOf(givenType)) {
				throw new IllegalTypeException("Argument " + i + " in " + getName(), arguments.get(i).getType(), argumentTypes.get(i));
			}
		}
	}
	
	//Sentence element stuff

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
		return getName() + (hasArguments() ? CollectionPrinter.toCouple(CollectionPrinter.toCode(getArguments())) : "");
	}
	
	protected String argumentsToString() {
		return CollectionPrinter.toNakedList(CollectionPrinter.toString(getArguments()));
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
