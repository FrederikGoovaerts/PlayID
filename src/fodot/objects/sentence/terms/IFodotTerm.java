package fodot.objects.sentence.terms;

import fodot.objects.sentence.IFodotSentenceElement;
import fodot.objects.vocabulary.elements.FodotType;

public interface IFodotTerm extends IFodotSentenceElement {
	
	@Override
	String toString();
	
	FodotType getType();
}
