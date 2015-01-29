package fodot.gdl_parser.secondphase;

import static fodot.objects.FodotElementBuilder.createConstant;
import static fodot.objects.FodotElementBuilder.createExists;
import static fodot.objects.FodotElementBuilder.createImplies;
import static fodot.objects.FodotElementBuilder.createPredicate;
import static fodot.objects.FodotElementBuilder.createPredicateDeclaration;
import static fodot.objects.FodotElementBuilder.createType;
import static fodot.objects.FodotElementBuilder.createTypeFunctionEnumerationElement;
import static fodot.objects.FodotElementBuilder.getNaturalNumberType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.Pair;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.communication.gdloutput.IFodotGdlTranslator;
import fodot.exceptions.gdl.GdlParsingOrderException;
import fodot.exceptions.gdl.GdlTransformationException;
import fodot.gdl_parser.FodotGameFactory;
import fodot.gdl_parser.GdlTransformer;
import fodot.gdl_parser.util.LTCPool;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.elements.predicateenum.elements.FodotPredicateEnumerationElement;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typenum.elements.FodoTypeFunctionEnumerationElement;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotFunction;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.util.FormulaUtil;
import fodot.util.NameUtil;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * For correct utilization of this class, non-GdlRules should be processed before
 * GdlRules. If this is not respected, a GdlParsingOrderException will be thrown.
 */
public class GdlFodotTransformer implements GdlTransformer, IFodotGdlTranslator {

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
		this.timeType = createType("Time");
		timeType.addSupertype(getNaturalNumberType());

		this.playerType = createType("Player");
		this.actionType = createType("Action");
		actionType.getDeclaration(); //TODO: this has to be fixed!	 //Update 17-01-15: What has to be fixes? -T.
		this.scoreType = createType("ScoreType");
		scoreType.addSupertype(getNaturalNumberType());
		this.allType = createType("All");

