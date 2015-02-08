package fodot.objects.theory.elements.formulas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.exceptions.fodot.IllegalAmountOfArgumentsException;
import fodot.exceptions.fodot.InvalidTypeException;
import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.IFodotSentenceElement;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.CollectionPrinter;

public abstract class FodotArgumentList extends FodotElement implements IFodotSentenceElement {
	
	private static final int BINDING_ORDER = -1;
	
	private FodotArgumentListDeclaration declaration;
	private List<IFodotTerm> arguments;
	
	public FodotArgumentList(FodotArgumentListDeclaration decl, List<IFodotTerm> arguments) {
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
	
	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return getArguments();
	}
	 
	public boolean hasArguments() {
		return !arguments.isEmpty();
	}
	
	private void setArguments(List<IFodotTerm> args) {
		this.arguments = (args == null ? new ArrayList<IFodotTerm>() : args);

		
		List<FodotType> argumentTypes = getDeclaration().getArgumentTypes();
		//Check amount of arguments
		if (arguments.size() != argumentTypes.size()) {
			throw new IllegalAmountOfArgumentsException(this, arguments.size(), argumentTypes.size());
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
				throw new InvalidTypeException("Argument " + i + " in " + getName(), arguments.get(i).getType(), argumentTypes.get(i));
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
	public int getBindingOrder() {
		return BINDING_ORDER;
	}

	@Override
	public String toCode() {
		return getName() + (hasArguments() ? CollectionPrinter.toCouple(CollectionPrinter.toCode(getArguments(), getBindingOrder())) : "");
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
		FodotArgumentList other = (FodotArgumentList) obj;
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
