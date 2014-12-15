package fodot.objects.vocabulary.elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.sentence.terms.FodotConstant;
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
				+ (getType().hasDomainElements() ? " constructed from " + CollectionUtil.toDomain(CollectionUtil.toCode(getType().getDomainElements())) : "" );
	}
    /************************************/
    
    
}
