package fodot.gdl_parser;

import fodot.gdl_parser.exceptions.GdlParsingOrderException;
import fodot.gdl_parser.util.GdlCastHelper;
import fodot.gdl_parser.util.LTCPool;
import fodot.objects.Fodot;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.formulas.argumented.FodotPredicate;
import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import static fodot.helpers.FodotPartBuilder.*;

import org.ggp.base.util.Pair;
import org.ggp.base.util.gdl.grammar.*;

import java.util.*;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * For correct utilization of this class, non-GdlRules should be processed before
 * GdlRules. If this is not respected, a GdlParsingOrderException will be thrown.
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
     * Processing state
     */

    /* This boolean is false when no GdlRules have been read in yet */
    private boolean processingRules;

    /*** End of Processing state subsection ***/

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


    /*************************************
     * Predicate Pool
     */

    private LTCPool pool;

    //Check wether an "external" pool is this object's pool
    public boolean isInternalPool(LTCPool pool){
        return this.pool == pool;
    }

    private FodotPredicateDeclaration getPredicate(String predName){
        return this.pool.getPredicate(predName);
    }

    private  boolean isPredicateRegistered(String predName){
        return pool.isPredicateRegistered(predName);
    }

    private boolean isFluentPredicateRegistered(String predName) {
        return pool.isFluentPredicateRegistered(predName);
    }

    private boolean isStaticPredicateRegistered(String predName) {
        return pool.isStaticPredicateRegistered(predName);
    }

    private void addFluentPredicate(FodotPredicateDeclaration pred){
        this.pool.addFluentPredicate(pred);
    }

    private void convertFluentPredicateToStatic(String pred) {
        this.pool.convertFluentPredicateToStatic(pred);
    }

    private void addStaticPredicate(FodotPredicateDeclaration staticPred){
        this.pool.addStaticPredicate(staticPred);
    }


    /*** End of Predicate Pool subsection ***/

    /*************************************
     * Constants
     */

    private void addConstant(FodotConstant constant){
        allType.addDomainElement(constant);
    }

    private boolean isConstantRegistered(FodotConstant constant){
        return allType.containsElement(constant);
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
            Set<FodotConstant[]> newSet = new HashSet<>();
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
            Set<FodotConstant[]> newSet = new HashSet<>();
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


    /*************************************
     * Score
     */

    private Map<Pair<String, Integer>,Set<IFodotFormula>> scoreMap;

    private void addScore(Pair<String, Integer> score, IFodotFormula condition) {

        //TODO
    }

    /*** End of Score subsection ***/

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    public void cleanAndInitializeBuilder(){
        this.initialValues = new HashMap<>();
        this.staticValues = new HashMap<>();
        this.scoreMap = new HashMap<>();
        this.processingRules = false;
        this.buildDefaultTypes();
        this.pool = new LTCPool(this.timeType);
    }

    @Override
    public Fodot buildFodot() {
        FodotGameFactory factory = new FodotGameFactory(this,pool);
        return factory.createFodot();
    }

    @Override
    public void processRoleRelation(GdlRelation relation) {
        if(processingRules)
            throw new GdlParsingOrderException("A rule has already been processed," +
                    "processing realtions is not allowed anymore.");

        // Role: (role player)
        GdlConstant player = relation.getBody().get(0).toSentence().getName();
        this.addRole(convertRawRole(player.toString()));
    }

    @Override
    public void processInitRelation(GdlRelation relation) {
        if(processingRules)
            throw new GdlParsingOrderException("A rule has already been processed," +
                    "processing realtions is not allowed anymore.");

        // Init: (init (pred x1 .. xn))

        GdlSentence predicate = relation.getBody().get(0).toSentence();


        FodotPredicateDeclaration fodotPredicate = processPredicate(predicate);
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
        if(processingRules)
            throw new GdlParsingOrderException("A rule has already been processed," +
                    "processing realtions is not allowed anymore.");

        // Static: (pred x1 .. xn)
        String predName = relation.getName().getValue();
        int predArity = relation.arity();

        FodotPredicateDeclaration pred;

        //If necessary, register predicate as static
        if (!isStaticPredicateRegistered(predName)) {
            if (isFluentPredicateRegistered(predName)){
                convertFluentPredicateToStatic(predName);
                pred = this.getPredicate(predName);
            } else {
                pred = new FodotPredicateDeclaration(predName,
                        FodotType.getSameTypeList(predArity, this.allType));
                this.addStaticPredicate(pred);
            }
        } else {
            pred = this.getPredicate(predName);
            if(pred.getAmountOfArgumentTypes() != predArity)
                throw new IllegalStateException("Predicate differs in arity from before!");
        }

        FodotConstant[] staticValues = new FodotConstant[predArity];

        for (int i = 0; i < predArity; i++) {
            //PLS TRUST ME
            String constantName = relation.get(i).toSentence().getName().getValue();
            FodotConstant newConstant = convertRawConstantName(constantName);
            staticValues[i] = newConstant;
            if(!isConstantRegistered(newConstant)) {
                this.addConstant(newConstant);
            }
        }

        this.addStaticValue(pred, staticValues);

    }


    @Override
    public void processNextRule(GdlRule rule) {
        if(!rule.getHead().getName().getValue().equals("next"))
            throw new IllegalArgumentException("Given rule is not a 'next' rule!");
        this.processingRules = true;

        HashMap<String,FodotVariable> variableMap = new HashMap<>();

        //process (fluent) predicate in head
        GdlSentence predSentence = rule.getHead().get(0).toSentence();
        this.processPredicate(predSentence);

        //generate IFodotFormula from the body
        
        
        //add the combination as a next rule
    }

    @Override
    public void processLegalRule(GdlRule rule) {
        this.processingRules = true;
        //TODO
    }

    @Override
    public void processGoalRule(GdlRule rule) {
        if(!rule.getHead().getName().getValue().equals("goal"))
            throw new IllegalArgumentException("Rule is not a goal rule!");
        this.processingRules = true;

        HashMap<String,FodotVariable> variableMap = new HashMap<>();

        Pair<String, Integer> score = Pair.of(rule.getHead().get(0).toSentence().getName().getValue(),
                Integer.parseInt(rule.getHead().get(1).toSentence().getName().getValue()));

        IFodotFormula condition = GdlCastHelper.generateFodotFormulaFrom(
                rule.getBody(),
                variableMap,
                this
        );

        this.addScore(score,condition);
    }

    @Override
    public void processTerminalRule(GdlRule rule) {
        this.processingRules = true;
        //TODO
    }

    @Override
    public void processDefinitionRule(Object rule) {
        this.processingRules = true;
        //TODO
    }

    FodotPredicateDeclaration processPredicate(GdlSentence predSentence) {
        //This can still be used when rules are processed, but should not be used.

        //Predicate: (pred x1 .. xn)

    	String predName = predSentence.getName().getValue();
        int amountOfArguments = predSentence.arity();

        FodotPredicateDeclaration pred;

        //If necessary, register predicate
        if(!isPredicateRegistered(predName)) {
            pred = new FodotPredicateDeclaration(
                    predName,
                    FodotType.getSameTypeList(amountOfArguments, allType)
            );
            this.addFluentPredicate(pred);
        } else {
            pred = this.getPredicate(predName);
            if(pred.getAmountOfArgumentTypes() != amountOfArguments)
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

        return pred;
    }

}
