package playid.domain.fodot.structure.elements;

import playid.domain.fodot.general.IFodotNamedElement;
import playid.domain.fodot.vocabulary.elements.IFodotVocabularyElement;

public interface IFodotStructureElement extends IFodotNamedElement {
	IFodotVocabularyElement getDeclaration();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}
