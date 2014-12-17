package fodot.objects.vocabulary.elements;

import fodot.objects.IFodotElement;
import fodot.util.CollectionUtil;

public class FodotTypeDeclaration implements IFodotElement {
	private FodotType type;
		
	public FodotTypeDeclaration(FodotType type) {
		setType(type);
	}

    /*************************************
     * Type
     */

    public FodotType getType() {
		return type;
	}
    
    private void setType(FodotType type) {
    	if (type == null)
    		throw new IllegalArgumentException("Not a valid type!");
    	this.type = type;
    }
	
    /************************************/   


    /***************************************************************************
     * Fodot Element requirements
     **************************************************************************/
    
	@Override
	public String toCode() {
		return "type " + getType().getName()
				+ (getType().hasSupertypes() ? " isa " + CollectionUtil.toNakedList(CollectionUtil.toCode(getType().getSupertypes())) : "")
//				+ (getType().hasSubtypes() ? " contains " + CollectionUtil.toNakedList(CollectionUtil.toCode(getType().getSubtypes())) : "")
				+ ((getType().hasDomainElements() && !getType().hasSupertypes()) ? " constructed from " + CollectionUtil.toDeclarationDomain(CollectionUtil.toCode(getType().getDomainElements())) : "" );
	}
    /************************************/
    
    
}
