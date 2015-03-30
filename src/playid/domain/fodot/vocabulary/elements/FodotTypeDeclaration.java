package playid.domain.fodot.vocabulary.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import playid.domain.exceptions.fodot.FodotException;
import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.general.sorting.FodotElementComparators;
import playid.util.CollectionPrinter;

public class FodotTypeDeclaration extends FodotElement implements IFodotVocabularyElement {
	private FodotType type;
	private boolean showDomain;
	private boolean showSupertypes;
	private boolean showSubtypes;

	private static final boolean DEFAULT_SHOW_DOMAIN = true;
	private static final boolean DEFAULT_SHOW_SUPERTYPES = true;
	private static final boolean DEFAULT_SHOW_SUBTYPES = false;

	public FodotTypeDeclaration(FodotType type, boolean showDomain,
			boolean showSupertypes, boolean showSubtypes) {
		super();
		setType(type);
		setShowDomain(showDomain);
		setShowSupertypes(showSupertypes);
		setShowSubtypes(showSubtypes);
	}

	public FodotTypeDeclaration(FodotType type) {
		this(type, DEFAULT_SHOW_DOMAIN, DEFAULT_SHOW_SUPERTYPES, DEFAULT_SHOW_SUBTYPES);
	}

    /*************************************
     * Type
     */

    public FodotType getType() {
		return type;
	}
    
    private void setType(FodotType type) {
    	if (type == null)
    		throw new FodotException("Not a valid type!");
    	this.type = type;
    }
	
    /************************************/   



	public boolean shouldShowSupertypes() {
		return showSupertypes;
	}

	public void setShowSupertypes(boolean showSupertypes) {
		this.showSupertypes = showSupertypes;
	}

	public boolean shouldShowSubtypes() {
		return showSubtypes;
	}

	public void setShowSubtypes(boolean showSubtypes) {
		this.showSubtypes = showSubtypes;
	}

	public boolean shouldShowDomain() {
		return showDomain;
	}

	public void setShowDomain(boolean showDomain) {
		this.showDomain = showDomain;
	}
    

    /***************************************************************************
     * Fodot Element requirements
     **************************************************************************/
    
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("type " + getType().getName());
		if (shouldShowSupertypes() && getType().hasSupertypes()) {
			builder.append(" isa ");
			builder.append(CollectionPrinter.toNakedList(CollectionPrinter.toCode(getType().getSupertypes())));
		}
		if (shouldShowSubtypes() && getType().hasSubtypes()) {
			builder.append(" contains ");
			builder.append(CollectionPrinter.toNakedList(CollectionPrinter.toCode(getType().getSubtypes())));
		}
		if (shouldShowDomain() && getType().hasDomainElements() && !getType().hasSupertypes()) {
			builder.append(" constructed from ");
			
			//Sort domain elements
			List<IFodotElement> domainElements = new ArrayList<IFodotElement>(getType().getDomainElements());
			Collections.sort(domainElements, FodotElementComparators.ELEMENT_TOCODE_COMPARATOR);
			
			builder.append(CollectionPrinter.printStringList("{ ", " }", ", ", CollectionPrinter.toCode(domainElements)));
		}
		return builder.toString();
	}
    /************************************/

	@Override
	public String toString() {
		return "[FodotTypeDeclaration: "+toCode()+"]";
	}

	@Override
	public Set<FodotType> getPrerequiredTypes() {
		return getType().getPrerequiredTypes();
	}

	@Override
	public String getName() {
		return getType().getName();
	}

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		Set<IFodotElement> res = new LinkedHashSet<IFodotElement>();
		res.add(getType());
		res.addAll(getType().getDomainElements());
		return res;
	}
    
}
