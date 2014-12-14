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
    private Set<FodotConstant> domain = new HashSet<>();
	private Set<FodotType> subtypes;
	private Set<FodotType> supertypes;
	
	public FodotTypeDeclaration(FodotType type, Set<FodotConstant> domain, Set<FodotType> supertypes, Set<FodotType> subtypes) {
		setType(type);
		setDomain(domain);
		setSupertypes(supertypes);
		setSubtypes(subtypes);
	}
	
	public FodotTypeDeclaration(FodotType type) {
		this(type, null, null, null);
	}

    /*************************************
     * Type
     */

    public FodotType getType() {
		return type;
	}
    
    private void setType(FodotType type) {
    	if (type == null)
    		throw new IllegalArgumentException("Not a valid type! ");
    	this.type = type;
    	this.type.setDeclaration(this);
    }
	
    /************************************/
    
    /*************************************
     * Subclasses
     */

    private void setSubtypes(Set<FodotType> subtypes) {
    	if (subtypes == null) {
    		this.subtypes = new HashSet<FodotType>();
    	} else {
    		this.subtypes = subtypes;
    	}
    }
    
    public void addSubtype(FodotType type) {
    	subtypes.add(type);
    }
    
    public void addAllSubtypes(Set<FodotType> types) {
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

    private void setSupertypes(Set<FodotType> supertypes) {
    	if (supertypes == null) {
    		this.supertypes = new HashSet<FodotType>();
    	} else {
    		this.supertypes = supertypes;
    	}
    }
    
    public void addSupertype(FodotType type) {
    	supertypes.add(type);
    }
    
    public void addAllSupertypes(Set<FodotType> types) {
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

    private void setDomain(Set<FodotConstant> domain) {
    	if (domain == null) {
    		this.domain = new HashSet<FodotConstant>();
    	} else {
    		this.domain = domain;
    	}
    }
    
	public void addDomainElement(FodotConstant element) {
//    	assert(!domain.contains(element)) :
//    			"Type " + this.getTypeName()
//    			+ " already contains given element " + element + ".";
    	domain.add(element);
    }
    
    public void addAllDomainElements(Set<FodotConstant> elements) {
    	domain.addAll(elements);
    }
    
    public boolean containsElement(FodotConstant element){
    	return domain.contains(element);
    }
    
    public Set<FodotConstant> getDomainElements() {
    	return new HashSet<FodotConstant>(domain);
    }
    
    public boolean hasDomainElements() {
    	return domain != null && !domain.isEmpty();
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
				+ (hasDomainElements() ? " constructed from " + CollectionUtil.toDomain(CollectionUtil.toCode(getDomainElements())) : "" );
	}
    
}
