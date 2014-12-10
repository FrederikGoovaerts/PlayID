package fodot.gdl_parser;

import fodot.objects.Fodot;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

import org.ggp.base.util.gdl.grammar.*;

import java.util.*;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotBuilder implements GdlTransformer{

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotBuilder(){
        this.cleanAndInitializeBuilder();
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

    /*** End of Roles subsection ***/

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

    private static String convertRawRole(String rawName){
        return "p_" + rawName;
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

    /*** End of Static Predicates subsection ***/

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

    private static String convertRawConstantName(String rawName){
        return "c_" + rawName;
    }

    /*** End of Constants subsection ***/

    /*************************************
     * Initial values
     */

    //Looks intriguing, but have no fear.
    private Map<FodotPredicateDeclaration,Set<String[]>>
            initialValues;

    private void addInitialValue(FodotPredicateDeclaration pred, String[] arguments){
        if(initialValues.containsKey(pred)) {
            initialValues.get(pred).add(arguments);
        } else {
            Set<String[]> newSet = new HashSet<String[]>();
            newSet.add(arguments);
            initialValues.put(pred,newSet);
        }
    }

    /*** End of Initial values subsection ***/

    /*************************************
     * Static values
     */

    private Map<FodotPredicateDeclaration,Set<String[]>>
        staticValues;


    /*** End of Static values subsection ***/

    /*************************************
     * Actions
     */

    private FodotType actionType;

    /*** End of Actions subsection ***/

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    public void cleanAndInitializeBuilder(){
        this.actionType = new FodotType("Action");

        this.roles = new TreeSet<>();
        this.fluentPredicates = new HashMap<>();
        this.staticPredicates = new HashMap<>();
        this.constants = new TreeSet<>();
        this.initialValues = new HashMap<>();
        this.staticValues = new HashMap<>();
    }

    @Override
    public void processRoleRelation(GdlRelation relation) {
        // Role: (role player)
        GdlConstant player = relation.getBody().get(0).toSentence().getName();
        this.addRole(convertRawRole(player.toString()));
    }

    @Override
    public void processInitRelation(GdlRelation relation) {
        // Init: (init pred x1 .. xn)

        GdlSentence predicate = relation.getBody().get(0).toSentence();
        processPredicate(predicate);

        FodotPredicateDeclaration fodotPredicate =
                this.getPredicate(predicate.getName().getValue());
        int predArity = predicate.arity();

        String[] initValues = new String[predArity];

        for (int i = 0; i < predArity; i++) {
            //PLS TRUST ME
            String constant = predicate.get(i).toSentence().getName().getValue();
            initValues[i] = convertRawConstantName(constant);
        }

        this.addInitialValue(fodotPredicate,initValues);
    }

    @Override
    public void processStaticPredicateRelation(GdlRelation relation) {
        // Static: (pred x1 .. xn)
        String predName = relation.getName().getValue();
        int amountOfArguments = relation.arity();

        FodotPredicateDeclaration newPred;

        //If necessary, register predicate as static
        if (!isStaticPredicateRegistered(predName)) {
            if (isFluentPredicateRegistered(predName)){
                convertFluentPredicateToStatic(getFluentPredicate(predName));
            } else {
                newPred = new FodotPredicateDeclaration(predName,
                        FodotType.getPlaceHolderList(amountOfArguments));
                this.addStaticPredicate(newPred);
            }
        } else {
            newPred = this.getPredicate(predName);
            if(newPred.getAmountOfArgumentTypes() != amountOfArguments)
                throw new IllegalStateException("Predicate differs in arity from before!");
        }

        //TODO: make static sets/maps

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

    private void processPredicate(GdlTerm predTerm){
        this.processPredicate(predTerm.toSentence());
    }

    private void processPredicate(GdlSentence predSentence) {
        //Needs a cast to function instead of conversion to sentence?
        //This term is always a function
    	String predName = predSentence.getName().getValue();
        int amountOfArguments = predSentence.arity();

        FodotPredicateDeclaration newPred;

        //If necessary, register predicate
        if(!isPredicateRegistered(predName)) {
            newPred = new FodotPredicateDeclaration(predName,
                    FodotType.getPlaceHolderList(amountOfArguments));
            this.addFluentPredicate(newPred);
        } else {
            newPred = this.getPredicate(predName);
            if(newPred.getAmountOfArgumentTypes() != amountOfArguments)
                throw new IllegalStateException("Predicate differs in arity from before!");
        }

        //Scan all body elements, if they are constants (ground in a non-built-in
        // predicate is equivalent with being a constant), add them as a constant
        // with the same type as its place in the predicate. Predicate should
        // be registered by now.
        for (int i = 0; i < amountOfArguments; i++) {
        	GdlTerm term = predSentence.get(i);
        	if(term.isGround()) {
                //Term is a constant, and only has a name, and arity 0
                String constantName = convertRawConstantName(term.toSentence().getName().getValue());
                if(!isConstantRegistered(constantName))
                    addConstant(constantName);
        	}
		}
    }

}
