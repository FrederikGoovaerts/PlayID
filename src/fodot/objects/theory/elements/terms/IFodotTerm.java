package fodot.objects.theory.elements.terms;

import fodot.objects.theory.elements.IFodotExpression;
import fodot.objects.vocabulary.elements.FodotType;

public interface IFodotTerm extends IFodotExpression {
	
	@Override
	String toString();
	
	FodotType getType();
}
