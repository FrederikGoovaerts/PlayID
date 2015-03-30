package playid.domain.fodot.structure.elements.functionenum.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import playid.domain.fodot.general.FodotElementList;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.theory.elements.terms.FodotFunction;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;
import playid.domain.fodot.vocabulary.elements.FodotFunctionDeclaration;
import playid.util.CollectionPrinter;

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
