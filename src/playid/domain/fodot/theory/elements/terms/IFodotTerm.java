package playid.domain.fodot.theory.elements.terms;

import playid.domain.fodot.theory.elements.IFodotExpression;
import playid.domain.fodot.vocabulary.elements.FodotType;

public interface IFodotTerm extends IFodotExpression {
	
	@Override
	String toString();
	
	FodotType getType();
}
