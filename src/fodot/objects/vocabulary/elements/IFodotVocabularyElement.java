package fodot.objects.vocabulary.elements;

import java.util.Set;

import fodot.objects.IFodotElement;

public interface IFodotVocabularyElement extends IFodotElement {
	Set<FodotType> getPrerequiredTypes();
	String getName();
}
