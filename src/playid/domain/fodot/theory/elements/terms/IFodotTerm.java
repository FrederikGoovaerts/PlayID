package playid.domain.fodot.theory.elements.terms;

import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.theory.elements.IFodotExpression;
import playid.domain.fodot.vocabulary.elements.FodotType;

public interface IFodotTerm extends IFodotExpression {
	
	@Override
	String toString();
	
	FodotType getType();
	
	IFodotTypeEnumerationElement toEnumerationElement();
		
}
