package fodot.objects.vocabulary.elements;

import java.util.Set;

import fodot.objects.general.IFodotNamedElement;

public interface IFodotVocabularyElement extends IFodotNamedElement {
	Set<FodotType> getPrerequiredTypes();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}
