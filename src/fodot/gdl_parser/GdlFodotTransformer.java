package fodot.gdl_parser;

import static fodot.helpers.FodotPartBuilder.createConstant;
import static fodot.helpers.FodotPartBuilder.createExists;
import static fodot.helpers.FodotPartBuilder.createImplies;
import static fodot.helpers.FodotPartBuilder.createPredicate;
import static fodot.helpers.FodotPartBuilder.createPredicateDeclaration;
import static fodot.helpers.FodotPartBuilder.createPredicateTerm;
import static fodot.helpers.FodotPartBuilder.createPredicateTermDeclaration;
import static fodot.helpers.FodotPartBuilder.createVariable;
import static fodot.helpers.FodotPartBuilder.getNaturalNumberType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.Pair;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.gdl_parser.exceptions.GdlParsingOrderException;
import fodot.gdl_parser.util.GdlCastHelper;
import fodot.gdl_parser.util.LTCPool;
import fodot.objects.Fodot;
import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.structure.elements.predicateenum.elements.FodotPredicateEnumerationElement;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;
import fodot.util.FormulaUtil;

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
		actionType.getDeclaration(); //TODO: this has to be fixed!
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

	public FodotConstant convertRawRole(String rawName){
		FodotConstant toReturn = createConstant("p_" + rawName, this.getPlayerType());
		addTranslation(toReturn, rawName);
		return toReturn;
	}

	/*** End of Roles subsection ***/


	/*************************************
	 * Predicate Pool
	 */

	private LTCPool pool;

	public LTCPool getPool(){
		return pool;
	}

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

	private FodotPredicateDeclaration getCompoundStaticPredicate(String predName) {
		return this.pool.getCompoundStaticPredicate(predName);
	}

	private void addCompoundStaticPredicate(FodotPredicateDeclaration pred) {
		this.pool.addCompoundStaticPredicate(pred);
	}

	private boolean isCompoundStaticPredicateRegistered(String predName) {
		return this.pool.isCompoundStaticPredicateRegistered(predName);
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

	public FodotConstant convertRawConstantName(String rawName){
		return convertConstantName(rawName, getAllType());
	}

	public FodotConstant convertConstantName(String rawName, FodotType type){
		FodotConstant toReturn = createConstant("c_" + rawName, type);
		addTranslation(toReturn, rawName);
		return toReturn;
	}

	/*** End of Constants subsection ***/

	/*************************************
	 * Initial values
	 */

	//Looks intriguing, but have no fear.
	private Map<FodotPredicateDeclaration,Set<IFodotPredicateEnumerationElement>>
	initialValues;

	private void addInitialValue(FodotPredicateDeclaration pred, IFodotPredicateEnumerationElement arguments){
		if(initialValues.containsKey(pred)) {
			initialValues.get(pred).add(arguments);
		} else {
			Set<IFodotPredicateEnumerationElement> newSet = new HashSet<>();
			newSet.add(arguments);
			initialValues.put(pred,newSet);
		}
	}

	public Map<FodotPredicateDeclaration, Set<IFodotPredicateEnumerationElement>> getInitialValues() {
		return new HashMap<>(initialValues);
	}

	/*** End of Initial values subsection ***/

	/*************************************
	 * Static values
	 */

	private Map<FodotPredicateDeclaration,Set<IFodotPredicateEnumerationElement>>
	staticValues;

	private void addStaticValue(FodotPredicateDeclaration pred, IFodotPredicateEnumerationElement arguments){
		if(staticValues.containsKey(pred)) {
			staticValues.get(pred).add(arguments);
		} else {
			Set<IFodotPredicateEnumerationElement> newSet = new HashSet<>();
			newSet.add(arguments);
			staticValues.put(pred,newSet);
		}
	}

	public Map<FodotPredicateDeclaration, Set<IFodotPredicateEnumerationElement>> getStaticValues() {
		return new HashMap<>(staticValues);
	}

	/*** End of Static values subsection ***/

	/*************************************
	 * Actions
	 */

	private FodotPredicateDeclaration doPredicateDeclaration;

	public FodotPredicateDeclaration getDoPredicate(){
		return this.doPredicateDeclaration;
	}

	/*** End of Actions subsection ***/

	/*************************************
	 * Next rules
	 */

	private Map<FodotPredicateDeclaration,Set<Pair<FodotPredicate, IFodotFormula>>> nextMap;

	public Map<FodotPredicateDeclaration,Set<Pair<FodotPredicate, IFodotFormula>>> getNextMap() {
		return new HashMap<>(nextMap);
	}

	private void addNext(FodotPredicateDeclaration predicate, Pair<FodotPredicate, IFodotFormula> condition) {
		if(nextMap.containsKey(predicate)){
			nextMap.get(predicate).add(condition);
		} else {
			Set<Pair<FodotPredicate, IFodotFormula>> newSet = new HashSet<>();
			newSet.add(condition);
			nextMap.put(predicate,newSet);
		}
	}

	/*** End of Next rules subsection ***/

	/*************************************
	 * Legal
	 */

	private Map<FodotPredicate,Set<IFodotFormula>> legalMap;

	public Map<FodotPredicate, Set<IFodotFormula>> getLegalMap() {
		return new HashMap<>(legalMap);
	}

	private void addLegal(FodotPredicate predicate, IFodotFormula condition) {
		if(legalMap.containsKey(predicate)){
			legalMap.get(predicate).add(condition);
		} else {
			Set<IFodotFormula> newSet = new HashSet<>();
			newSet.add(condition);
			legalMap.put(predicate,newSet);
		}
	}

	/*** End of Legal subsection ***/

	/*************************************
	 * Terminals
	 */

	private FodotPredicateDeclaration terminalTimePredicateDeclaration;

	Set<IFodotFormula> terminalSet;

	public Set<IFodotFormula> getTerminalSet() {
		return new HashSet<>(terminalSet);
	}

	private void addTerminal(IFodotFormula condition) {
		this.terminalSet.add(condition);
	}

	/*** End of Terminals subsection ***/

	/*************************************
	 * Score
	 */

	private Map<Pair<IFodotTerm, Integer>,Set<IFodotFormula>> scoreMap;

	public Map<Pair<IFodotTerm, Integer>,Set<IFodotFormula>> getScoreMap(){
		return new HashMap<>(scoreMap);
	}

	private void addScore(Pair<IFodotTerm, Integer> score, IFodotFormula condition) {
		if(scoreMap.containsKey(score)){
			scoreMap.get(score).add(condition);
		} else {
			Set<IFodotFormula> newSet = new HashSet<>();
			newSet.add(condition);
			scoreMap.put(score,newSet);
		}
	}

	/*** End of Score subsection ***/

	/*************************************
	 * Compound Static Predicates
	 */
	private Map<FodotPredicateDeclaration,Set<Pair<FodotPredicate, IFodotFormula>>> compoundMap;

	public Map<FodotPredicateDeclaration,Set<Pair<FodotPredicate, IFodotFormula>>> getCompoundMap() {
		return new HashMap<>(compoundMap);
	}

	private void addCompound(
			FodotPredicateDeclaration predicate,
			Pair<FodotPredicate, IFodotFormula> pair) {
		if(compoundMap.containsKey(predicate)){
			compoundMap.get(predicate).add(pair);
		} else {
			Set<Pair<FodotPredicate, IFodotFormula>> newSet = new HashSet<>();
			newSet.add(pair);
			compoundMap.put(predicate,newSet);
		}
	}

	/*** End of Compound Static Predicates subsection ***/

	/***************************************************************************
	 * Class Methods
	 **************************************************************************/

	public void cleanAndInitializeBuilder(){
		this.initialValues = new HashMap<>();
		this.staticValues = new HashMap<>();
		this.scoreMap = new HashMap<>();
		this.nextMap = new HashMap<>();
		this.legalMap = new HashMap<>();
		this.terminalSet = new HashSet<>();
		this.compoundMap = new HashMap<>();
		this.processingRules = false;
		this.buildDefaultTypes();
		this.pool = new LTCPool(this.timeType);

		List<FodotType> typeList = new ArrayList<>();
		typeList.add(getTimeType());
		typeList.add(getPlayerType());
		typeList.add(getActionType());
		this.doPredicateDeclaration = createPredicateDeclaration("do", typeList);

		ArrayList<FodotType> typeList2 = new ArrayList<>();
		typeList2.add(getTimeType());
		this.terminalTimePredicateDeclaration = createPredicateDeclaration("terminalTime", typeList2);

	}

	@Override
	public Fodot buildFodot() {
		FodotGameFactory factory = new FodotGameFactory(this,
				pool,
				getDoPredicate(),
				terminalTimePredicateDeclaration);
		return factory.createFodot();
	}


	public IFodotTerm processTerm(GdlTerm term, Map<GdlVariable,FodotVariable> variableMap) {
		return processTerm(term, getAllType(), variableMap);
	}

	/**
	 * Processes a GdlTerm. If it's a variable, it will be added to the given variablemap
	 * @param term			Term to convert
	 * @param variableMap	Term to add to variablemap
	 * @return				Converted IFodotTerm
	 */
	public IFodotTerm processTerm(GdlTerm term, FodotType argType, Map<GdlVariable,FodotVariable> variableMap) {
		IFodotTerm fodotTerm;

		if (term.isGround()) { //Term is a constant
			fodotTerm = convertConstantName(term.toString(), argType);
		} else if(variableMap != null && variableMap.containsKey(term)) { //Term is already known in the variablemapping
			fodotTerm = variableMap.get(term);
			if (fodotTerm.getType() != argType && argType != getAllType()) {
				((FodotVariable) fodotTerm).setType(argType); //TODO: Something should probably signal all other instances using this variable so they update their types!
			}
			
		} else if (term instanceof GdlVariable) { //Term is an new variable
			GdlVariable gdlVar = (GdlVariable) term;
			FodotVariable temp = createVariable(gdlVar.getName(), argType, new HashSet<FodotVariable>(variableMap.values()));
			if (variableMap != null) {
				variableMap.put(gdlVar, temp);
			}
			fodotTerm = temp;
		} else { //Term is something else, is it a function?
			System.out.println("Unresolved term: " + term + "\n-->" + term.getClass());
			throw new IllegalArgumentException("We're dealing with a function?!");
		}
		return fodotTerm;
	}

	//TODO: mock gdl variables maken om in de map te steken zodat ze bezet zijn.
	public FodotVariable createTimeVariable(Map<GdlVariable,FodotVariable> variableMap) {
		return createVariable("t", getTimeType(), new HashSet<FodotVariable>(variableMap.values()));
	}
	
	public FodotVariable createAllVariable(Map<GdlVariable,FodotVariable> variableMap) {
		return createVariable("v", getAllType(), new HashSet<FodotVariable>(variableMap.values()));
	}
	
	public FodotVariable createActionVariable(Map<GdlVariable,FodotVariable> variableMap) {
		return createVariable("act", getActionType(), new HashSet<FodotVariable>(variableMap.values()));
	}
	
	public FodotVariable createPlayerVariable(Map<GdlVariable,FodotVariable> variableMap) {
		return createVariable("p", getPlayerType(), new HashSet<FodotVariable>(variableMap.values()));
	}
	
	/**********************************************
	 *  Processing of Sentence Arguments to List of Fodot Terms
	 ***********************************************/
	
	/**
	 * This method can process all GdlTerms of a GdlSentence and converts them to FodotTerms
	 * All variables will be added to the variablemap
	 * If a variablemap contains a variable who's type is more specific than the "all"-type,
	 * it will replace that type in th given FodotDeclaration
	 * @param sentence
	 * @param declaration
	 * @param variableMap
	 * @return
	 */
	public List<IFodotTerm> processSentenceArguments(
			GdlSentence sentence, FodotArgumentListDeclaration declaration,
			int argumentOffset, HashMap<GdlVariable,FodotVariable> variableMap) {

		List<IFodotTerm> elements = new ArrayList<IFodotTerm>(); 
		for (int i = 0; i < sentence.arity(); i++) {
			FodotType currentArgType = declaration.getArgumentType(i+argumentOffset);
			
			GdlTerm term = sentence.get(i);
			IFodotTerm element = processTerm(term, currentArgType, variableMap);
			elements.add(element);

			//Improve declaration types (if variable was already mapped)
			FodotType actualType = element.getType();
			if (currentArgType != actualType && currentArgType == getAllType()) {
				System.out.println("fixed " + declaration.getName() + " arg("+i+") to " + actualType);
				declaration.setArgumentType(i+argumentOffset, actualType);
			}


		}
		return elements;
	}
	
	public List<IFodotTerm> processSentenceArguments(
			GdlSentence sentence,FodotArgumentListDeclaration declaration,
			HashMap<GdlVariable,FodotVariable> variableMap) {
		return processSentenceArguments(sentence, declaration, 0, variableMap);
	}
	
	public List<IFodotTerm> processSentenceArgumentsTimed(GdlSentence sentence,
			FodotArgumentListDeclaration declaration, HashMap<GdlVariable,FodotVariable> variableMap) {
		List<IFodotTerm> arguments = new ArrayList<IFodotTerm>();		
		arguments.add(createTimeVariable(variableMap));
		arguments.addAll(processSentenceArguments(sentence, declaration, arguments.size(), variableMap));
		return arguments;
	}
	
	
	
	/**********************************************/

	
	
	
	@Override
	public void processRoleRelation(GdlRelation relation) {
		if(processingRules)
			throw new GdlParsingOrderException("A rule has already been processed," +
					"processing relations is not allowed anymore.");

		// Role: (role player)
		GdlConstant player = relation.getBody().get(0).toSentence().getName();
		this.addRole(convertRawRole(player.toString()));
	}

	@Override
	public void processInitRelation(GdlRelation relation) {
		if(processingRules)
			throw new GdlParsingOrderException("A rule has already been processed," +
					"processing relations is not allowed anymore.");

		// Init: (init (pred x1 .. xn))

		GdlSentence predicate = relation.getBody().get(0).toSentence();


		FodotPredicateDeclaration fodotPredicate = processPredicate(predicate);
		int predArity = predicate.arity();

		List<IFodotTypeEnumerationElement> initValues = new ArrayList<IFodotTypeEnumerationElement>();

		for (int i = 0; i < predArity; i++) {
			//PLS TRUST ME
			String constant = predicate.get(i).toSentence().getName().getValue();
			initValues.add(convertRawConstantName(constant));
		}

		this.addInitialValue(fodotPredicate, new FodotPredicateEnumerationElement(initValues));
	}

	@Override
	public void processStaticPredicateRelation(GdlRelation relation) {
		if(processingRules)
			throw new GdlParsingOrderException("A rule has already been processed," +
					"processing relations is not allowed anymore.");

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


		List<IFodotTypeEnumerationElement> staticValues = new ArrayList<IFodotTypeEnumerationElement>();

		for (int i = 0; i < predArity; i++) {
			//PLS TRUST ME
			String constantName = relation.get(i).toSentence().getName().getValue();
			FodotConstant newConstant = convertRawConstantName(constantName);
			staticValues.add(newConstant);
			if(!isConstantRegistered(newConstant)) {
				this.addConstant(newConstant);
			}
		}

		this.addStaticValue(pred, new FodotPredicateEnumerationElement(staticValues));

	}

	@Override
	public void processNextRule(GdlRule rule) {
		if(!rule.getHead().getName().getValue().equals("next"))
			throw new IllegalArgumentException("Given rule is not a 'next' rule!");
		this.processingRules = true;

		HashMap<GdlVariable,FodotVariable> variableMap = new HashMap<>();

		//process (fluent) predicate in head
		GdlTerm nextGdlTerm = rule.getHead().get(0);
		IFodotTerm nextFodotTerm;
		if (nextGdlTerm instanceof GdlVariable) {
			nextFodotTerm = processTerm(nextGdlTerm, variableMap);
			throw new IllegalStateException("You can't give a variable as argument for 'next', dummy!");
		} else {
		
			GdlSentence predSentence = nextGdlTerm.toSentence();
			FodotPredicateDeclaration originalPredicateDecl = this.processPredicate(predSentence);
			FodotPredicateDeclaration causePredDecl = pool.getCauseOf(originalPredicateDecl);

			List<IFodotTerm> arguments = processSentenceArgumentsTimed(predSentence, causePredDecl, variableMap);

			FodotPredicate causePred = createPredicate(
					causePredDecl,
					arguments
					);

			//generate IFodotFormula from the body
			IFodotFormula condition = GdlCastHelper.generateFodotFormulaFrom(
					rule.getBody(),
					variableMap,
					this
					);

			//add the combination as a next rule
			this.addNext(originalPredicateDecl, Pair.of(causePred, condition));
		}
	}

	@Override
	public void processLegalRule(GdlRule rule) {
		// legal(player, action) ==> do(time,player,action)

		IFodotTerm player = convertRawRole(rule.getHead().get(0).toString());

		HashMap<GdlVariable,FodotVariable> variableMap = new HashMap<>();

		GdlTerm actionGdlTerm = rule.getHead().get(1);
		IFodotTerm actionTerm;
		if (actionGdlTerm instanceof GdlVariable) {
			actionTerm = processTerm(actionGdlTerm, getActionType(), variableMap);
		} else {
			GdlSentence actionSent = actionGdlTerm.toSentence();

			FodotPredicateTermDeclaration actionDecl =
					createPredicateTermDeclaration(
							actionSent.getName().getValue(),
							FodotType.getSameTypeList(actionSent.arity(),getAllType()),
							getActionType()
							);

			List<IFodotTerm> actionTermArguments = new ArrayList<>();
			for (GdlTerm term : actionSent.getBody()) {
				IFodotTerm actionVar = processTerm(term, variableMap);
				actionTermArguments.add(actionVar);
			}

			actionTerm =
					createPredicateTerm(
							actionDecl,
							actionTermArguments
							);
		}

		List<IFodotTerm> doArguments =
				Arrays.asList(
						createTimeVariable(variableMap),
						player,
						actionTerm
						);

		FodotPredicate doPred =
				createPredicate(
						this.getDoPredicate(),
						doArguments
						);

		//generate IFodotFormula from the body
		IFodotFormula condition = GdlCastHelper.generateFodotFormulaFrom(
				rule.getBody(),
				variableMap,
				this
				);

		if(!removeTimeVars(condition.getFreeVariables()).isEmpty()
				&& removeTimeVars(doPred.getFreeVariables()).isEmpty()){
			condition = createExists(removeTimeVars(condition.getFreeVariables()),
					condition);
		}

		//add the combination as a next rule
		this.addLegal(doPred, condition);
	}

	@Override
	public void processGoalRule(GdlRule rule) {
		if(!rule.getHead().getName().getValue().equals("goal"))
			throw new IllegalArgumentException("Rule is not a goal rule!");
		this.processingRules = true;

		HashMap<GdlVariable, FodotVariable> variableMap = new HashMap<>();

		GdlTerm playerGdlTerm = rule.getHead().get(0);
		GdlTerm scoreGdlTerm = rule.getHead().get(1);
		
		IFodotTerm playerTerm;
		if (playerGdlTerm instanceof GdlVariable) {
			playerTerm = processTerm(playerGdlTerm, getPlayerType(), variableMap);	
		} else {
			playerTerm = convertRawRole(playerGdlTerm.toSentence().getName().getValue());
		}
//		if (scoreGdlTerm instanceof GdlVariable) {
//			IFodotTerm scoreTerm = processTerm(scoreGdlTerm, variableMap);			
//		}
		
		Pair<IFodotTerm, Integer> score = Pair.of(playerTerm,
				Integer.parseInt(scoreGdlTerm.toSentence().getName().getValue()));

		IFodotFormula condition = GdlCastHelper.generateFodotFormulaFrom(
				rule.getBody(),
				variableMap,
				this
				);

		if(!removeTimeVars(condition.getFreeVariables()).isEmpty()){
			condition = createExists(removeTimeVars(condition.getFreeVariables()),
					condition);
		}

		IFodotFormula extendedCondition =
				createImplies(
						createPredicate(
								terminalTimePredicateDeclaration,
								createTimeVariable(variableMap)
								), condition
						);

		this.addScore(score,extendedCondition);
	}

	@Override
	public void processTerminalRule(GdlRule rule) {
		if(!rule.getHead().getName().getValue().equals("terminal"))
			throw new IllegalArgumentException("Rule is not a terminal rule!");
		this.processingRules = true;
		HashMap<GdlVariable, FodotVariable> variableMap = new HashMap<>();

		IFodotFormula condition = GdlCastHelper.generateFodotFormulaFrom(
				rule.getBody(),
				variableMap,
				this
				);

		this.addTerminal(condition);

	}

	@Override
	public void processDefinitionRule(GdlRule rule) {
		this.processingRules = true;
		HashMap<GdlVariable,FodotVariable> variableMap = new HashMap<>();

		//process (compound static) predicate in head
		GdlSentence predSentence = rule.getHead();
		FodotPredicateDeclaration originalPredicate =
				this.processCompoundStaticPredicate(predSentence);

		List<IFodotTerm> variables = new ArrayList<>();
		variables.add(createTimeVariable(variableMap));
		for (GdlTerm term : predSentence.getBody()) {
			//GdlSentence sentence = gdlTerm.toSentence();
			IFodotTerm fodotTerm = processTerm(term, variableMap);
			variables.add(fodotTerm);
		}
		FodotPredicate compoundStaticPred = createPredicate(
				pool.getCompoundTimedVerionOf(originalPredicate),
				variables
				);

		//generate IFodotFormula from the body
		IFodotFormula condition = GdlCastHelper.generateFodotFormulaFrom(
				rule.getBody(),
				variableMap,
				this
				);

		//add the combination as a next rule
		this.addCompound(originalPredicate, Pair.of(compoundStaticPred, condition));
	}

	//TODO: nakijken waar deze return niet gebruikt wordt
	public FodotPredicateDeclaration processPredicate(GdlSentence predSentence) {
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

	public FodotPredicateDeclaration processCompoundStaticPredicate(GdlSentence predSentence) {
		//Predicate: (pred x1 .. xn)

		String predName = predSentence.getName().getValue();
		int amountOfArguments = predSentence.arity();

		FodotPredicateDeclaration predDecl;

		//If necessary, register predicate
		if(!isCompoundStaticPredicateRegistered(predName)) {
			predDecl = new FodotPredicateDeclaration(
					predName,
					FormulaUtil.createTypeList(allType, amountOfArguments)
					);
			this.addCompoundStaticPredicate(predDecl);
		} else {
			predDecl = this.getCompoundStaticPredicate(predName);
			if(predDecl.getAmountOfArgumentTypes() != amountOfArguments)
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

		return predDecl;
	}

	Map<IFodotEnumerationElement, String> translationsFromFodot = new HashMap<IFodotEnumerationElement, String>();
	Map<String, IFodotEnumerationElement> translationsFromGdl = new HashMap<String, IFodotEnumerationElement>();

	public void addTranslation(IFodotEnumerationElement fodot, String gdl) {
		translationsFromFodot.put(fodot, gdl);
		translationsFromGdl.put(gdl, fodot);
	}

	public String translateToGdl(IFodotEnumerationElement fodot) {
		return translationsFromFodot.get(fodot);
	}

	public IFodotEnumerationElement translateToFodot(String gdl) {
		return translationsFromGdl.get(gdl);
	}

	/***************************************************************************
	 * Helper methods
	 **************************************************************************/

	public Set<FodotVariable> removeTimeVars(Set<FodotVariable> vars){
		Set<FodotVariable> toReturn = new HashSet<>();
		for (FodotVariable var : vars) {
			if(!var.getType().equals(timeType))
				toReturn.add(var);
		}
		return toReturn;
	}

}
