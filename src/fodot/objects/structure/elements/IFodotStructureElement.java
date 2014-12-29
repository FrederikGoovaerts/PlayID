package fodot.objects.structure.elements;

import fodot.objects.general.IFodotNamedElement;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;

public interface IFodotStructureElement extends IFodotNamedElement {
	IFodotVocabularyElement getDeclaration();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}
