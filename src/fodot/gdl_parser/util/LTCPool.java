package fodot.gdl_parser.util;

import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

import static fodot.helpers.FodotPartBuilder.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class LTCPool {

    /***************************************************************************
     * Constructor
     *************************************************************************
     * @param timeType*/

    public LTCPool(FodotType timeType) {
        this.timeType = timeType;
        this.fluentPredicates = new HashMap<>();
        this.staticPredicates = new HashMap<>();
        this.timedVersions = new HashMap<>();
        this.initials = new HashMap<>();
        this.causes = new HashMap<>();
        this.causeNots = new HashMap<>();
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private FodotType timeType;

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    public FodotPredicateDeclaration getPredicate(String predName){
        if(!isPredicateRegistered(predName))
            throw new IllegalArgumentException("Predicate not found: " + predName);
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

    public List<FodotPredicateDeclaration> getFluentPredicates() {
        return new ArrayList<>(fluentPredicates.values());
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
        this.removeLTCPredicatesOf(pred);
        fluentPredicates.remove(pred.getName());
    }


    /*** End of Fluent Predicates subsection ***/

    /*************************************
     * LTC predicates
     */

    private HashMap<FodotPredicateDeclaration,FodotPredicateDeclaration> timedVersions;

    private HashMap<FodotPredicateDeclaration,FodotPredicateDeclaration> initials;

    private HashMap<FodotPredicateDeclaration,FodotPredicateDeclaration> causes;

    private HashMap<FodotPredicateDeclaration,FodotPredicateDeclaration> causeNots;

    private void removeLTCPredicatesOf(FodotPredicateDeclaration pred) {
        this.timedVersions.remove(pred);
        this.initials.remove(pred);
        this.causes.remove(pred);
        this.causeNots.remove(pred);
    }

    public FodotPredicateDeclaration getTimedVerionOf(FodotPredicateDeclaration pred){
        if(pred == null)
            throw new IllegalArgumentException();
        if(!fluentPredicates.containsKey(pred.getName()))
            throw new IllegalArgumentException();
        if(!timedVersions.containsKey(pred)) {
            List<FodotType> timedList = pred.getArgumentTypes();
            timedList.add(0, this.timeType);
            timedVersions.put(pred, createPredicateDeclaration(pred.getName(), timedList));
        }
        return this.timedVersions.get(pred);
    }

    public FodotPredicateDeclaration getInitialOf(FodotPredicateDeclaration pred){
        if(pred == null)
            throw new IllegalArgumentException();
        if(!fluentPredicates.containsKey(pred.getName()))
            throw new IllegalArgumentException();
        if(!initials.containsKey(pred)) {
            initials.put(pred,createPredicateDeclaration("I_" + pred.getName(),
                    pred.getArgumentTypes()));
        }
        return this.initials.get(pred);
    }

    public FodotPredicateDeclaration getCauseOf(FodotPredicateDeclaration pred){
        if(pred == null)
            throw new IllegalArgumentException();
        if(!fluentPredicates.containsKey(pred.getName()))
            throw new IllegalArgumentException();
        if(!causes.containsKey(pred)) {
            List<FodotType> timedList = pred.getArgumentTypes();
            timedList.add(0, this.timeType);
            causes.put(pred, createPredicateDeclaration("C_" + pred.getName(),
                    timedList));
        }
        return this.causes.get(pred);
    }

    public FodotPredicateDeclaration getCauseNotOf(FodotPredicateDeclaration pred){
        if(pred == null)
            throw new IllegalArgumentException();
        if(!fluentPredicates.containsKey(pred.getName()))
            throw new IllegalArgumentException();
        if(!causes.containsKey(pred)) {
            List<FodotType> timedList = pred.getArgumentTypes();
            timedList.add(0, this.timeType);
            causes.put(pred, createPredicateDeclaration("Cn_" + pred.getName(),
                    timedList));
        }
        return this.causes.get(pred);
    }

    /*** End of LTC predicates subsection ***/

    /*************************************
     * Static Predicates
     */

    private HashMap<String,FodotPredicateDeclaration> staticPredicates;

    public List<FodotPredicateDeclaration> getStaticPredicates() {
        return new ArrayList<>(staticPredicates.values());
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
