package fodot.gdl_parser;

import fodot.objects.Fodot;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotBuilder implements GdlTransformer{

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotBuilder(){
        builtFodot = Fodot.getNewEmptyFodot();

        this.roles = new TreeSet<>();
        this.fluentPredicates = new HashMap<>();
        this.staticPredicates = new HashMap<>();
        this.constants = new TreeSet<>();

    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    Fodot builtFodot;

    /*************************************
     * Roles
     */

    private Set<String> roles;

    public Set<String> getRoles() {
        return new TreeSet<>(roles);
    }

    private void addRole(String roleName){
        if(roleName == null)
            throw new IllegalArgumentException();
        roles.add(roleName);
    }

    /************************************/

    private FodotPredicateDeclaration getPredicate(String predName){
        if(!isPredicateRegistered(predName))
            throw new IllegalArgumentException("Predicate not found!");
        if(isFluentPredicateRegistered(predName))
            return getFluentPredicate(predName);
        return getStaticPredicate(predName);
    }

    private  boolean isPredicateRegistered(String predName){
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

    private boolean isFluentPredicateRegistered(String predName) {
        return (fluentPredicates.containsKey(predName));
    }

    private void addFluentPredicate(FodotPredicateDeclaration pred){
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


    /************************************/

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

    private boolean isStaticPredicateRegistered(String predName) {
        return (staticPredicates.containsKey(predName));
    }

    private void addStaticPredicate(FodotPredicateDeclaration staticPred){
        if(staticPred == null || fluentPredicates.containsKey(staticPred))
            throw new IllegalArgumentException();
        staticPredicates.put(staticPred.getName(), staticPred);
    }

    private void convertFluentPredicateToStatic(FodotPredicateDeclaration pred) {
        if (!fluentPredicates.containsKey(pred.getName()))
            throw new IllegalArgumentException();
        removeFluentPredicate(pred);
        addStaticPredicate(pred);
    }

    /************************************/

    /*************************************
     * Constants
     */

    private Set<String> constants;

    public Set<String> getConstants(){
        return new HashSet<>(constants);
    }

    private void addConstant(String constantName){
        constants.add(constantName);
    }

    private boolean isConstantRegistered(String constantName){
        return !constants.contains(constantName);
    }

    /************************************/

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    @Override
    public void processRoleRelation(GdlRelation relation) {
        // Role: (role player)
        // player = relation.getBody().get(0)
        this.addRole("p_"+relation.getBody().get(0).toString());
    }

    @Override
    public void processInitRelation(GdlRelation relation) {
        // Init: (init pred x1 .. xn)
        // predName
        

    }

    @Override
    public void processStaticPredicateRelation(GdlRelation relation) {

    }

    @Override
    public void processNextRule(GdlRule rule) {

    }

    @Override
    public void processLegalRule(GdlRule rule) {

    }

    @Override
    public void processGoalRule(GdlRule rule) {

    }

    @Override
    public void processTerminalRule(GdlRule rule) {

    }

//    @Override
//    public void processPredicate(GdlTerm gdlTerm) {
//        //Needs a cast to function instead of conversion to sentence?
//        //This term is always a function
//        GdlSentence predSentence = gdlTerm.toSentence();
//    	String predName = predSentence.getName().getValue();
//        int amountOfArguments = predSentence.arity();
//
//        if(!isFluentPredicateRegistered(predName)) {
//            FodotPredicateDeclaration newPred = new FodotPredicateDeclaration(predName,
//                    FodotType.getPlaceHolderList(amountOfArguments));
//        }
//
//        for (int i = 0; i < amountOfArguments; i++) {
//        	GdlTerm term = predSentence.get(i);
//        	if(term.isGround()) {
//        		GdlSentence sent = term.toSentence();
//        		System.out.println(sent.getName() + " with arity " + sent.arity());
//        	}
//		}
//
//        //TODO: check if constants are registered, if so, set pred type
//
//        //TODO: bind constants to fluentPredicates (Type has to be updated)
//        // Observer structure?
//
//        //TODO: register constants?
//
//        //TODO: register new types?
//    }

}
