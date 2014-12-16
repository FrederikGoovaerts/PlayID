package fodot.gdl_parser.util;

import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;

import java.util.HashMap;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class LTCPool {

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public LTCPool() {
        this.fluentPredicates = new HashMap<>();
        this.staticPredicates = new HashMap<>();
    }

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    public FodotPredicateDeclaration getPredicate(String predName){
        if(!isPredicateRegistered(predName))
            throw new IllegalArgumentException("Predicate not found!");
        if(isFluentPredicateRegistered(predName))
            return getFluentPredicate(predName);
        return getStaticPredicate(predName);
    }

    public  boolean isPredicateRegistered(String predName){
        return (isFluentPredicateRegistered(predName)
                ||isStaticPredicateRegistered(predName));
    }

    /*************************************
     * Fluent Predicates
     */

    private HashMap<String,FodotPredicateDeclaration> fluentPredicates;

    public HashMap<String,FodotPredicateDeclaration> getFluentPredicates() {
        return new HashMap<>(fluentPredicates);
    }

    private FodotPredicateDeclaration getFluentPredicate(String predName) {
        if(!isFluentPredicateRegistered(predName))
            throw new IllegalArgumentException();
        return fluentPredicates.get(predName);
    }

    public boolean isFluentPredicateRegistered(String predName) {
        return (fluentPredicates.containsKey(predName));
    }

    public void addFluentPredicate(FodotPredicateDeclaration pred){
        if(pred == null)
            throw new IllegalArgumentException();
        fluentPredicates.put(pred.getName(), pred);
    }

    private void removeFluentPredicate(FodotPredicateDeclaration pred){
        if(pred == null)
            throw new IllegalArgumentException();
        if(!fluentPredicates.containsKey(pred.getName()))
            throw new IllegalArgumentException();
        fluentPredicates.remove(pred.getName());
    }


    /*** End of Fluent Predicates subsection ***/

    /*************************************
     * Static Predicates
     */

    private HashMap<String,FodotPredicateDeclaration> staticPredicates;

    public HashMap<String,FodotPredicateDeclaration> getStaticPredicates() {
        return new HashMap<>(staticPredicates);
    }

    private FodotPredicateDeclaration getStaticPredicate(String predName) {
        if(!isStaticPredicateRegistered(predName))
            throw new IllegalArgumentException();
        return staticPredicates.get(predName);
    }

    public boolean isStaticPredicateRegistered(String predName) {
        return (staticPredicates.containsKey(predName));
    }

    public void addStaticPredicate(FodotPredicateDeclaration staticPred){
        if(staticPred == null || fluentPredicates.containsKey(staticPred))
            throw new IllegalArgumentException();
        staticPredicates.put(staticPred.getName(), staticPred);
    }

    public void convertFluentPredicateToStatic(String predName) {
        if (!fluentPredicates.containsKey(predName))
            throw new IllegalArgumentException();
        FodotPredicateDeclaration pred = getFluentPredicate(predName);
        removeFluentPredicate(pred);
        addStaticPredicate(pred);
    }

    /*** End of Static Predicates subsection ***/
}
