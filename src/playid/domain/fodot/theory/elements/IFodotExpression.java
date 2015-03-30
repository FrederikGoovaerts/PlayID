package playid.domain.fodot.theory.elements;

import java.util.Set;

import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.theory.elements.terms.FodotVariable;

public interface IFodotExpression extends IFodotElement {
	Set<FodotVariable> getFreeVariables();
	int getBindingOrder();
	
	@Override
	boolean equals(Object obj);
	
	@Override
	int hashCode();
}
