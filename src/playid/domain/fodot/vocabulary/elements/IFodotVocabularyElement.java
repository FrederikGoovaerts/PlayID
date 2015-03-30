package playid.domain.fodot.vocabulary.elements;

import java.util.Set;

import playid.domain.fodot.general.IFodotNamedElement;

public interface IFodotVocabularyElement extends IFodotNamedElement {
	Set<FodotType> getPrerequiredTypes();
	int getArity();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}
