package fodot.objects.vocabulary.elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.sentence.terms.FodotConstant;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotType implements IFodotElement {
	
	private FodotTypeDeclaration declaration;
    private String name;
    private Set<FodotConstant> domain = new HashSet<>();
	private Set<FodotType> subtypes;
	private Set<FodotType> supertypes;

    /***************************************************************************
     * Constructor
     **************************************************************************/

	public FodotType(String typeName, Set<FodotConstant> domain, Set<FodotType> supertypes, Set<FodotType> subtypes) {
		setName(typeName);
		setDomain(domain);
		setSupertypes(supertypes);
		setSubtypes(subtypes);
	}

    public FodotType(String typeName) {
        this(typeName, null, null, null);
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    public String getName() {
        return name;
    }
    
    private void setName(String name) {
    	this.name = name;
    }

    public FodotTypeDeclaration getDeclaration() {
    	if (!hasDeclaration()) {
    		setDeclaration(new FodotTypeDeclaration(this));
    	}
    	return declaration;
    }
    
    private void setDeclaration(FodotTypeDeclaration declaration) {
    	this.declaration = declaration;
    }
    
    public boolean hasDeclaration() {
    	return this.declaration != null;
    }
    
    /*************************************
     *  Static 'placeholder' type
     *
     * This type is used as a placeholder
     * for all types in predicates and
     * functions until they can be filled
     */

    private static final FodotType moon = new FodotType("Unfilled");
    public static final FodotType NATURAL_NUMBER = new FodotType("nat");
    public static final FodotType INTEGER = new FodotType("int");

    public static FodotType getPlaceHolderType() {
        return moon;
    }

    public static List<FodotType> getPlaceHolderList(int amount) {
        List<FodotType> result = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            result.add(FodotType.getPlaceHolderType());
        }
        return result;
    }
	
	//DOMAIN, SUBCLASS, SUPERCLASS STUFF
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
     * Class Properties
     **************************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FodotType type = (FodotType) o;

        if (name != null ? !name.equals(type.name) :
                type.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
    
    /***************************************************************************
     * Fodot Element requirements
     **************************************************************************/
    
	@Override
	public String toCode() {
		return getName();
	}
	
	
}
