package playid.domain.fodot.includes;

import java.util.Collection;

import playid.domain.fodot.general.FodotElementContainer;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.includes.elements.FodotIncludeStatement;
import playid.util.CollectionPrinter;

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
		return CollectionPrinter.printStringList("", "", "\n", CollectionPrinter.toCode(getElements()));
	}
	
	@Override
	public String toString() {
		return "FodotIncludesHolder ["+toCode()+"]";
	}
	

}
