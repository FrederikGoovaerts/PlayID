package fodot.gdl_parser.second_phase;

import static fodot.objects.FodotElementBuilder.createAnd;
import static fodot.objects.FodotElementBuilder.createExists;
import static fodot.objects.FodotElementBuilder.createPredicate;
import static fodot.objects.FodotElementBuilder.createPredicateDeclaration;
import static fodot.objects.FodotElementBuilder.createTypeFunctionEnumerationElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.Pair;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.exceptions.gdl.GdlParsingOrderException;
import fodot.exceptions.gdl.GdlTransformationException;
import fodot.gdl_parser.FodotGameFactory;
import fodot.gdl_parser.GdlTransformer;
import fodot.gdl_parser.GdlVocabulary;
import fodot.gdl_parser.first_phase.data.declarations.GdlPredicateDeclaration;
import fodot.gdl_parser.second_phase.data.FodotCompoundData;
import fodot.gdl_parser.second_phase.data.FodotNextData;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.elements.predicateenum.elements.FodotPredicateEnumerationElement;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typeenum.elements.FodotInteger;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
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
import fodot.util.IntegerTypeUtil;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * For correct utilization of this class, non-GdlRules should be processed before
 * GdlRules. If this is not respected, a GdlParsingOrderException will be thrown.
 */
public class GdlFodotTransformer implements GdlTransformer {

	public static final String ACTION_PREDICATE_NAME = "do";
	public static final String LEGAL_MOVE_PREDICATE_NAME = "legal";

	/***************************************************************************
	 * Constructor
	 * @param gdlVocabulary 	The data created by the first pass through the GDL file
	 **************************************************************************/

	public GdlFodotTransformer(GdlVocabulary gdlVocabulary){
		this.gdlVocabulary = gdlVocabulary;
		this.cleanAndInitializeBuilder();
	}

	/***************************************************************************
	 * Class Properties
	 **************************************************************************/

	/**********************************************
	 *  GDL Vocabulary
	 ***********************************************/
	private GdlVocabulary gdlVocabulary;

	public GdlVocabulary getGdlVocabulary() {
		return gdlVocabulary;
	}

	/**********************************************/


	/*************************************
	 * Processing state
	 */

	/* This boolean is false when no GdlRules have been read in yet */
	private boolean processingRules;

	/*** End of Processing state subsection ***/

	/*************************************
	 * Default Types
	 */

	public FodotType getTimeType(){
		return getGdlVocabulary().getTimeType();
	}

	public FodotType getPlayerType() {
		return getGdlVocabulary().getPlayerType();
	}

	public FodotType getActionType() {
		return getGdlVocabulary().getActionType();
	}

	public FodotType getScoreType() {
		return getGdlVocabulary().getScoreType();
	}


	/*** End of Default Types subsection ***/

	/**********************************************
	 *  get GDL vocabulary info
	 ***********************************************/


	/**********************************************/

	public FodotPredicateDeclaration getPredicateDeclaration(GdlRelation relation) {
		return getPredicate(new GdlPredicateDeclaration(relation));
	}

	public FodotPredicateDeclaration getPredicate(GdlPredicateDeclaration gdlPredicateDeclaration) {
		return getGdlVocabulary().getPredicateDeclaration(gdlPredicateDeclaration);
	}

	public FodotTypeFunctionDeclaration getFunctionDeclaration(GdlFunction function) {
		return getGdlVocabulary().getFunctionDeclaration(function);
	}


	/*************************************
	 * Roles
	 */

	private FodotConstant ownRole;

	private void addRole(FodotConstant role){
		if(role == null)
			throw new IllegalArgumentException();
		if(ownRole == null)
			ownRole = role;
		// Should already be done in the first phase
		//getPlayerType().addDomainElement(role);
	}

	public FodotConstant getOwnRole() {
		if (ownRole == null) {
			throw new GdlTransformationException("No roles present in this class!");
		} else {
			return ownRole;
		}
	}

	public FodotConstant convertRole(GdlConstant term){
		return getGdlVocabulary().getConstant(term, getPlayerType());
	}

	/*** End of Roles subsection ***/

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

	private FodotPredicateDeclaration legalmovePredicateDeclaration;

	public FodotPredicateDeclaration getLegalmovePredicateDeclaration(){
		return this.legalmovePredicateDeclaration;
	}


	/*** End of Actions subsection ***/

	/*************************************
	 * Next rules
	 */

	private Map<FodotPredicateDeclaration,Set<FodotNextData>> nextMap;

	public Map<FodotPredicateDeclaration,Set<FodotNextData>> getNextMap() {
		return new HashMap<>(nextMap);
	}