		//Nope, this doesn't work :c
		//		this.allType.addAllSupertypes(Arrays.asList(scoreType,actionType,timeType,playerType));
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
			throw new GdlTransformationException("No roles present in this class!");
		} else {
			return ownRole;
		}
	}

	public FodotConstant convertRawRole(GdlConstant term){
		String rawName = term.getValue();
		FodotConstant toReturn = createConstant("p_" + rawName, this.getPlayerType());
		addTranslation(toReturn, term);
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

	private void addConstant(FodotConstant constant) {
		allType.addDomainElement(constant);
	}

	private boolean isConstantRegistered(FodotConstant constant) {
		return allType.containsDomainElement(constant);
	}

	public FodotConstant convertRawConstantName(GdlConstant constant) {
		return convertConstantName(constant, getAllType());
	}

	public FodotConstant convertConstantName(GdlConstant constant, FodotType type) {
		String rawName = constant.getValue();

		String constantName;
		if (rawName.matches("^[0-9]+$") && !type.isASubtypeOf(FodotType.INTEGER)) {
			constantName = "i" + rawName;
		} else {
			constantName = rawName;
		}

		FodotConstant toReturn = createConstant(constantName, type);
		addTranslation(toReturn, constant);
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

	public FodotPredicateDeclaration getDoPredicateDeclaration(){
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

	private Map<Pair<IFodotTerm, IFodotTerm>,Set<IFodotFormula>> scoreMap;

	public Map<Pair<IFodotTerm, IFodotTerm>,Set<IFodotFormula>> getScoreMap(){
		return new HashMap<>(scoreMap);
	}

	private void addScore(Pair<IFodotTerm, IFodotTerm> score, IFodotFormula condition) {
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

	public IFodotFile buildFodot() {
		FodotGameFactory factory = new FodotGameFactory(this,
				pool,
				getDoPredicateDeclaration(),
				terminalTimePredicateDeclaration);
		return factory.createFodot();
	}

	/**********************************************/

	@Override
	public void processRoleRelation(GdlRelation relation) {
		if(processingRules)
			throw new GdlParsingOrderException("A rule has already been processed," +
					"processing relations is not allowed anymore.");

		// Role: (role player)
		GdlConstant player = relation.getBody().get(0).toSentence().getName();
		this.addRole(convertRawRole(player));
	}

	@Override
	public void processInitRelation(GdlRelation relation) {
		if(processingRules)
			throw new GdlParsingOrderException("A rule has already been processed," +
					"processing relations is not allowed anymore.");

		// Init: (init (pred x1 .. xn))

		GdlSentence predicate = relation.getBody().get(0).toSentence();

		FodotPredicateDeclaration fodotPredicate = processPredicate(predicate);

		List<IFodotTypeEnumerationElement> initValues = extractEnumerationList(predicate);

		this.addInitialValue(fodotPredicate, new FodotPredicateEnumerationElement(initValues));
	}

	@Override
	public void processStaticPredicateRelation(GdlRelation relation) {
		if(processingRules)
			throw new GdlParsingOrderException("A rule has already been processed," +
					"processing relations is not allowed anymore.");
		
		// Static: (pred x1 .. xn)
		String originalPredName = relation.getName().getValue();
		String predName = NameUtil.convertToValidPredicateName(originalPredName);
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


		List<IFodotTypeEnumerationElement> staticValues = extractEnumerationList(relation);

		this.addStaticValue(pred, new FodotPredicateEnumerationElement(staticValues));

	}

	@Override
	public void processLegalRelation(GdlRelation relation) {
		//Do Nothing
	}
	
	
	private List<IFodotTypeEnumerationElement> extractEnumerationList(GdlSentence sentence) {

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this);
		List<IFodotTypeEnumerationElement> staticValues = new ArrayList<IFodotTypeEnumerationElement>();

		for (int i = 0; i < sentence.arity(); i++) {
			IFodotTerm term = sentenceTrans.generateTerm(sentence.get(i));
			if(term instanceof FodotConstant) {
				staticValues.add((FodotConstant) term);
				if(!isConstantRegistered((FodotConstant)term)) {
					this.addConstant((FodotConstant)term);
				}
			} else if (term instanceof FodotFunction) {
				FodotFunction func = (FodotFunction) term;
				staticValues.add(createTypeFunctionEnumerationElement((FodotTypeFunctionDeclaration) func.getDeclaration(), extractEnumerationList(sentence.get(i).toSentence())));				
			} else {
				throw new GdlTransformationException("Not a valid argument in a predicate");
			}
		}
		
		return staticValues;
	}

	@Override
	public void processNextRule(GdlRule rule) {
		if(!rule.getHead().getName().getValue().equals("next"))
			throw new IllegalArgumentException("Given rule is not a 'next' rule!");
		this.processingRules = true;

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this);

		//process (fluent) predicate in head
		GdlTerm nextGdlTerm = rule.getHead().get(0);
		if (nextGdlTerm instanceof GdlVariable) {
			IFodotTerm nextFodotTerm = sentenceTrans.generateTerm(nextGdlTerm);
			throw new IllegalStateException("You can't give a variable as argument for 'next', dummy!\n"+nextGdlTerm + " ===> " + nextFodotTerm);
		} else {

			GdlSentence predSentence = nextGdlTerm.toSentence();
			FodotPredicateDeclaration originalPredicateDecl = this.processPredicate(predSentence);
			FodotPredicateDeclaration causePredDecl = pool.getCauseOf(originalPredicateDecl);

			FodotPredicate causePred = sentenceTrans.generateTimedPredicate(predSentence, causePredDecl);

			//generate IFodotFormula from the body
			IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

			//add the combination as a next rule
			this.addNext(originalPredicateDecl, Pair.of(causePred, condition));
		}
	}

	@Override
	public void processLegalRule(GdlRule rule) {
		// legal(player, action) ==> do(time,player,action)

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this);

		IFodotTerm player;
		GdlTerm playerGdlTerm = rule.getHead().get(0);
		if (playerGdlTerm instanceof GdlConstant) {
			player = convertRawRole((GdlConstant) playerGdlTerm);
		} else {
			player = sentenceTrans.generateTerm(playerGdlTerm, getPlayerType());
		}

		GdlTerm actionGdlTerm = rule.getHead().get(1);
		IFodotTerm actionTerm = sentenceTrans.generateTerm(actionGdlTerm, getActionType());

		List<IFodotTerm> doArguments =
				Arrays.asList(
						sentenceTrans.createTimeVariable(),
						player,
						actionTerm
						);

		FodotPredicate doPred = 
				createPredicate(
						this.getDoPredicateDeclaration(),
						doArguments
						);

		//generate IFodotFormula from the body
		IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

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

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this);

		GdlTerm playerGdlTerm = rule.getHead().get(0);
		GdlTerm scoreGdlTerm = rule.getHead().get(1);

		IFodotTerm playerTerm;
		if (playerGdlTerm instanceof GdlVariable) {
			playerTerm = sentenceTrans.generateTerm(playerGdlTerm, getPlayerType());	
		} else if (playerGdlTerm instanceof GdlConstant) {
			playerTerm = convertRawRole((GdlConstant) playerGdlTerm);
		} else {
			throw new GdlTransformationException("Playerterm should probably only be constant or a variable.");
		}

		IFodotTerm scoreTerm = sentenceTrans.generateTerm(scoreGdlTerm, getScoreType());

		Pair<IFodotTerm, IFodotTerm> score = Pair.of(playerTerm, scoreTerm);

		IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

		if(!removeTimeVars(condition.getFreeVariables()).isEmpty()){
			condition = createExists(removeTimeVars(condition.getFreeVariables()),
					condition);
		}

		IFodotFormula extendedCondition =
				createImplies(
						createPredicate(
								terminalTimePredicateDeclaration,
								sentenceTrans.createTimeVariable()
								), condition
						);

		this.addScore(score,extendedCondition);
	}

	@Override
	public void processTerminalRule(GdlRule rule) {
		if(!rule.getHead().getName().getValue().equals("terminal"))
			throw new IllegalArgumentException("Rule is not a terminal rule!");
		this.processingRules = true;

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this);
		IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

		this.addTerminal(condition);

	}

	@Override
	public void processDefinitionRule(GdlRule rule) {
		this.processingRules = true;
		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this);

		//process (compound static) predicate in head
		GdlSentence predSentence = rule.getHead();
		FodotPredicateDeclaration originalPredicate = this.processCompoundStaticPredicate(predSentence);
		FodotPredicateDeclaration timedDeclaration = pool.getCompoundTimedVerionOf(originalPredicate);

		FodotPredicate compoundStaticPred = sentenceTrans.generateTimedPredicate(predSentence, timedDeclaration);

		//generate IFodotFormula from the body
		IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

		//add the combination as a next rule
		this.addCompound(originalPredicate, Pair.of(compoundStaticPred, condition));
	}

	//TODO: nakijken waar deze return niet gebruikt wordt
	public FodotPredicateDeclaration processPredicate(GdlSentence predSentence) {
		//This can still be used when rules are processed, but should not be used.

		//Predicate: (pred x1 .. xn)

		String originalPredName = predSentence.getName().getValue();
		String predName = NameUtil.convertToValidPredicateName(originalPredName);

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

		registerConstants(predSentence);

		return pred;
	}

	public FodotPredicateDeclaration processCompoundStaticPredicate(GdlSentence predSentence) {
		//Predicate: (pred x1 .. xn)

		String originalPredName = predSentence.getName().getValue();
		String predName = NameUtil.convertToValidPredicateName(originalPredName);

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

		registerConstants(predSentence);

		return predDecl;
	}

	/**
	 * Scan all body elements, if they are constants (ground in a non-built-in
	 * predicate is equivalent with being a constant), add them as a constant
	 * with the same type as its place in the predicate. Predicate should
	 * be registered by now.
	 * @param sentence
	 */
	private void registerConstants(GdlSentence sentence) {
		for (int i = 0; i < sentence.arity(); i++) {
			GdlTerm term = sentence.get(i);
			if(term.isGround()) {
				//Term is a constant, and only has a name, and arity 0
				GdlConstant constant = (GdlConstant) term;
				FodotConstant fodotConstant = convertRawConstantName(constant);
				if(!isConstantRegistered(fodotConstant))
					addConstant(fodotConstant);
			}
		}
	}

	Map<FodotConstant, GdlTerm> constantsMap = new HashMap<FodotConstant, GdlTerm> ();
	Map<FodotTypeFunctionDeclaration, GdlConstant> predicateTermMap = new HashMap<FodotTypeFunctionDeclaration, GdlConstant> ();
	//	Map<GdlTerm, IFodotTerm> translationsFromGdl = new HashMap<GdlTerm, IFodotTerm>();

	public void addTranslation(FodotConstant fodot, GdlTerm gdl) {
		constantsMap.put(fodot, gdl);
	}

	public void addTranslation(FodotTypeFunctionDeclaration fodot, GdlConstant name) {
		predicateTermMap.put(fodot, name);
	}

	@Override
	public GdlTerm translate(IFodotTypeEnumerationElement fodot) {
		if (fodot instanceof FodotConstant) {
			FodotConstant casted = (FodotConstant) fodot;
			if (constantsMap.containsKey(casted)) {
				return constantsMap.get(casted);
			}
		}
		if (fodot instanceof FodoTypeFunctionEnumerationElement) {
			FodoTypeFunctionEnumerationElement casted = (FodoTypeFunctionEnumerationElement) fodot;
			FodotTypeFunctionDeclaration decl = casted.getDeclaration();
			if (predicateTermMap.containsKey(decl)) {
				GdlConstant name = predicateTermMap.get(decl);
				List<GdlTerm> body = new ArrayList<GdlTerm>();
				for (IFodotTypeEnumerationElement el : casted.getElements()) {
					body.add(translate(el));
				}
				return GdlPool.getRelation(name, body).toTerm();
			}
		}
		return null;
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
