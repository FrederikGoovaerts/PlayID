package fodot.objects.vocabulary.elements;

import java.util.Set;

import fodot.objects.IFodotNamedElement;

public interface IFodotVocabularyElement extends IFodotNamedElement {
	Set<FodotType> getPrerequiredTypes();
}
