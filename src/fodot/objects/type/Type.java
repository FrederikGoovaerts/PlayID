package fodot.objects.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class Type {

    /***************************************************************************
     * Constructor
     **************************************************************************/


    public Type(String typeName) {
        this.typeName = typeName;
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private final String typeName;

    public String getTypeName() {
        return typeName;
    }

    /*************************************
     *  Static 'placeholder' type
     *
     * This type is used as a placeholder
     * for all types in predicates and
     * functions until they can be filled
     */

    private static Type moon = new Type("Unfilled");

    public static Type getPlaceHolderType() {
        return moon;
    }

    public static List<Type> getPlaceHolderList(int amount) {
        List<Type> result = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            result.add(Type.getPlaceHolderType());
        }
        return result;
    }

    /************************************/
    
    /*************************************
     * Domain elements in type
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
     * Class Properties
     **************************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        if (typeName != null ? !typeName.equals(type.typeName) :
                type.typeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return typeName != null ? typeName.hashCode() : 0;
    }
}
