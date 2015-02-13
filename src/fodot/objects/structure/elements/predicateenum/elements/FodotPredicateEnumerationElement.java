package fodot.objects.structure.elements.predicateenum.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.general.FodotElementList;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.util.CollectionPrinter;

public class FodotPredicateEnumerationElement
		extends FodotElementList<IFodotTypeEnumerationElement>
		implements IFodotPredicateEnumerationElement {

	private FodotPredicateDeclaration declaration;
	
	public FodotPredicateEnumerationElement(
			FodotPredicateDeclaration declaration,
			Collection<? extends IFodotTypeEnumerationElement> elements) {
		super(elements);
		this.declaration = declaration;
	}

	public FodotPredicateDeclaration getDeclaration() {
		return declaration;
	}

	/**********************************************
	 *  Obligatory methods
	 ***********************************************/

	@Override
	public String getValue() {
		return toCode();
	}

	@Override
	public String toCode() {
		return CollectionPrinter.toNakedList(CollectionPrinter.toCode(getElements()));
	}

	@Override
	public boolean isValidElement(IFodotTypeEnumerationElement argElement) {
		return true;
	}

	@Override
	public FodotPredicate toPredicate() {
		List<IFodotTerm> arguments = new ArrayList<IFodotTerm>();
		for (IFodotTypeEnumerationElement element : getElements()) {
			arguments.add(element.toTerm());
		}
		return new FodotPredicate(getDeclaration(), arguments);
	}	

	/**********************************************/


}
