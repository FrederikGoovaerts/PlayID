package fodot.objects.structure.elements.predicateenum.elements;

import java.util.Collection;

import fodot.objects.general.FodotElementList;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.util.CollectionPrinter;

public class FodotPredicateEnumerationElement
		extends FodotElementList<IFodotTypeEnumerationElement>
		implements IFodotPredicateEnumerationElement {

	public FodotPredicateEnumerationElement(
			Collection<? extends IFodotTypeEnumerationElement> elements) {
		super(elements);
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

	/**********************************************/


}
