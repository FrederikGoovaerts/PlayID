package fodot.objects.structure.elements;

import java.util.Collection;

import fodot.exceptions.fodot.FodotException;
import fodot.objects.general.FodotElementContainer;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;
import fodot.util.CollectionPrinter;

public abstract class FodotEnumeration<E extends IFodotEnumerationElement> extends FodotElementContainer<E> implements IFodotStructureElement {
	private IFodotVocabularyElement declaration;
	
	public FodotEnumeration(IFodotVocabularyElement declaration, Collection<? extends E> elements) {
		super(elements);
		setDeclaration(declaration);
	}
	
	public FodotEnumeration(IFodotVocabularyElement declaration) {
		this(declaration, null);
	}
	
	/**********************************************
	 *  Declaration
	 ***********************************************/
	private void setDeclaration(IFodotVocabularyElement decl) {
		if (decl == null) {
			throw new FodotException("Declaration of enumeration can't be null!");
		}
		this.declaration = decl;
	}
	
	public IFodotVocabularyElement getDeclaration() {
		return declaration;
	}
	/**********************************************/
	
	/**********************************************
	 *  toCode
	 ***********************************************/

	@Override
	public String toCode() {
        return getName() + " = " + CollectionPrinter.toDomain(CollectionPrinter.toCode(getElements()));
	}
	
	/**********************************************/


    /**********************************************/
	
	/**********************************************
	 *  Obligatory
	 ***********************************************/

	@Override
	public String getName() {
		return getDeclaration().getName();
	}

	@Override
	public boolean isValidElement(IFodotEnumerationElement argElement) {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((declaration == null) ? 0 : declaration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FodotEnumeration<?> other = (FodotEnumeration<?>) obj;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		if (getElements() == null) {
			if (other.getElements() != null)
				return false;
		} else if (!getElements().equals(other.getElements()))
			return false;
		return true;
	}	

	/**********************************************/
	
}
