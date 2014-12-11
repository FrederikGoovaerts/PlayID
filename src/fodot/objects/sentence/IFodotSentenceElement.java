package fodot.objects.sentence;

import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.sentence.terms.FodotVariable;

public interface IFodotSentenceElement extends IFodotElement {
	Set<FodotVariable> getFreeVariables();
}
