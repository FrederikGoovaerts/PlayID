package fodot.objects.vocabulary.elements;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.util.CollectionUtil;

public class FodotTypeDeclaration implements IFodotElement {
	private FodotType type;
	
	public FodotTypeDeclaration(FodotType type, Set<String> domain) {
		this.type = type;
		this.domain = domain;
	}
	
	public FodotTypeDeclaration(FodotType type) {
		this(type, new HashSet<String>());
	}

    /*************************************
     * Type
     */

    public FodotType getType() {
		return type;
	}
	
    /************************************/
    
    /*************************************
     * Domain elements in type declaration
     */
    
    private Set<String> domain = new HashSet<>();

	public void addDomainElement(String element) {
//    	assert(!domain.contains(element)) :
//    			"Type " + this.getTypeName()
//    			+ " already contains given element " + element + ".";
    	domain.add(element);
    }
    
    public void addAllDomainElements(Set<String> elements) {
    	domain.addAll(elements);
    }
    
    public boolean containsElement(String element){
    	return domain.contains(element);
    }
    
    public Set<String> getDomainElements() {
    	return new HashSet<String>(domain);
    }

    
    /************************************/

    /***************************************************************************
     * Fodot Element requirements
     **************************************************************************/
    
	@Override
	public String toCode() {
		return "type " + getType().getTypeName() + " constructed from " + CollectionUtil.toDomain(getDomainElements());
	}
    
}
