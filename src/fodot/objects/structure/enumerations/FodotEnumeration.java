package fodot.objects.structure.enumerations;

import fodot.objects.structure.IFodotStructureElement;
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
