package fodot.objects.theory.elements.terms;

import fodot.objects.theory.elements.IFodotSentenceElement;
import fodot.objects.vocabulary.elements.FodotType;

public interface IFodotTerm extends IFodotSentenceElement {
	
	@Override
	String toString();
	
	FodotType getType();
}
