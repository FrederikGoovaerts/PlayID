package fodot.objects.theory.elements.inductivedefinitions;

import fodot.exceptions.fodot.InvalidTypeException;
import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.terms.FodotFunction;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class FodotInductivePredicate extends FodotElement implements IFodotInductiveDefinitionElement {

	private static final int BINDING_ORDER = -1;

	private FodotPredicate predicate;

	public FodotInductivePredicate(FodotPredicate predicate) {
		super();
		this.predicate = predicate;
	}

	private FodotPredicate getPredicate() {
		return predicate;
	}
	
	@Override
	public Set<FodotVariable> getFreeVariables() {
		return predicate.getFreeVariables();
	}
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
	}
	
	@Override
	public String toCode() {
		return predicate.toCode();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
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
		FodotInductivePredicate other = (FodotInductivePredicate) obj;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[ Inductive Predicate: "+toCode()+"]";
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		Set<IFodotElement> res = new HashSet<IFodotElement>(getPredicate().getDirectFodotElements());
		return res;
	}
	
	
	
	
	
}
