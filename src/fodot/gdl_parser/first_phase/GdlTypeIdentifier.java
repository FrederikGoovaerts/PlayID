package fodot.gdl_parser.first_phase;

import static fodot.objects.FodotElementBuilder.createConstant;
import static fodot.objects.FodotElementBuilder.createPredicateDeclaration;
import static fodot.objects.FodotElementBuilder.createType;
import static fodot.objects.FodotElementBuilder.createTypeFunctionDeclaration;
import static fodot.objects.FodotElementBuilder.createVariable;
import static fodot.objects.FodotElementBuilder.getNaturalNumberType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.exceptions.gdl.GdlTypeIdentificationError;
import fodot.gdl_parser.GdlTransformer;
import fodot.gdl_parser.GdlVocabulary;
import fodot.gdl_parser.first_phase.data.GdlArgumentListData;
import fodot.gdl_parser.first_phase.data.GdlTermData;
import fodot.gdl_parser.first_phase.data.declarations.GdlConstantDeclaration;
import fodot.gdl_parser.first_phase.data.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.first_phase.data.declarations.GdlPredicateDeclaration;
import fodot.gdl_parser.first_phase.data.declarations.GdlVariableDeclaration;
import fodot.gdl_parser.first_phase.data.declarations.IGdlArgumentListDeclaration;
import fodot.gdl_parser.first_phase.data.declarations.IGdlTermDeclaration;
import fodot.gdl_parser.first_phase.data.occurrences.GdlTermOccurrence;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.patterns.gdl_vocabulary.GdlVocabularyChainOptimizer;
import fodot.patterns.gdl_vocabulary.IGdlVocabularyOptimizer;
import fodot.util.FormulaUtil;
import fodot.util.NameUtil;

public class GdlTypeIdentifier {

	private static final IGdlVocabularyOptimizer DEFAULT_OPTIMIZER = new GdlVocabularyChainOptimizer();
	private IGdlVocabularyOptimizer optimizer = DEFAULT_OPTIMIZER;
	
	/**********************************************
	 *  Data maps
	 ***********************************************/
	private Map<IGdlTermDeclaration, GdlTermData> terms = new HashMap<>();
	private Map<IGdlArgumentListDeclaration, GdlArgumentListData> argumentLists = new HashMap<>();

	private Set<GdlPredicateDeclaration> dynamicPredicates = new HashSet<>();
	private Set<GdlRule> timeDependentRules = new HashSet<>();
	/**********************************************/


	/***********************************************
	 *  Default types
	 ***********************************************/
	private FodotType unfilledType = createType("Unfilled");
	private FodotType timeType = createType("Time", getNaturalNumberType());
	private FodotType playerType = createType("Player");
	private FodotType actionType = createType("Action");
	private FodotType scoreType = createType("ScoreType", getNaturalNumberType());
	private FodotType allType = createType("All");
	/**********************************************/


	/**********************************************
	 *  Default predicates
	 ***********************************************/
	private GdlPredicateDeclaration distinctPred =	new GdlPredicateDeclaration ( GdlPool.getConstant("distinct"), 2);
	private GdlPredicateDeclaration doesPred = 		new GdlPredicateDeclaration ( GdlPool.getConstant("does"), 2);
	private GdlPredicateDeclaration goalPred =		new GdlPredicateDeclaration ( GdlPool.getConstant("goal"), 2);
	private GdlPredicateDeclaration initPred = 		new GdlPredicateDeclaration ( GdlPool.getConstant("init"), 1);
	private GdlPredicateDeclaration legalPred =		new GdlPredicateDeclaration ( GdlPool.getConstant("legal"), 2);
	private GdlPredicateDeclaration nextPred = 		new GdlPredicateDeclaration ( GdlPool.getConstant("next"), 1);
	private GdlPredicateDeclaration rolePred =		new GdlPredicateDeclaration ( GdlPool.getConstant("role"), 1);
	private GdlPredicateDeclaration terminalPred = 	new GdlPredicateDeclaration ( GdlPool.getConstant("terminal"), 0);
	private GdlPredicateDeclaration truePred = 		new GdlPredicateDeclaration ( GdlPool.getConstant("true"), 1);

