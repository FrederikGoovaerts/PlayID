package playid.domain.gdl_transformers.first_phase;

import static playid.domain.fodot.FodotElementBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.gdl.grammar.*;

import playid.domain.exceptions.gdl.GdlTypeIdentificationError;
import playid.domain.fodot.structure.elements.typeenum.FodotTypeEnumeration;
import playid.domain.fodot.structure.elements.typeenum.elements.FodotInteger;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.theory.elements.terms.FodotConstant;
import playid.domain.fodot.theory.elements.terms.FodotVariable;
import playid.domain.fodot.vocabulary.elements.FodotPredicateDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.FodotTypeFunctionDeclaration;
import playid.domain.fodot.vocabulary.elements.IFodotDomainElement;
import playid.domain.gdl_transformers.GdlTransformer;
import playid.domain.gdl_transformers.GdlVocabulary;
import playid.domain.gdl_transformers.first_phase.data.GdlArgumentListData;
import playid.domain.gdl_transformers.first_phase.data.GdlTermData;
import playid.domain.gdl_transformers.first_phase.data.declarations.GdlConstantDeclaration;
import playid.domain.gdl_transformers.first_phase.data.declarations.GdlFunctionDeclaration;
import playid.domain.gdl_transformers.first_phase.data.declarations.GdlPredicateDeclaration;
import playid.domain.gdl_transformers.first_phase.data.declarations.GdlVariableDeclaration;
import playid.domain.gdl_transformers.first_phase.data.declarations.IGdlArgumentListDeclaration;
import playid.domain.gdl_transformers.first_phase.data.declarations.IGdlTermDeclaration;
import playid.domain.gdl_transformers.first_phase.data.occurrences.GdlTermOccurrence;
import playid.domain.patterns.gdl_vocabulary.GdlIntegerTypeRecognizer;
import playid.domain.patterns.gdl_vocabulary.GdlVocabularyChainOptimizer;
import playid.domain.patterns.gdl_vocabulary.IGdlVocabularyOptimizer;
import playid.util.FormulaUtil;
import playid.util.NameUtil;

public class GdlTypeIdentifier {

	private static final IGdlVocabularyOptimizer DEFAULT_OPTIMIZER = new GdlVocabularyChainOptimizer(new GdlIntegerTypeRecognizer());
	private IGdlVocabularyOptimizer optimizer = DEFAULT_OPTIMIZER;

	/**********************************************
	 *  Data maps
	 ***********************************************/
	private Map<IGdlTermDeclaration, GdlTermData> terms = new HashMap<>();
	private Map<IGdlArgumentListDeclaration, GdlArgumentListData> argumentLists = new HashMap<>();

	private Set<GdlPredicateDeclaration> dynamicPredicates = new HashSet<>();
	private Set<GdlProposition> propositions = new HashSet<>();
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

		registerDynamicRelation(doesPred);
		registerDynamicRelation(legalPred);
		registerDynamicRelation(terminalPred);
		registerDynamicRelation(goalPred);

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
	
