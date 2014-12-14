package fodot.objects.vocabulary.elements;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.IFodotElement;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotType implements IFodotElement {
	
	private FodotTypeDeclaration declaration;
    private final String typeName;

    /***************************************************************************
     * Constructor
     **************************************************************************/


    public FodotType(String typeName) {
        this.typeName = typeName;
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    public String getTypeName() {
        return typeName;
    }

    public FodotTypeDeclaration getDeclaration() {
    	return declaration;
    }
    
    public void setDeclaration(FodotTypeDeclaration declaration) {
    	this.declaration = declaration;
    }
    
    public boolean hasDeclaration() {
    	return this.declaration == null;
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
    /***************************************************************************
     * Class Properties
     **************************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FodotType type = (FodotType) o;

        if (typeName != null ? !typeName.equals(type.typeName) :
                type.typeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return typeName != null ? typeName.hashCode() : 0;
    }
    
    /***************************************************************************
     * Fodot Element requirements
     **************************************************************************/
    
	@Override
	public String toCode() {
		return getTypeName();
	}
}
