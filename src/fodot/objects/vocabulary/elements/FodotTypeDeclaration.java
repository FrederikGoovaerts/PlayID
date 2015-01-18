package fodot.objects.vocabulary.elements;

import java.util.Set;

import fodot.util.CollectionPrinter;

public class FodotTypeDeclaration implements IFodotVocabularyElement {
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
    		throw new IllegalArgumentException("Not a valid type!");
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
		return "type " + getType().getName()
				+ (shouldShowSupertypes() && getType().hasSupertypes() ? " isa " + CollectionPrinter.toNakedList(CollectionPrinter.toCode(getType().getSupertypes())) : "")
				+ (shouldShowSubtypes() && getType().hasSubtypes() ? " contains " + CollectionPrinter.toNakedList(CollectionPrinter.toCode(getType().getSubtypes())) : "")
				+ (shouldShowDomain() && getType().hasDomainElements() && !getType().hasSupertypes() ? " constructed from " + CollectionPrinter.printStringList("{",	"}", ",", CollectionPrinter.toCode(getType().getDomainElements())) : "" );
	}
    /************************************/

	@Override
	public String toString() {
		return "FodotTypeDeclaration [getType()=" + getType() + "]";
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
    
}
