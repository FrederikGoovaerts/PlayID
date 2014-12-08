package fodot.objects.vocabulary.elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.util.CollectionUtil;

public class FodotTypeDeclaration implements IFodotElement {
	private FodotType type;
	private List<FodotType> subtypes;
	private List<FodotType> supertypes;
	
	public FodotTypeDeclaration(FodotType type, Set<String> domain, List<FodotType> isa, List<FodotType> contains) {
		this.type = type;
		this.domain = domain;
	}
	
	public FodotTypeDeclaration(FodotType type) {
		this(type, new HashSet<String>(), new ArrayList<FodotType>(), new ArrayList<FodotType>());
	}

    /*************************************
     * Type
     */

    public FodotType getType() {
		return type;
	}
	
    /************************************/
    
    /*************************************
     * Subclasses
     */

    public void addSubtype(FodotType type) {
    	subtypes.add(type);
    }
    
    public void addAllSubtypes(List<FodotType> types) {
    	subtypes.addAll(types);
    }
    
    public boolean containsSubtype(FodotType type){
    	return subtypes.contains(type);
    }
    
    public List<FodotType> getSubtypes() {
    	return new ArrayList<FodotType>(subtypes);
    }
    
    public boolean hasSubtypes() {
    	return !subtypes.isEmpty();
    }
    
    /************************************/
    
    
    /*************************************
     * Superclasses
     */

    public void addSupertype(FodotType type) {
    	supertypes.add(type);
    }
    
    public void addAllSupertypes(List<FodotType> types) {
    	supertypes.addAll(types);
    }
    
    public boolean containsSupertype(FodotType type){
    	return supertypes.contains(type);
    }
    
    public List<FodotType> getSupertypes() {
    	return new ArrayList<FodotType>(supertypes);
    }

    public boolean hasSupertypes() {
    	return !supertypes.isEmpty();
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
		return "type " + getType().getTypeName()
				+ (hasSupertypes() ? " isa " + CollectionUtil.toNakedList(CollectionUtil.toCode(supertypes)) : "")
				+ (hasSubtypes() ? " contains " + CollectionUtil.toNakedList(CollectionUtil.toCode(subtypes)) : "")
				+ " constructed from " + CollectionUtil.toDomain(getDomainElements());
	}
    
}
