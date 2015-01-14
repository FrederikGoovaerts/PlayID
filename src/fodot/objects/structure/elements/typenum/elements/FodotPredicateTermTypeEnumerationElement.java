package fodot.objects.structure.elements.typenum.elements;

import java.util.Collection;

import fodot.objects.general.FodotElementContainer;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.util.CollectionPrinter;

public class FodotPredicateTermTypeEnumerationElement extends
		FodotElementContainer<IFodotTypeEnumerationElement> implements
		IFodotTypeEnumerationElement {

	private FodotPredicateTermDeclaration declaration;
	
	public FodotPredicateTermTypeEnumerationElement(FodotPredicateTermDeclaration decl, Collection<? extends IFodotTypeEnumerationElement> elements) {
		super(elements);
		setDeclaration(decl);
	}

	/**********************************************
	 *  Declaration
	 ***********************************************/
	
	public FodotPredicateTermDeclaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(FodotPredicateTermDeclaration declaration) {
		if (declaration == null) {
			throw new IllegalArgumentException("Declaration can't be null");
		}
		this.declaration = declaration;
	}
	
	/**********************************************/


	/**********************************************
	 *  Obligatory methods
	 ***********************************************/
	
	@Override
	public String getValue() {
		return toCode();
	}

	@Override
	public String toCode() {
		return getDeclaration().getName() + CollectionPrinter.toCoupleAsCode(getElements());
	}

	@Override
	public boolean isValidElement(IFodotTypeEnumerationElement argElement) {
		return true;
	}

	/**********************************************/


}