	private List<GdlPredicateDeclaration> defaultPredicates;
	private List<GdlPredicateDeclaration> unfilledDefaultPredicates;

	private void initDefaultPredicates() {
		initDefaultPredicate(distinctPred,	Arrays.asList(unfilledType, unfilledType),	true);
		initDefaultPredicate(doesPred,		Arrays.asList(playerType, actionType),		true);
		initDefaultPredicate(goalPred,		Arrays.asList(playerType, scoreType),		true);
		initDefaultPredicate(initPred,		Arrays.asList(unfilledType),				true);
		initDefaultPredicate(legalPred,		Arrays.asList(playerType, actionType),		true);
		initDefaultPredicate(nextPred,		Arrays.asList(unfilledType),				true);
		initDefaultPredicate(rolePred,		Arrays.asList(playerType),					true);
		initDefaultPredicate(terminalPred,	new ArrayList<FodotType>(),					true);
		initDefaultPredicate(truePred,		Arrays.asList(unfilledType),				true);

		registerDynamicPredicate(doesPred);
		registerDynamicPredicate(legalPred);
		registerDynamicPredicate(terminalPred);
		registerDynamicPredicate(goalPred);

		defaultPredicates = Arrays.asList(distinctPred, doesPred, goalPred, initPred, legalPred, nextPred, rolePred, terminalPred, truePred);
		unfilledDefaultPredicates = Arrays.asList(distinctPred, initPred, nextPred, truePred);
	}

	//GETTERS
	public GdlPredicateDeclaration getDistinct() {
		return distinctPred;
	}
	public GdlPredicateDeclaration getDoes() {
		return doesPred;
	}
	public GdlPredicateDeclaration getGoal() {
		return goalPred;
	}
	public GdlPredicateDeclaration getInit() {
		return initPred;
	}
	public GdlPredicateDeclaration getLegal() {
		return legalPred;
	}
	public GdlPredicateDeclaration getNext() {
		return nextPred;
	}
	public GdlPredicateDeclaration getRole() {
		return rolePred;
	}
	public GdlPredicateDeclaration getTerminal() {
		return terminalPred;
	}
	public GdlPredicateDeclaration getTrue() {
		return truePred;
	}

	public List<GdlPredicateDeclaration> getDefaultPredicates() {
		return defaultPredicates;
	}

	public List<GdlPredicateDeclaration> getUnfilledDefaultPredicates() {
		return unfilledDefaultPredicates;
	}
	/**********************************************/


	/**********************************************
	 *  Constructor
	 ***********************************************/
	public GdlTypeIdentifier() {
		initDefaultPredicates();
	}
	/**********************************************/


	/**********************************************
	 *  Initialize entries
	 ***********************************************/
	private void initTerm(IGdlTermDeclaration declaration) {
		terms.put(declaration, new GdlTermData(unfilledType));
	}