	private void addNext(FodotPredicateDeclaration predicate, FodotNextData data) {
		if (!nextMap.containsKey(predicate)){
			nextMap.put(predicate, new HashSet<FodotNextData>());
		}
		nextMap.get(predicate).add(data);
	}

	/*** End of Next rules subsection ***/

	/*************************************
	 * Legal
	 */

	//Contains legal rules, actions which are only legal in certain circumstances
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

	//Contains actions which are always legal
	private Set<FodotPredicate> legalSet;

	public Set<FodotPredicate> getLegalSet(){
		return new HashSet<>(this.legalSet);
	}

	private void addLegal(FodotPredicate predicate){
		legalSet.add(predicate);
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
		if(!scoreMap.containsKey(score)){
			Set<IFodotFormula> newSet = new HashSet<>();
			scoreMap.put(score,newSet);
		}
		scoreMap.get(score).add(condition);
	}

	private FodotInteger maximumPossibleScore;

	public FodotInteger getMaximumScore() {
		if (maximumPossibleScore == null) {
			maximumPossibleScore = IntegerTypeUtil.getMaximum(
					IntegerTypeUtil.getIntegers(getScoreType().getDomainElements()));
		}
		return maximumPossibleScore;
	}

	/*** End of Score subsection ***/

	/*************************************
	 * Compound Static Predicates
	 */
	private Map<FodotPredicateDeclaration, Set<FodotCompoundData>> compoundMap;

	public Map<FodotPredicateDeclaration, Set<FodotCompoundData>> getCompoundMap() {
		return new HashMap<>(compoundMap);
	}

	private void addCompound(
			FodotPredicateDeclaration predicate,
			FodotCompoundData data) {
		if(!compoundMap.containsKey(predicate)){
			compoundMap.put(predicate,new HashSet<FodotCompoundData>());
		}
		compoundMap.get(predicate).add(data);
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
		this.legalSet = new HashSet<>();
		this.terminalSet = new HashSet<>();
		this.compoundMap = new HashMap<>();
		this.processingRules = false;

		List<FodotType> typeList = new ArrayList<>();
		typeList.add(getTimeType());
		typeList.add(getPlayerType());
		typeList.add(getActionType());
		this.doPredicateDeclaration = createPredicateDeclaration(ACTION_PREDICATE_NAME, typeList);
		this.legalmovePredicateDeclaration = createPredicateDeclaration(LEGAL_MOVE_PREDICATE_NAME,typeList);

		ArrayList<FodotType> typeList2 = new ArrayList<>();
		typeList2.add(getTimeType());
		this.terminalTimePredicateDeclaration = createPredicateDeclaration("terminalTime", typeList2);

	}

	public IFodotFile buildFodot() {
		FodotGameFactory factory = new FodotGameFactory(this,
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
		this.addRole(convertRole(player));
	}

	@Override
	public void processInitRelation(GdlRelation relation) {
		if(processingRules)
			throw new GdlParsingOrderException("A rule has already been processed," +
					"processing relations is not allowed anymore.");

		// Init: (init (pred x1 .. xn))

		GdlSentence predicate = relation.getBody().get(0).toSentence();

		FodotPredicateDeclaration fodotPredicate = processPredicate(predicate);

		List<IFodotTypeEnumerationElement> initValues =
				extractEnumerationList(predicate, FormulaUtil.removeTypes(fodotPredicate.getArgumentTypes(), getTimeType()));

		this.addInitialValue(fodotPredicate, new FodotPredicateEnumerationElement(fodotPredicate, initValues));
	}

	@Override
	public void processStaticPredicateRelation(GdlRelation relation) {
		if(processingRules)
			throw new GdlParsingOrderException("A rule has already been processed," +
					"processing relations is not allowed anymore.");

		// Static: (pred x1 .. xn)

		FodotPredicateDeclaration pred = getGdlVocabulary().getPredicateDeclaration(relation);

		List<IFodotTypeEnumerationElement> staticValues =
				extractEnumerationList(relation, FormulaUtil.removeTypes(pred.getArgumentTypes(), getTimeType()));

		this.addStaticValue(pred, new FodotPredicateEnumerationElement(pred, staticValues));

	}

	@Override
	public void processLegalRelation(GdlRelation relation) {
		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this);

		GdlTerm playerGdlTerm = relation.get(0);
		IFodotTerm player = sentenceTrans.generateTerm(playerGdlTerm, getPlayerType());

		GdlTerm actionGdlTerm = relation.get(1);
		IFodotTerm actionTerm = sentenceTrans.generateTerm(actionGdlTerm, getActionType());

		this.addLegal(
				createPredicate(
						this.getLegalmovePredicateDeclaration(),
						sentenceTrans.createTimeVariable(),
						player,
						actionTerm
						)
				);
	}


