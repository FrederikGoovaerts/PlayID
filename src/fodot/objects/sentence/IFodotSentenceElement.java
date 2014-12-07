package fodot.objects.sentence;

import java.util.List;

import fodot.objects.IFodotElement;
import fodot.objects.sentence.terms.FodotVariable;

public interface IFodotSentenceElement extends IFodotElement {
	List<FodotVariable> getFreeVariables();
}
