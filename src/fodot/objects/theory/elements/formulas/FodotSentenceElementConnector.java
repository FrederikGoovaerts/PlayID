package fodot.objects.theory.elements.formulas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.exceptions.IllegalConnectorException;
import fodot.objects.theory.elements.IFodotSentenceElement;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.util.CollectionPrinter;

public abstract class FodotSentenceElementConnector<E extends IFodotSentenceElement> implements IFodotSentenceElement {

	private List<E> arguments;
	private String connector;

	public FodotSentenceElementConnector(String connector, Collection<? extends E> args) {
		super();
		if (!isValidConnector(connector)) {
			throw new IllegalConnectorException(this, connector);
		} 
		if (!isAssociativeConnector(connector) && args.size() > 2) {
			throw new RuntimeException(connector
					+ " has way more than two arguments, which it doesn't seem to be able to handle. "
					+ CollectionPrinter.toCouple(CollectionPrinter.toCode(args)));
		}
		this.connector = connector;	
		this.arguments = new ArrayList<E>();	


		for (E arg : args) {
			if (isMergeableWith(arg)) {
				@SuppressWarnings("unchecked")
				FodotSentenceElementConnector<E> casted = (FodotSentenceElementConnector<E>) arg;
				this.arguments.addAll(casted.getArguments());				
			} else {
				this.arguments.add(arg);
			}
		}
	}

	//Arguments
	public List<E> getArguments() {
		return new ArrayList<E>(arguments);
	}

	public String getConnector() {
		return connector;
	}

	//Mergeability
	private boolean isMergeableWith(E arg) {
		if (!isAssociativeConnector(connector))
			return false;
		if (!areAllOfSameClassAndConnector(connector, arg))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean areAllOfSameClassAndConnector(String connector, E arg) {
		if (!this.getClass().equals(arg.getClass()))
			return false;
		if ( ((FodotSentenceElementConnector<E>) arg).getConnector() != connector)
			return false;
		return true;
	}

	protected abstract boolean isAssociativeConnector(String connector);

	//Fodot Formula

	@Override
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> formVars = new HashSet<FodotVariable>();
		for (IFodotSentenceElement arg : getArguments()) {
			formVars.addAll(arg.getFreeVariables());
		}
		return formVars;
	}
	
	@Override
	public Set<IFodotSentenceElement> getElementsOfClass(Class<? extends IFodotSentenceElement> clazz) {
		Set<IFodotSentenceElement> result = new HashSet<IFodotSentenceElement>();
		
		//Check for all elements
		for (IFodotSentenceElement el : getArguments()) {
			result.addAll(el.getElementsOfClass(clazz));
		}
		
		//Check for this itself
		if (clazz.isAssignableFrom(this.getClass())) {
			result.add(this);
		}
		
		return result;
	}

	@Override
	public String toCode() {
		return CollectionPrinter.printStringList(
				"", "",
				" " + getConnector() + " ",
				CollectionPrinter.toCode(getArguments(), getBindingOrder())
				);
	}	

	public abstract boolean isValidConnector(String connector);

	//Hashcode & Equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result
				+ ((connector == null) ? 0 : connector.hashCode());
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
		FodotSentenceElementConnector<?> other = (FodotSentenceElementConnector<?>) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (connector == null) {
			if (other.connector != null)
				return false;
		} else if (!connector.equals(other.connector))
			return false;
		return true;
	}

}
