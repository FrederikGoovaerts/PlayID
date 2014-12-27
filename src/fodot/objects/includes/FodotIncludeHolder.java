package fodot.objects.includes;

import java.util.Collection;

import fodot.objects.general.FodotElementContainer;
import fodot.objects.general.IFodotElement;
import fodot.objects.includes.elements.FodotIncludeStatement;
import fodot.util.CollectionPrinter;

public class FodotIncludeHolder extends FodotElementContainer<FodotIncludeStatement> implements IFodotElement {

	public FodotIncludeHolder(Collection<? extends FodotIncludeStatement> includes) {
		super(includes);
	}
	
	public FodotIncludeHolder() {
		this(null);
	}
	
	//Merging
	public void mergeWith(FodotIncludeHolder other) {
		addAllElements(other.getElements());
	}
	
	//Obligatory methods

	@Override
	public boolean isValidElement(FodotIncludeStatement argElement) {
		return true;
	}
	
	@Override
	public String toCode() {		
		return CollectionPrinter.printStringList("", "", "\n", CollectionPrinter.toCode(getElements())) + "\n";
	}
	
	@Override
	public String toString() {
		return "FodotIncludesHolder ["+toCode()+"]";
	}
	

}
