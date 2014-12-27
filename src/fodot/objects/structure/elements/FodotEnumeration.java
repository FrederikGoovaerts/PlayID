package fodot.objects.structure.elements;

import fodot.objects.vocabulary.elements.IFodotVocabularyElement;

public abstract class FodotEnumeration implements IFodotStructureElement {
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
	
	public abstract IFodotVocabularyElement getDeclaration();

	@Override
	public String getName() {
		return getDeclaration().getName();
	}
}
