package fodot.type;

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

    /************************************/

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        if (typeName != null ? !typeName.equals(type.typeName) : type.typeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return typeName != null ? typeName.hashCode() : 0;
    }
}