	private List<IFodotTypeEnumerationElement> extractEnumerationList(GdlSentence sentence, List<FodotType> types) {

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this);
		List<IFodotTypeEnumerationElement> staticValues = new ArrayList<IFodotTypeEnumerationElement>();

		for (int i = 0; i < sentence.arity(); i++) {

			IFodotTerm term = sentenceTrans.generateTerm(sentence.get(i), types.get(i));
			if (term instanceof FodotConstant) {
				staticValues.add(((FodotConstant) term).toEnumerationElement());
			} else if (term instanceof FodotFunction) {
				FodotFunction func = (FodotFunction) term;
				staticValues.add(
						createTypeFunctionEnumerationElement(
								(FodotTypeFunctionDeclaration) func.getDeclaration(),
								extractEnumerationList(sentence.get(i).toSentence(), 
										FormulaUtil.removeTypes(func.getDeclaration().getArgumentTypes(), getTimeType())) ));				
			} else {
				throw new GdlTransformationException("Not a valid argument in a predicate: " + term
						+ " [" + term.getClass().getSimpleName() + "]");
			}
		}

		return staticValues;
	}

	@Override
	public void processNextRule(GdlRule rule) {
		if(!rule.getHead().getName().getValue().equals("next"))
			throw new IllegalArgumentException("Given rule is not a 'next' rule!");
		this.processingRules = true;


		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this, getGdlVocabulary().getVariables(rule));

		//process (fluent) predicate in head
		GdlTerm nextGdlTerm = rule.getHead().get(0);
		if (nextGdlTerm instanceof GdlVariable) {
			IFodotTerm nextFodotTerm = sentenceTrans.generateTerm(nextGdlTerm, null);
			throw new IllegalStateException("You can't give a variable as argument for 'next'!\n"+nextGdlTerm + " ===> " + nextFodotTerm);
		} else {

			GdlSentence predSentence = nextGdlTerm.toSentence();
			FodotPredicateDeclaration originalPredicateDecl = this.processPredicate(predSentence);
			FodotPredicateDeclaration causePredDecl = getCauseOf(originalPredicateDecl);
			//FodotPredicateDeclaration causePredDecl = null;

			FodotPredicate causePred = sentenceTrans.generateTimedPredicate(predSentence, causePredDecl);

			//generate IFodotFormula from the body
			IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

			//add the combination as a next rule
			this.addNext(originalPredicateDecl, new FodotNextData(causePred, condition));
		}
	}



	@Override
	public void processLegalRule(GdlRule rule) {
		// legal(player, action) ==> do(time,player,action)

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this, getGdlVocabulary().getVariables(rule));

		GdlTerm playerGdlTerm = rule.getHead().get(0);
		IFodotTerm player = sentenceTrans.generateTerm(playerGdlTerm, getPlayerType());

		GdlTerm actionGdlTerm = rule.getHead().get(1);
		IFodotTerm actionTerm = sentenceTrans.generateTerm(actionGdlTerm, getActionType());

		List<IFodotTerm> legalArguments =
				Arrays.asList(
						sentenceTrans.createTimeVariable(),
						player,
						actionTerm
						);
		FodotPredicate legalmovePred =
				createPredicate(
						this.getLegalmovePredicateDeclaration(),
						legalArguments
						);

		//generate IFodotFormula from the body
		IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

		if (condition != null) {
			Set<FodotVariable> conditionExclusiveVariables = removeTimeVars(condition.getFreeVariables());
			conditionExclusiveVariables.removeAll(legalmovePred.getFreeVariables());

			if(!conditionExclusiveVariables.isEmpty()){
				condition = createExists(conditionExclusiveVariables, condition);
			}

			//add the combination as a next rule
		} 

		this.addLegal(legalmovePred, condition);
	}

	@Override
	public void processGoalRule(GdlRule rule) {
		if(!rule.getHead().getName().getValue().equals("goal"))
			throw new IllegalArgumentException("Rule is not a goal rule!");
		this.processingRules = true;

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this, getGdlVocabulary().getVariables(rule));

		GdlTerm playerGdlTerm = rule.getHead().get(0);
		GdlTerm scoreGdlTerm = rule.getHead().get(1);

		IFodotTerm playerTerm;
		playerTerm = sentenceTrans.generateTerm(playerGdlTerm, getPlayerType());	

		IFodotTerm scoreTerm = sentenceTrans.generateTerm(scoreGdlTerm, getScoreType());

		Pair<IFodotTerm, IFodotTerm> score = Pair.of(playerTerm, scoreTerm);

		IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

		Set<FodotVariable> conditionExclusiveVariables = removeTimeVars(condition.getFreeVariables());
		conditionExclusiveVariables.remove(playerTerm);
		conditionExclusiveVariables.remove(scoreTerm);

		if(!conditionExclusiveVariables.isEmpty()){
			condition = createExists(conditionExclusiveVariables, condition);
		}

		IFodotFormula extendedCondition;
		//Empty: only contains empty formula connectors
		if (condition.getAllInnerElementsOfClass(IFodotFormula.class).size() <= 1) {
			extendedCondition = null;
		} else {

			FodotVariable timeVar = sentenceTrans.createTimeVariable();
			extendedCondition =
					createExists(timeVar,
							createAnd(
									createPredicate(
											terminalTimePredicateDeclaration,
											timeVar
											), condition
									)
							);
		}

		this.addScore(score,extendedCondition);
	}

	@Override
	public void processTerminalRule(GdlRule rule) {
		if(!rule.getHead().getName().getValue().equals("terminal"))
			throw new IllegalArgumentException("Rule is not a terminal rule!");
		this.processingRules = true;

		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this, getGdlVocabulary().getVariables(rule));
		IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

		this.addTerminal(condition);

	}

	@Override
	public void processDefinitionRule(GdlRule rule) {
		this.processingRules = true;
		GdlFodotSentenceTransformer sentenceTrans = new GdlFodotSentenceTransformer(this, getGdlVocabulary().getVariables(rule));

		//process (compound static) predicate in head
		GdlSentence predSentence = rule.getHead();
		//		FodotPredicateDeclaration originalPredicate = this.processCompoundStaticPredicate(predSentence);
		GdlPredicateDeclaration gdlDeclaration =
				new GdlPredicateDeclaration(predSentence.getName(), predSentence.arity());
		FodotPredicateDeclaration fodotDeclaration =
				getGdlVocabulary().getPredicateDeclaration(gdlDeclaration);

		FodotPredicate compoundStaticPred = sentenceTrans.generateRelation(GdlPool.getRelation(predSentence.getName(), predSentence.getBody()));
		//generate IFodotFormula from the body
		IFodotFormula condition = sentenceTrans.generateFodotFormulaFrom(rule.getBody());

		//add the combination as a next rule
		this.addCompound(fodotDeclaration, new FodotCompoundData(compoundStaticPred, condition));
	}

	//TODO: nakijken waar deze return niet gebruikt wordt
	public FodotPredicateDeclaration processPredicate(GdlSentence predSentence) {
		//This can still be used when rules are processed, but should not be used.

		//Predicate: (pred x1 .. xn)




		FodotPredicateDeclaration pred = getGdlVocabulary().getPredicateDeclaration(
				new GdlPredicateDeclaration(predSentence.getName(), predSentence.arity()));

		return pred;
	}


	/***************************************************************************
	 * Helper methods
	 **************************************************************************/

	public Set<FodotVariable> removeTimeVars(Set<FodotVariable> vars){
		Set<FodotVariable> toReturn = new HashSet<>();
		for (FodotVariable var : vars) {
			if(!var.getType().equals(getTimeType()))
				toReturn.add(var);
		}
		return toReturn;
	}

	public FodotPredicateDeclaration getCauseOf(FodotPredicateDeclaration originalPredicateDecl) {
		if(originalPredicateDecl.getAmountOfArgumentTypes()==0 ||
				!originalPredicateDecl.getArgumentType(0).equals(gdlVocabulary.getTimeType()))
			throw new IllegalArgumentException("This is not an LTC predicate");
		return createPredicateDeclaration("C_" + originalPredicateDecl.getName(), originalPredicateDecl.getArgumentTypes());
	}

	public FodotPredicateDeclaration getCauseNotOf(FodotPredicateDeclaration originalPredicateDecl) {
		if(originalPredicateDecl.getAmountOfArgumentTypes()==0 ||
				!originalPredicateDecl.getArgumentType(0).equals(gdlVocabulary.getTimeType()))
			throw new IllegalArgumentException("This is not an LTC predicate");
		return createPredicateDeclaration("Cn_" + originalPredicateDecl.getName(), originalPredicateDecl.getArgumentTypes());
	}

	public FodotPredicateDeclaration getInitialOf(FodotPredicateDeclaration originalPredicateDecl) {
		if(originalPredicateDecl.getAmountOfArgumentTypes()==0 ||
				!originalPredicateDecl.getArgumentType(0).equals(gdlVocabulary.getTimeType()))
			throw new IllegalArgumentException("This is not an LTC predicate");
		return createPredicateDeclaration("I_" + originalPredicateDecl.getName(),
				originalPredicateDecl.getArgumentTypes().subList(1,originalPredicateDecl.getAmountOfArgumentTypes()));
	}

}