	private void initArgumentList(IGdlArgumentListDeclaration declaration) {
		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilledType, declaration.getArity() );
		argumentLists.put(declaration, new GdlArgumentListData(argumentTypes));
	}
	private void initDefaultPredicate(GdlPredicateDeclaration predicate, List<FodotType> argArgumentTypes, boolean lockedTypes) {
		initArgumentList(predicate);

		for (int i = 0; i < argArgumentTypes.size(); i++) {
			if (!argArgumentTypes.get(i).equals(unfilledType)) {
				updateArgumentListArgumentType(predicate, i, argArgumentTypes.get(i));
			}
		}

		if (lockedTypes) {
			argumentLists.get(predicate).lockTypes();
		}
	}
	/**********************************************/



	/**********************************************
	 *  Adding occurrences of elements.
	 *  This should be done by a visitor.
	 ***********************************************/
	public void addConstantOccurrence(IGdlArgumentListDeclaration directParent, int argumentIndex, GdlConstant constant) {

		//Don't register constants of type scoreType
		//		if (argumentLists.get(directParent).getArgumentType(argumentIndex).equals(scoreType)) {
		//			return;
		//		}
		GdlConstantDeclaration decl = new GdlConstantDeclaration(constant);
		addTermOccurrence(decl, directParent, argumentIndex);
		terms.get(decl).allowMultipleTypes();
	}


	public void addVariableOccurrence(GdlRule parentRule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlVariable argVariable) {
		GdlVariableDeclaration decl = new GdlVariableDeclaration(argVariable, parentRule);
		addTermOccurrence(decl, directParent, argumentIndex);

	}

	public void addFunctionOccurrence(GdlRule parentRule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlFunction function) {
		GdlFunctionDeclaration decl = new GdlFunctionDeclaration( function );
		addArgumentListOccurrence(decl);
		addTermOccurrence(decl, directParent, argumentIndex);
		terms.get(decl).allowMultipleTypes();
	}

	public void addPredicateOccurrence(GdlRule parentRule, GdlRelation predicate) {
		GdlPredicateDeclaration declaration = new GdlPredicateDeclaration( predicate );
		addArgumentListOccurrence(declaration);
	}

	private void addTermOccurrence(IGdlTermDeclaration decl, IGdlArgumentListDeclaration directParent, int argumentIndex) {		
		//Initialize list if first occurrence of constant
		if (terms.get(decl) == null) {
			initTerm(decl);
		}		

		//Add occurrences
		terms.get(decl).addOccurence( new GdlTermOccurrence(directParent, argumentIndex) );
		argumentLists.get(directParent).addArgumentOccurrence(argumentIndex, decl);

		if (!terms.get(decl).hasOnlyType(unfilledType)) {
			if (canPushUpdatesTo(decl, directParent, argumentIndex)) {
				FodotType termType = terms.get(decl).getTypes().get(0);
				updateArgumentListArgumentType(directParent, argumentIndex, termType);
			}
		}


		//Edit typing if necessary
		FodotType foundType = argumentLists.get(directParent).getArgumentType(argumentIndex);
		if (!foundType.equals(unfilledType)) {
			//Don't add constants to the score type.
			updateTermType(decl, foundType);
		}
	}

	private void addArgumentListOccurrence(IGdlArgumentListDeclaration declaration) {
		if (!argumentLists.containsKey(declaration)) {
			initArgumentList(declaration);
		}		
	}

	
	private boolean canPushTypeUpdates(IGdlTermDeclaration term) {
		GdlTermData data = terms.get(term);
		return !data.hasOnlyType(unfilledType)
				&& !data.hasMultipleTypes()
				&& !(term instanceof GdlConstantDeclaration
						&& ((GdlConstantDeclaration)term).getConstant().getValue().matches(NameUtil.NUMBER_REGEX));
	}

	private boolean canPushUpdatesTo(IGdlTermDeclaration term, IGdlArgumentListDeclaration argumentList, int argIndex) {
		FodotType argumentType = argumentLists.get(argumentList).getArgumentType(argIndex);
		if (terms.get(term).getTypes().equals(argumentType)) {
			return false;
		}
		return canPushTypeUpdates(term);
	}

	/**********************************************
	 *  Time dependent rules
	 ***********************************************/
	public void registerTimeDependent(GdlRule rule) {
		timeDependentRules.add(rule);
	}

	public boolean isTimeDependentRule(GdlRule rule) {
		return timeDependentRules.contains(rule);
	}
	/**********************************************/


	/**********************************************
	 *  Dynamic predicates
	 ***********************************************/
	public void registerDynamicPredicate(GdlRelation predicate) {
		registerDynamicPredicate(new GdlPredicateDeclaration(predicate));
	}

	public void registerDynamicPredicate(GdlPredicateDeclaration predicate) {
		if (!argumentLists.containsKey(predicate)) {
			initArgumentList(predicate);
		}
		dynamicPredicates.add(predicate);		
	}

	public boolean isDynamicPredicate(GdlRelation predicate) {
		return isDynamicPredicate(new GdlPredicateDeclaration(predicate));
	}

	public boolean isDynamicPredicate(GdlPredicateDeclaration predicate) {
		return dynamicPredicates.contains(predicate);
	}
	/**********************************************/

	/**********************************************/


	/**********************************************
	 *  Updating the typings.
	 *  This should not be accessed outside this class:
	 *  Everything can be deducted by the occurrences.
	 ***********************************************/

	/**********************************************
	 *  Intrinsic Types
	 ***********************************************/

	/**
	 * Helper method for updating the type of GDL terms.
	 * It also updates the type of arguments when knowing the type of constants, variables and functions
	 */
	private void updateTermType(IGdlTermDeclaration declaration, FodotType foundType) {
		assert !foundType.equals(unfilledType);

		//		if (!terms.containsKey(declaration)) {
		//			initTerm(declaration);
		//		}

		GdlTermData data = terms.get(declaration);

		//What if it already has a type?
		if (!data.canHaveMultipleTypes() && !data.hasOnlyType(unfilledType) && !data.hasOnlyType(foundType)) {
			throw new GdlTypeIdentificationError("Type collision in "+ declaration +" \nOld: " + data.getTypes() + "\nNew: " + foundType);
		}

		//		if (declaration instanceof GdlConstantDeclaration && foundType.equals(scoreType)) {
		//			System.out.println("<:score constant found:>");
		//			return;
		//		}
		//Set the new type
		data.removeType(unfilledType);
		data.addType(foundType);

		//Update all occurrences
		for (GdlTermOccurrence occ : data.getOccurences()) {
			IGdlArgumentListDeclaration parent = occ.getDirectParent();
			if (canPushUpdatesTo(declaration, parent, occ.getArgumentIndex())) {
				//				System.out.println(":T) "+declaration + " ==> " + parent + (occ.getArgumentIndex()+1) + "/" + parent.getArity() + " :: " + foundType);
				updateArgumentListArgumentType( parent, occ.getArgumentIndex(), foundType);		
			}
		}	
	}
	/**********************************************/


	/**********************************************
	 *  Argument types
	 ***********************************************/
	/**
	 * Helper method for function&predicate argument types
	 */
	private void updateArgumentListArgumentType(IGdlArgumentListDeclaration declaration, int argumentNr, FodotType foundType) {
		assert !foundType.equals(unfilledType);

		//Check if exists
		//		if (!argumentLists.containsKey(declaration)) {
		//			initArgumentList(declaration);
		//		}

		GdlArgumentListData data = argumentLists.get(declaration);

		//Don't update typelocked things
		if (data.isTypeLocked()) {
			return;
		}

		//What if it already has a type?
		if (!data.getArgumentType(argumentNr).equals(unfilledType) && !data.getArgumentType(argumentNr).equals(foundType)) {
			throw new GdlTypeIdentificationError("Type collision in "+ declaration  +" \nOld: " + data.getArgumentType(argumentNr) + "\nNew: " + foundType);
		}

		//Set typing
		data.setArgumentType(argumentNr, foundType);

		//Update all occurred arguments
		for (IGdlTermDeclaration term : data.getArgumentOccurrences(argumentNr)) {
			if (!terms.get(term).hasType(foundType)) {
				//System.out.println(":A> "+declaration + (argumentNr+1) + "/" + declaration.getArity() + " ==> " + term + " :: " + foundType);
				updateTermType(term, foundType);
			}
		}

	}
	/**********************************************/

	/**********************************************/


	/**********************************************
	 *  Resulting GdlFodotData generator
	 ***********************************************/
	private void addTimeVariableToDynamicPredicates() {
		//Add time variable to all dynamic predicates
		for (GdlPredicateDeclaration predicate : dynamicPredicates) {
			GdlArgumentListData data = argumentLists.get(predicate);
			data.addArgumentType(0, timeType);
		}
	}

	private void fillMissingTypes() {
		//Argument list elements updates
		for (IGdlArgumentListDeclaration argumentList : argumentLists.keySet()) {
			if (!getDefaultPredicates().contains(argumentList)) {
				GdlArgumentListData data = argumentLists.get(argumentList);
				for (int i = 0; i < data.getAmountOfArguments(); i++) {
					if (data.getArgumentType(i).equals(unfilledType)) {
						updateArgumentListArgumentType(argumentList, i, getNewFillType());
					}				
				}
			}
		}
		
		
	}

	private static final boolean useAllType = false;
	private static final String OTHER_TYPE_NAME = "Type";
	private int otherTypesIndex = 1;
	private Collection<FodotType> otherTypes = useAllType ? Arrays.asList(allType) :  new ArrayList<FodotType>();

	private FodotType getNewFillType() {
		if (useAllType) {
			return allType;
		}

		FodotType fillType = new FodotType(OTHER_TYPE_NAME + (otherTypesIndex++));
		otherTypes.add(fillType);
		return fillType;
	}

	public GdlVocabulary generateTranslationData() {

		fillMissingTypes();
		addTimeVariableToDynamicPredicates();

		Map<GdlConstant, Map<FodotType, FodotConstant>> constantsMap = new HashMap<>();
		Map<GdlFunctionDeclaration, FodotTypeFunctionDeclaration> functionDeclarations = new HashMap<>();
		Map<GdlPredicateDeclaration, FodotPredicateDeclaration> predicateDeclarations = new HashMap<>();
		Map<GdlRule, Map<GdlVariable, FodotVariable>> variablesPerRule = new HashMap<>();

		//GENERATE SETS FOR GDLFODOTDATA
		for (IGdlTermDeclaration term : terms.keySet()) {
			GdlTermData data = terms.get(term);
			if (term instanceof GdlConstantDeclaration) {
				GdlConstantDeclaration cc = (GdlConstantDeclaration) term;

				String constantValue = cc.getConstant().getValue();

				for (FodotType type : terms.get(cc).getTypes()) {

					FodotConstant fc = createConstant( NameUtil.convertToValidConstantName(constantValue, type) , type);

					GdlConstant constant = cc.getConstant();

					if (constantsMap.get(constant) == null) {
						constantsMap.put(constant, new HashMap<FodotType, FodotConstant>());
					}

					constantsMap.get(constant).put(type, fc);


				}
			} else if (term instanceof GdlVariableDeclaration) {
				GdlVariableDeclaration var = (GdlVariableDeclaration) term;
				GdlRule location = var.getLocation();
				if (!variablesPerRule.containsKey(var.getLocation())) {
					variablesPerRule.put(location, new HashMap<GdlVariable,FodotVariable>());
				}
				Set<FodotVariable> usedVariables = new HashSet<FodotVariable>( variablesPerRule.get( location ).values() );
				FodotVariable fvar = createVariable(var.getVariable().getName(), data.getTypes().get(0), usedVariables);
				variablesPerRule.get(location).put(var.getVariable(), fvar);			
			}
		}

		for (IGdlArgumentListDeclaration p : argumentLists.keySet()) {
			if (!getUnfilledDefaultPredicates().contains(p)) {
				GdlArgumentListData data = argumentLists.get(p);
				if (p instanceof GdlPredicateDeclaration) {
					GdlPredicateDeclaration pd = (GdlPredicateDeclaration) p;
					FodotPredicateDeclaration pf = createPredicateDeclaration(
							NameUtil.convertToValidPredicateName(p.getName().getValue()),
							data.getArgumentTypes());
					predicateDeclarations.put(pd, pf);
				} else if (p instanceof GdlFunctionDeclaration) {
					GdlFunctionDeclaration fd = (GdlFunctionDeclaration) p;
					GdlTermData termData = terms.get(fd);
					FodotTypeFunctionDeclaration ff = createTypeFunctionDeclaration(
							NameUtil.convertToValidPredicateName(fd.getName().getValue()),
							data.getArgumentTypes(),
							termData.getTypes().get(0));
					functionDeclarations.put(fd, ff);
				}
			}
		}		

		GdlVocabulary vocabulary =  new GdlVocabulary(
				this.timeType, this.playerType, this.actionType, 
				this.scoreType, this.otherTypes,

				constantsMap, variablesPerRule,
				functionDeclarations, predicateDeclarations,

				this.dynamicPredicates);

		vocabulary = this.getOptimizer().improve(vocabulary);
		
		System.out.println(vocabulary.toString());

		return vocabulary;
	}
	/**********************************************/

	/**********************************************
	 *  Optimizer
	 ***********************************************/

	public IGdlVocabularyOptimizer getOptimizer() {
		return optimizer;
	}

	public void setOptimizer(IGdlVocabularyOptimizer optimizer) {
		this.optimizer = optimizer;
	}
	/**********************************************/

	

	/**********************************************
	 *  GdlTransformer creator
	 ***********************************************/
	public GdlTransformer createTransformer() {
		return new GdlTypeIdentifierTransformer(this);
	}
	/**********************************************/

}
