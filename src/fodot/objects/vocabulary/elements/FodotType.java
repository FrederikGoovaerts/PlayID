package fodot.objects.vocabulary.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.terms.FodotConstant;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotType extends FodotElement implements IFodotElement {
	

	public static final FodotType BOOLEAN = new FodotType("bool");
	public static final FodotType NATURAL_NUMBER = new FodotType("nat");
	public static final FodotType INTEGER = new FodotType("int", null, null, new HashSet<FodotType>(Arrays.asList(new FodotType[]{NATURAL_NUMBER})));

	static {
		new FodotConstant("false", BOOLEAN);
		new FodotConstant("true", BOOLEAN);
	}
	
	
	private FodotTypeDeclaration declaration;
	private String name;
	private Set<IFodotDomainElement> domain = new HashSet<>();
	private Set<FodotType> subtypes;
	private Set<FodotType> supertypes;

	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	public FodotType(String typeName, Collection<? extends IFodotDomainElement> domain, Set<FodotType> supertypes, Set<FodotType> subtypes) {
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
	 * Class methods
	 **************************************************************************/

	public static List<FodotType> getSameTypeList(int predArity,
												  FodotType type) {
		List<FodotType> toReturn = new ArrayList<>();
		for (int i = 0; i < predArity; i++) {
			toReturn.add(type);
		}
		return toReturn;
	}

	/*************************************
	 *  'placeholder' type
	 *
	 * This type can be used as a placeholder
	 * for all types in predicates and
	 * functions until they can be filled
	 */

	public static FodotType createPlaceHolderType() {
		return new FodotType("Unfilled", null, new HashSet<FodotType>(Arrays.asList(new FodotType[]{INTEGER})), null);
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
			for (FodotType subtype : subtypes) {
				if(!(subtype.containsSupertype(this)))
					subtype.addSupertype(this);
			}
		}
	}

	public void addSubtype(FodotType type) {
		subtypes.add(type);
		if (!type.containsSupertype(this)) {
			type.addSupertype(this);
		}
	}

	public void addAllSubtypes(Collection<? extends FodotType> types) {
		for (FodotType type : types) {
			addSubtype(type);
		}
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

	public void removeSubtype(FodotType type) {
		if (type == null) {
			return;
		}
		if (type.containsSupertype(this)) {
			type.removeSupertype(this);
		}
		subtypes.remove(type);
	}

	public void removeAllSubtypes() {
		for (FodotType sub : subtypes) {
			removeSubtype(sub);
		}
	}
	
	public boolean isASubtypeOf(FodotType toLookFor) {
		Queue<FodotType> queue = new LinkedList<FodotType>();
		queue.add(this);
		while (!queue.isEmpty()) {
			FodotType current = queue.poll();
			if (current.equals(toLookFor)) {
				return true;
			}
			queue.addAll(current.getSupertypes());
		}
		return false;
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
			for (FodotType supertype : supertypes) {
				if(!(supertype.containsSubtype(this)))
					supertype.addSubtype(this);
			}
		}
	}

	public void addSupertype(FodotType type) {
		supertypes.add(type);
		if (!type.containsSubtype(this)) {
			type.addSubtype(this);
		}
	}

	public void addAllSupertypes(Collection<? extends FodotType> types) {
		for (FodotType type : types) {
			addSupertype(type);
		}
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

	public void removeSupertype(FodotType type) {
		if (type == null) {
			return;
		}
		if (type.containsSubtype(this)) {
			type.removeSubtype(this);
		}
		supertypes.remove(type);
	}
	
	public void removeAllSupertypes() {
		for (FodotType superT : supertypes) {
			removeSupertype(superT);
		}
	}
	
	public boolean isASupertypeOf(FodotType toLookFor) {
		Queue<FodotType> queue = new LinkedList<FodotType>();
		queue.add(this);
		while (!queue.isEmpty()) {
			FodotType current = queue.poll();
			if (current.equals(toLookFor)) {
				return true;
			}
			queue.addAll(current.getSubtypes());
		}
		return false;
	}
	
	/************************************/


	/*************************************
	 * Domain elements in type declaration
	 */

	private void setDomain(Collection<? extends IFodotDomainElement> domain) {
		this.domain = (domain == null? new HashSet<IFodotDomainElement>() : new HashSet<IFodotDomainElement>(domain));
	}

	public void addDomainElement(IFodotDomainElement element) {
		domain.add(element);
	}

	public void addAllDomainElements(Set<IFodotDomainElement> elements) {
		domain.addAll(elements);
	}

	public boolean containsElement(IFodotDomainElement element){
		return domain.contains(element);
	}

	public Set<IFodotDomainElement> getDomainElements() {
		return new HashSet<IFodotDomainElement>(domain);
	}

	public boolean hasDomainElements() {
		return domain != null && !domain.isEmpty();
	}

	public void removeDomainElement(IFodotDomainElement element) {
		domain.remove(element);
	}

	/************************************/

	/***************************************************************************
	 * Fodot Element requirements
	 **************************************************************************/

	@Override
	public String toCode() {
		return getName();
	}

	@Override
	public String toString() {
		return "[FodotType: " + toCode() + "]";
	}
	
	public Set<FodotType> getPrerequiredTypes() {
		Set<FodotType> result = new HashSet<FodotType>();
		for (IFodotDomainElement el : getDomainElements()) {
			result.addAll(el.getRequiredTypes());
		}		
		return result;
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return new LinkedHashSet<IFodotElement>();
	}


}