	public void addPropositionOccurrence(GdlRule rule,
			GdlProposition proposition) {
		// TODO : Implement this?
		propositions.add(proposition);
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
			if (canPushUpdatesTo(directParent, argumentIndex, decl)) {
				updateTermType(decl, foundType);
			}
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

	private boolean canPushUpdatesTo(IGdlTermDeclaration from, IGdlArgumentListDeclaration to, int argIndex) {
		FodotType currentArgumentType = argumentLists.get(to).getArgumentType(argIndex);
		if (terms.get(from).getTypes().equals(currentArgumentType)) {
			return false;
		}
		return canPushTypeUpdates(from);
	}

	private boolean canPushUpdatesTo(IGdlArgumentListDeclaration from, int argIndex, IGdlTermDeclaration to) {
		FodotType argumentType = argumentLists.get(from).getArgumentType(argIndex);

		List<FodotType> currentTermTypes = terms.get(to).getTypes();
		if (currentTermTypes.contains(argumentType)) {
			return false;
		}

		/* 
		 * TODO Enkel rollen mogen constant op playertype zetten.
		 * In sommige singleplayer (mummymaze1p) spellen wordt er echter geimpliceerd
		 * dat er meerdere spelers zouden zijn.
		 */
		//		if (argumentType.equals(playerType) && !from.equals(getRole()) && to instanceof GdlConstantDeclaration) {
		//			System.out.println("Can't update constant to playertype: " + from + "::>" + to);
		//			return false;
		//		}

		return true;
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
	public void registerDynamicRelation(GdlSentence predicate) {
		registerDynamicRelation(new GdlPredicateDeclaration(predicate));
	}

	public void registerDynamicRelation(GdlPredicateDeclaration predicate) {
		if (!argumentLists.containsKey(predicate)) {
			initArgumentList(predicate);
		}
		dynamicPredicates.add(predicate);		
	}

	public boolean isDynamicSentence(GdlSentence predicate) {
		return isDynamicSentence(new GdlPredicateDeclaration(predicate));
	}

	public boolean isDynamicSentence(GdlPredicateDeclaration predicate) {
		return dynamicPredicates.contains(predicate);
	}
	/**********************************************/

    /***********************************************
     * Propositions
     **********************************************/

    public void registerProposition(GdlProposition prop){
        this.propositions.add(prop);
    }

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
		
		GdlTermData data = terms.get(declaration);

		//What if it already has a type?
		if (!data.canHaveMultipleTypes() && !data.hasOnlyType(unfilledType) && !data.hasOnlyType(foundType)) {
			throw new GdlTypeIdentificationError("Type collision in "+ declaration +" \nOld: " + data.getTypes() + "\nNew: " + foundType);
		}
		
		//Set the new type
		data.removeType(unfilledType);
		data.addType(foundType);

		//Update all occurrences
		for (GdlTermOccurrence occ : data.getOccurences()) {
			IGdlArgumentListDeclaration parent = occ.getDirectParent();
			if (canPushUpdatesTo(declaration, parent, occ.getArgumentIndex())) {
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
			if (canPushUpdatesTo(declaration, argumentNr, term)) {
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
        Map<GdlProposition, FodotPredicateDeclaration> propositionDeclarations = new HashMap<>();
		Set<GdlPredicateDeclaration> nonDefaultDynamicPredicates = new LinkedHashSet<GdlPredicateDeclaration>();

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
			if (!getDefaultPredicates().contains(p)) {
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

        for(GdlProposition prop : this.propositions) {
            propositionDeclarations.put(
                    prop,
                    createPredicateDeclaration(
                            NameUtil.convertToValidPredicateName(prop.getName().getValue()),
                            Arrays.asList(timeType)
                    )
            );
        }

		for (GdlPredicateDeclaration predicate : this.dynamicPredicates) {
			if (!getDefaultPredicates().contains(predicate)) {
				nonDefaultDynamicPredicates.add(predicate);
			}
		}

		//Find all elements of scoretype
		List<IFodotTypeEnumerationElement> scoreValues = new ArrayList<>();
		for (IFodotDomainElement el : scoreType.getDomainElements()) {
			if (el instanceof FodotInteger) {
				scoreValues.add( (FodotInteger) el);
			} else {
				throw new GdlTypeIdentificationError(
						"An illegal value in the score domain is detected."
						+ "\nAll values must be integer constants! \nDetected score value: " + el);
			}
		}
		FodotTypeEnumeration scoreEnumeration = createTypeEnumeration(scoreType, scoreValues);

		
		GdlVocabulary vocabulary =  new GdlVocabulary(
				this.timeType, this.playerType, this.actionType, 
				this.scoreType, this.otherTypes,

				constantsMap, variablesPerRule,
				functionDeclarations, predicateDeclarations,
                propositionDeclarations,

				nonDefaultDynamicPredicates,
				Arrays.asList(scoreEnumeration)
//				new HashSet<IFodotStructureElement>()
				);

		vocabulary = this.getOptimizer().improve(vocabulary);

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
