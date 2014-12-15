package fodot.gdl_parser;

import fodot.objects.Fodot;
import fodot.objects.sentence.formulas.argumented.FodotPredicate;
import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import static fodot.helpers.FodotPartBuilder.*;

import org.ggp.base.util.Pair;
import org.ggp.base.util.gdl.grammar.*;

import java.util.*;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlFodotTransformer implements GdlTransformer{

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public GdlFodotTransformer(){
        this.cleanAndInitializeBuilder();
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    /*************************************
     * Default Types
     */

    private FodotType timeType;
    private FodotType playerType;
    private FodotType actionType;
    private FodotType scoreType;
    private FodotType allType;

    private void buildDefaultTypes(){
        this.timeType = new FodotType("Time");
        timeType.addSupertype(getNaturalNumberType());
        this.playerType = new FodotType("Player");
        this.actionType = new FodotType("Action");
        this.scoreType = new FodotType("ScoreType");
        scoreType.addSupertype(getNaturalNumberType());
        this.allType = new FodotType("all");
    }

    public FodotType getTimeType(){
        return this.timeType;
    }

    public FodotType getPlayerType() {
        return playerType;
    }

    public FodotType getActionType() {
        return actionType;
    }

    public FodotType getScoreType() {
        return scoreType;
    }

    public FodotType getAllType() {
        return allType;
    }

    /*** End of Default Types subsection ***/

    /*************************************
     * Roles
     */

    private FodotConstant ownRole;

    private void addRole(FodotConstant role){
        if(role == null)
            throw new IllegalArgumentException();
        if(ownRole == null)
            ownRole = role;
        playerType.addDomainElement(role);
    }

    public FodotConstant getOwnRole() {
        if (ownRole == null) {
            throw new IllegalStateException("No roles present in this class!");
        } else {
            return ownRole;
        }
    }

    private FodotConstant convertRawRole(String rawName){
        return createConstant("p_" + rawName, this.getPlayerType());
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

    private void addConstant(FodotConstant constantName){
        allType.addDomainElement(constantName);
    }

    private boolean isConstantRegistered(FodotConstant constantName){
        return !allType.containsElement(constantName);
    }

    private FodotConstant convertRawConstantName(String rawName){
        return createConstant("c_" + rawName, allType);
    }

    /*** End of Constants subsection ***/

    /*************************************
     * Initial values
     */

    //Looks intriguing, but have no fear.
    private Map<FodotPredicateDeclaration,Set<FodotConstant[]>>
            initialValues;

    private void addInitialValue(FodotPredicateDeclaration pred, FodotConstant[] arguments){
        if(initialValues.containsKey(pred)) {
            initialValues.get(pred).add(arguments);
        } else {
            Set<FodotConstant[]> newSet = new HashSet<FodotConstant[]>();
            newSet.add(arguments);
            initialValues.put(pred,newSet);
        }
    }

    public Map<FodotPredicateDeclaration, Set<FodotConstant[]>> getInitialValues() {
        return new HashMap<>(initialValues);
    }

    /*** End of Initial values subsection ***/

    /*************************************
     * Static values
     */

    private Map<FodotPredicateDeclaration,Set<FodotConstant[]>>
        staticValues;

    private void addStaticValue(FodotPredicateDeclaration pred, FodotConstant[] arguments){
        if(staticValues.containsKey(pred)) {
            staticValues.get(pred).add(arguments);
        } else {
            Set<FodotConstant[]> newSet = new HashSet<FodotConstant[]>();
            newSet.add(arguments);
            staticValues.put(pred,newSet);
        }
    }

    public Map<FodotPredicateDeclaration, Set<FodotConstant[]>> getStaticValues() {
        return new HashMap<>(staticValues);
    }

    /*** End of Static values subsection ***/

    /*************************************
     * Actions
     */


    /*** End of Actions subsection ***/

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    public void cleanAndInitializeBuilder(){
        this.fluentPredicates = new HashMap<>();
        this.staticPredicates = new HashMap<>();
        this.initialValues = new HashMap<>();
        this.staticValues = new HashMap<>();
        this.buildDefaultTypes();
    }

    @Override
    public Fodot builFodot() {
        FodotGameFactory factory = new FodotGameFactory(this);
        return factory.createFodot();
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

        FodotConstant[] initValues = new FodotConstant[predArity];

        for (int i = 0; i < predArity; i++) {
            //PLS TRUST ME
            String constant = predicate.get(i).toSentence().getName().getValue();
            initValues[i] = convertRawConstantName(constant);
        }

        this.addInitialValue(fodotPredicate, initValues);
    }

    @Override
    public void processStaticPredicateRelation(GdlRelation relation) {
        // Static: (pred x1 .. xn)
        String predName = relation.getName().getValue();
        int predArity = relation.arity();

        FodotPredicateDeclaration newPred;

        //If necessary, register predicate as static
        if (!isStaticPredicateRegistered(predName)) {
            if (isFluentPredicateRegistered(predName)){
                convertFluentPredicateToStatic(getFluentPredicate(predName));
                newPred = this.getPredicate(predName);
            } else {

                newPred = new FodotPredicateDeclaration(predName,
                        FodotType.getSameTypeList(predArity,this.allType));
                this.addStaticPredicate(newPred);
            }
        } else {
            newPred = this.getPredicate(predName);
            if(newPred.getAmountOfArgumentTypes() != predArity)
                throw new IllegalStateException("Predicate differs in arity from before!");
        }

        FodotConstant[] staticValues = new FodotConstant[predArity];

        for (int i = 0; i < predArity; i++) {
            //PLS TRUST ME
            String constantName = relation.get(i).toSentence().getName().getValue();
            staticValues[i] = convertRawConstantName(constantName);
        }

        this.addStaticValue(newPred, staticValues);

    }

    @Override
    public void processNextRule(GdlRule rule) {

    }

    @Override
    public void processLegalRule(GdlRule rule) {

    }

    @Override
    public void processGoalRule(GdlRule rule) {
        Pair<String, Integer> score;

        GdlSentence scoreSentence = rule.getHead();
        if(!scoreSentence.getName().getValue().equals("goal")) {
            throw new IllegalArgumentException("Rule is not a goal rule!");
        }

        score = Pair.of(scoreSentence.get(0).toSentence().getName().getValue(),
                Integer.parseInt(scoreSentence.get(1).toSentence().getName().getValue()));

        FodotPredicate[] conditions = new FodotPredicate[rule.arity()];

        for (GdlLiteral literal : rule.getBody()) {

        }
    }

    @Override
    public void processTerminalRule(GdlRule rule) {

    }

    @Override
    public void processDefinitionRule(Object rule) {

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
                    FodotType.getSameTypeList(amountOfArguments,allType));
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
                FodotConstant constantName = convertRawConstantName(term.toSentence().getName().getValue());
                if(!isConstantRegistered(constantName))
                    addConstant(constantName);
        	}
		}
    }

}
