package fodot.objects.structure.elements.functionenum.elements;

import java.util.Collection;

import fodot.objects.general.FodotElementList;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import fodot.util.CollectionPrinter;

public class FodotFunctionEnumerationElement extends
		FodotElementList<IFodotTypeEnumerationElement> implements
		IFodotFunctionEnumerationElement {

	private IFodotTypeEnumerationElement returnValue;
	
	public FodotFunctionEnumerationElement(
			Collection<? extends IFodotTypeEnumerationElement> elements, IFodotTypeEnumerationElement returnValue) {
		super(elements);
		setReturnValue(returnValue);
	}

	/**********************************************
	 *  Return value
	 ***********************************************/
	
	public IFodotTypeEnumerationElement getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(IFodotTypeEnumerationElement returnValue) {
		this.returnValue = returnValue;
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
		return (hasElements() ?
				CollectionPrinter.toNakedList(CollectionPrinter.toCode(getElements())) + "->" : "")
				+ returnValue.getValue();
	}

	@Override
	public boolean isValidElement(IFodotTypeEnumerationElement argElement) {
		return true;
	}	

	/**********************************************/

}
