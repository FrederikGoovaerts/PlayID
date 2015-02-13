package fodot.objects.structure.elements.functionenum.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.general.FodotElementList;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.terms.FodotFunction;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.util.CollectionPrinter;

public class FodotFunctionEnumerationElement extends
		FodotElementList<IFodotTypeEnumerationElement> implements
		IFodotFunctionEnumerationElement {

	private IFodotTypeEnumerationElement returnValue;
	private FodotFunctionDeclaration declaration;
	
	public FodotFunctionEnumerationElement(FodotFunctionDeclaration declaration,
			Collection<? extends IFodotTypeEnumerationElement> elements, IFodotTypeEnumerationElement returnValue) {
		super(elements);
		this.declaration = declaration;
		this.returnValue = returnValue;
	}

	/**********************************************
	 *  Getters
	 ***********************************************/
	
	public IFodotTypeEnumerationElement getReturnValue() {
		return returnValue;
	}

	
	public FodotFunctionDeclaration getDeclaration() {
		return declaration;
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

	@Override
	public FodotFunction toFunction() {
		List<IFodotTerm> arguments = new ArrayList<IFodotTerm>();
		for (IFodotTypeEnumerationElement element : getElements()) {
			arguments.add(element.toTerm());
		}
		return new FodotFunction(getDeclaration(), arguments);
	}	

	/**********************************************/

}
