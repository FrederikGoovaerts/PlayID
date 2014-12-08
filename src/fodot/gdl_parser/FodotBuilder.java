package fodot.gdl_parser;

import fodot.objects.Fodot;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;

import java.util.HashMap;
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

//        this.roles = new TreeSet<>();
//        this.fluentPredicates = new HashMap<>();
//        this.constants = new HashMap<>();

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

    //TODO
    private FodotPredicateDeclaration getPredicate(String predName){
        if(!isFluentPredicateRegistered(predName))
            throw new IllegalArgumentException("Predicate not found!");
        return fluentPredicates.get(predName);
    }

    /*************************************
     * Fluent Predicates
     */

    private HashMap<String,FodotPredicateDeclaration> fluentPredicates;

    public HashMap<String,FodotPredicateDeclaration> getFluentPredicates() {
        return new HashMap<>(fluentPredicates);
    }

    public boolean isFluentPredicateRegistered(String predName) {
        return (fluentPredicates.containsKey(predName)||staticPredicates.containsKey(predName));
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

    //TODO static checker

    /************************************/

    /*************************************
     * Constants
     */

    private Set<String> constants;

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
    public void processRole(GdlRelation relation) {
        // Role has only one body item, the name of a player
        this.addRole("p_"+relation.getBody().get(0).toString());
    }

    @Override
    public void processInit(GdlRelation relation) {
        // Init has one body item, namely a ground predicate representing a
        // truth for the initial state
        GdlTerm predTerm = relation.get(0);
        this.processPredicate(predTerm);

    }

    @Override
    public void processLegal(GdlRelation relation) {

    }

    @Override
    public void processTerminal(GdlRelation relation) {

    }

    @Override
    public void processGoal(GdlRelation relation) {

    }

    @Override
    public void processStaticPredicate(GdlRelation relation) {

    }

    @Override
    public void processPredicate(GdlTerm gdlTerm) {
        //Needs a cast to function instead of conversion to sentence?
        //This term is always a function
        GdlSentence predSentence = gdlTerm.toSentence();
    	String predName = predSentence.getName().getValue();
        int amountOfArguments = predSentence.arity();

        if(!isFluentPredicateRegistered(predName)) {
            FodotPredicateDeclaration newPred = new FodotPredicateDeclaration(predName,
                    FodotType.getPlaceHolderList(amountOfArguments));
        }
        
        for (int i = 0; i < amountOfArguments; i++) {
        	GdlTerm term = predSentence.get(i);
        	if(term.isGround()) {
        		GdlSentence sent = term.toSentence();
        		System.out.println(sent.getName() + " with arity " + sent.arity());
        	}
		}

        //TODO: check if constants are registered, if so, set pred type

        //TODO: bind constants to fluentPredicates (Type has to be updated)
        // Observer structure?

        //TODO: register constants?

        //TODO: register new types?
    }

	@Override
	public void processAction(GdlTerm gdlTerm) {
		// TODO Auto-generated method stub
		
	}

}
