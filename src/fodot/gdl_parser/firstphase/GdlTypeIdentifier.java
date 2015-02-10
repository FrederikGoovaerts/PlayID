package fodot.gdl_parser.firstphase;

import static fodot.objects.FodotElementBuilder.createConstant;
import static fodot.objects.FodotElementBuilder.createPredicateDeclaration;
import static fodot.objects.FodotElementBuilder.createType;
import static fodot.objects.FodotElementBuilder.createTypeFunctionDeclaration;
import static fodot.objects.FodotElementBuilder.createVariable;
import static fodot.objects.FodotElementBuilder.getNaturalNumberType;

import java.util.ArrayList;
import java.util.Arrays;
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
import fodot.gdl_parser.firstphase.data.GdlArgumentListData;
import fodot.gdl_parser.firstphase.data.GdlTermData;
import fodot.gdl_parser.firstphase.data.IGdlArgumentListData;
import fodot.gdl_parser.firstphase.data.declarations.GdlConstantDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlPredicateDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlVariableDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.IGdlArgumentListDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.IGdlTermDeclaration;
import fodot.gdl_parser.firstphase.data.occurrences.GdlTermOccurrence;
import fodot.gdl_parser.firstphase.data.occurrences.IGdlTermOccurrence;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.FormulaUtil;
import fodot.util.NameUtil;

public class GdlTypeIdentifier {

	/**********************************************
	 *  Data maps
	 ***********************************************/
	//new sets
	private Map<IGdlTermDeclaration, GdlTermData> terms = new HashMap<>();
	private Map<IGdlArgumentListDeclaration, GdlArgumentListData> argumentLists = new HashMap<>();

	private Set<GdlPredicateDeclaration> dynamicPredicates = new HashSet<>();
	private Set<GdlRule> dynamicRules = new HashSet<>();
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
	private GdlPredicateDeclaration distinctPred = new GdlPredicateDeclaration ( GdlPool.getConstant("distinct"), 2);
	private GdlPredicateDeclaration doesPred = new GdlPredicateDeclaration ( GdlPool.getConstant("does"), 2);
	private GdlPredicateDeclaration goalPred = new GdlPredicateDeclaration ( GdlPool.getConstant("goal"), 2);
	private GdlPredicateDeclaration initPred = new GdlPredicateDeclaration ( GdlPool.getConstant("init"), 1);
	private GdlPredicateDeclaration legalPred = new GdlPredicateDeclaration ( GdlPool.getConstant("legal"), 2);
	private GdlPredicateDeclaration nextPred = new GdlPredicateDeclaration ( GdlPool.getConstant("next"), 1);
	private GdlPredicateDeclaration rolePred = new GdlPredicateDeclaration ( GdlPool.getConstant("role"), 1);
	private GdlPredicateDeclaration terminalPred = new GdlPredicateDeclaration ( GdlPool.getConstant("terminal"), 0);
	private GdlPredicateDeclaration truePred = new GdlPredicateDeclaration ( GdlPool.getConstant("true"), 1);

	private List<GdlPredicateDeclaration> defaultPredicates;
	private List<GdlPredicateDeclaration> unfilledPredicates;

	private void initDefaultPredicates() {
		initPredicate(distinctPred,	Arrays.asList(unfilledType, unfilledType),	true);
		initPredicate(doesPred,		Arrays.asList(playerType, actionType),		true);
		initPredicate(goalPred,		Arrays.asList(playerType, scoreType),		true);
		initPredicate(initPred,		Arrays.asList(unfilledType),				true);
		initPredicate(legalPred,	Arrays.asList(playerType, actionType),		true);
		initPredicate(nextPred,		Arrays.asList(unfilledType),				true);
		initPredicate(rolePred,		Arrays.asList(playerType),					true);
		initPredicate(terminalPred,	new ArrayList<FodotType>(),					true);
		initPredicate(truePred,		Arrays.asList(unfilledType),				true);

		makeDynamic(doesPred);
		makeDynamic(legalPred);
		makeDynamic(terminalPred);

		defaultPredicates = Arrays.asList(distinctPred, doesPred, goalPred, initPred, legalPred, nextPred, rolePred, terminalPred, truePred);
		unfilledPredicates = Arrays.asList(distinctPred, initPred, nextPred, truePred);
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

	public List<GdlPredicateDeclaration> getUnfilledPredicates() {
		return unfilledPredicates;
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
	
	private void initFunction(GdlFunctionDeclaration function, FodotType givenType) {

		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilledType, function.getArity() );

		terms.put(function, new GdlTermData(unfilledType));
		argumentLists.put(function, new GdlArgumentListData(argumentTypes));
		if (!unfilledType.equals(givenType)) {
			updateTermType(function, givenType);
		}
	}	
	private void initPredicate(GdlPredicateDeclaration predicate, List<FodotType> argArgumentTypes, boolean lockedTypes) {
		argumentLists.put(predicate, new GdlArgumentListData(FormulaUtil.createTypeList( unfilledType, predicate.getArity())));

		if (lockedTypes) {
			argumentLists.get(predicate).lockTypes();
		}

		for (int i = 0; i < argArgumentTypes.size(); i++) {
			if (!argArgumentTypes.get(i).equals(unfilledType)) {
				updatePredicateArgumentType(predicate, i, argArgumentTypes.get(i));
			}
		}
	}
	private void initPredicate(GdlPredicateDeclaration predicate) {
		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilledType, predicate.getArity() );
		initPredicate(predicate, argumentTypes, false);
	}
	/**********************************************/

	//TODO remove
	private FodotType getArgumentType(IGdlArgumentListDeclaration decl,
			int argumentIndex) {
		if (argumentLists.containsKey(decl)) {
			return argumentLists.get(decl).getArgumentType(argumentIndex);
		}
		throw new GdlTypeIdentificationError("Given declaration isn't a known predicate or function:" + decl);
	}



	/**********************************************
	 *  Adding occurrences of elements.
	 *  This should be done by a visitor.
	 ***********************************************/
	//TODO enkel term occurrence tracking, niet specifiek subklasse. Ook maar twee mappings: terms en argumentlist mappings


	private void addTermOccurrence(IGdlArgumentListDeclaration directParent, int argumentIndex, IGdlTermDeclaration decl) {
		FodotType foundType = getArgumentType(directParent, argumentIndex);
		
		//Initialize list if first occurrence of constant
		if (terms.get(decl) == null) {
			initTerm(decl);
		}		
		
		//Add occurrences
		terms.get(decl).addOccurence( new GdlTermOccurrence(directParent, argumentIndex) );
		argumentLists.get(directParent).addArgumentOccurrence(argumentIndex, decl);

		//Edit typing if necessary
		if (!foundType.equals(unfilledType)) {
			updateTermType(decl, foundType);
		}
	}

	public void addConstantOccurrence(IGdlArgumentListDeclaration directParent, int argumentIndex, GdlConstant constant) {
		GdlConstantDeclaration decl = new GdlConstantDeclaration(constant);
		addTermOccurrence(directParent, argumentIndex, decl);
	}

	
	public void addVariableOccurrence(GdlRule parentRule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlVariable argVariable) {
		GdlVariableDeclaration decl = new GdlVariableDeclaration(argVariable, parentRule);
		addTermOccurrence(directParent, argumentIndex, decl);

	}

	public void addFunctionOccurrence(GdlRule parentRule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlFunction function) {
		GdlFunctionDeclaration declaration = new GdlFunctionDeclaration( function );
		addArgumentListOccurrence(declaration);
		addTermOccurrence(directParent, argumentIndex, declaration);
	}


	public void addPredicateOccurrence(GdlRule parentRule, GdlRelation predicate) {
		GdlPredicateDeclaration declaration = new GdlPredicateDeclaration( predicate );
		addArgumentListOccurrence(declaration);
	}
	
	private void addArgumentListOccurrence(IGdlArgumentListDeclaration declaration) {
		if (argumentLists.get(declaration) == null) {
			initArgumentList(declaration);
		}		
	}

	public void makeRuleDynamic(GdlRule rule) {
		dynamicRules.add(rule);
	}

	public boolean isDynamic(GdlRule rule) {
		return dynamicRules.contains(rule);
	}

	public void makePredicateDynamic(GdlRelation predicate) {
		makeDynamic(new GdlPredicateDeclaration(predicate));
	}

	public void makeDynamic(GdlPredicateDeclaration predicate) {
		if (!argumentLists.containsKey(predicate)) {
			initPredicate(predicate);
		}
		dynamicPredicates.add(predicate);		
	}

	public boolean isDynamic(GdlRelation predicate) {
		return isDynamic(new GdlPredicateDeclaration(predicate));
	}

	public boolean isDynamic(GdlPredicateDeclaration predicate) {
		if (!argumentLists.containsKey(predicate)) {
			initPredicate(predicate);
		}
		return dynamicPredicates.contains(predicate);
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

		if (!terms.containsKey(declaration)) {
			initTerm(declaration);
		}

		GdlTermData data = terms.get(declaration);

		//What if it already has a type?
		if (!data.getType().equals(unfilledType) && !data.getType().equals(foundType)) {
			//TODO: add to both domains OR make a type that's a subtype of both, we'll see!
			throw new GdlTypeIdentificationError("Type collision: \nOld: " + data.getType() + "\nNew: " + foundType);
		}

		//Set the new type
		data.setType(foundType);

		//Constants can't push scoretype updates.
		if (! ((declaration instanceof GdlConstantDeclaration) && data.getType().equals(scoreType) ) ) {
			//Update all occurrences
			for (IGdlTermOccurrence occ : data.getOccurences()) {
				IGdlArgumentListDeclaration parent = occ.getDirectParent();
				if (!argumentLists.get(parent).getArgumentType(occ.getArgumentIndex()).equals(foundType)) {
					updateFunctionArgumentType( parent, occ.getArgumentIndex(), foundType);		
				}
			}		
		}
	}
	/**********************************************/


	/**********************************************
	 *  Argument types
	 ***********************************************/
	private void updatePredicateArgumentType(GdlPredicateDeclaration predicate, int argumentNr, FodotType foundType) {
		if (!argumentLists.containsKey(predicate)) {
			initPredicate(predicate);
		}
		GdlArgumentListData data = argumentLists.get(predicate);
		if (data.isTypeLocked()) {
			return;
		}
		updateArgumentListArgumentType(data, argumentNr, foundType);
	}

	private void updateFunctionArgumentType(IGdlArgumentListDeclaration function, int argumentNr, FodotType foundType) {
		if (!argumentLists.containsKey(function)) {
			if (function instanceof GdlFunctionDeclaration) {
				initFunction((GdlFunctionDeclaration) function, unfilledType);
			} else {
				initPredicate((GdlPredicateDeclaration) function);

			}
		}
		GdlArgumentListData data = argumentLists.get(function);
		updateArgumentListArgumentType(data, argumentNr, foundType);
	}

	/**
	 * Helper method for function&predicate argument types
	 * @param data
	 * @param argumentNr
	 * @param foundType
	 */
	private void updateArgumentListArgumentType(IGdlArgumentListData data, int argumentNr, FodotType foundType) {
		assert !foundType.equals(unfilledType);

		//Return if already set
		if (foundType.equals(data.getArgumentType(argumentNr))) {
			return;
		}

		//What if it already has a type?
		if (!data.getArgumentType(argumentNr).equals(unfilledType)) {
			throw new GdlTypeIdentificationError("Type collision in " + data);
		}

		//Set typing
		data.setArgumentType(argumentNr, foundType);

		//Update all occurrences
		//TODO save each argumentoccurrence in data, add it when adding terms.

		for (IGdlTermDeclaration occ : data.getArgumentOccurrences(argumentNr)) {
			updateTermType(occ, foundType );
		}

		//		for (GdlArgumentListOccurrence occ : data.getOccurences()) {
		//			GdlTerm term = occ.getArgument(argumentNr);
		//			if (term instanceof GdlConstant) {
		//				updateConstantType( (GdlConstant) term, foundType );
		//			} else if (term instanceof GdlVariable) {
		//				GdlRule rule = occ.getRule();
		//				updateVariableType( new GdlVariableDeclaration((GdlVariable) term, rule), foundType );
		//			} else if (term instanceof GdlFunction) {
		//				updateFunctionType( new GdlFunctionDeclaration((GdlFunction) term), foundType);
		//			}
		//		}

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
			data.addArgumentType(timeType);
		}
	}

	private void fillMissingTypes() {
		// SET ALL "unfilled" TO "All".
		/* TODO: You can identify "chains" of types, so you can subdivide all
		 * into more specific types. This will improve grounding in IDP.
		 * We need to prove the correctness of this though. (or disprove the current correctness)
		 */

		FodotType fillType = this.allType;

		//Constants to all
		for (IGdlTermDeclaration term : terms.keySet()) {
			GdlTermData data = terms.get(term);
			if (data.getType().equals(unfilledType)) {
				//				updateConstantType(constant, fillType);
				data.setType(fillType);
			}
		}

		//Predicate arguments to all
		for (IGdlArgumentListDeclaration predicate : argumentLists.keySet()) {
			GdlArgumentListData data = argumentLists.get(predicate);
			for (int i = 0; i < data.getAmountOfArguments(); i++) {
				if (data.getArgumentType(i).equals(unfilledType)) {
					//					updatePredicateArgumentType(predicate, i, fillType);
					if (!data.isTypeLocked()) {
						data.setArgumentType(i, fillType);
					}
				}				
			}
		}
	}

	public GdlVocabulary generateTranslationData() {

		fillMissingTypes();
		addTimeVariableToDynamicPredicates();

		Map<GdlConstant, FodotConstant> constantsMap = new HashMap<GdlConstant, FodotConstant>();
		Map<GdlFunctionDeclaration, FodotFunctionDeclaration> functionDeclarations = new HashMap<GdlFunctionDeclaration, FodotFunctionDeclaration>();
		Map<GdlPredicateDeclaration, FodotPredicateDeclaration> predicateDeclarations = new HashMap<GdlPredicateDeclaration, FodotPredicateDeclaration>();
		Map<GdlRule, Map<GdlVariable, FodotVariable>> variablesPerRule = new HashMap<GdlRule, Map<GdlVariable, FodotVariable>>();

		//GENERATE SETS FOR GDLFODOTDATA
		for (IGdlTermDeclaration term : terms.keySet()) {
			GdlTermData data = terms.get(term);
			if (term instanceof GdlConstantDeclaration) {
				GdlConstantDeclaration cc = (GdlConstantDeclaration) term;
				FodotConstant fc = createConstant( NameUtil.convertToValidConstantName(cc.getConstant().getValue(), data.getType()) , data.getType());
				constantsMap.put(cc.getConstant(), fc);
			} if (term instanceof GdlVariableDeclaration) {
				GdlVariableDeclaration var = (GdlVariableDeclaration) term;
				GdlRule location = var.getLocation();
				if (!variablesPerRule.containsKey(var.getLocation())) {
					variablesPerRule.put(location, new HashMap<GdlVariable,FodotVariable>());
				}
				Set<FodotVariable> usedVariables = new HashSet<FodotVariable>( variablesPerRule.get( location ).values() );
				FodotVariable fvar = createVariable(var.getVariable().getName(), data.getType(), usedVariables);
				variablesPerRule.get(location).put(var.getVariable(), fvar);			
			} if (term instanceof GdlFunctionDeclaration) {
				System.out.println("todo"); //TODO
			}
		}

		for (IGdlArgumentListDeclaration p : argumentLists.keySet()) {
			if (!getUnfilledPredicates().contains(p)) {
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
					FodotFunctionDeclaration ff = createTypeFunctionDeclaration(
							NameUtil.convertToValidPredicateName(fd.getName().getValue()),
							data.getArgumentTypes(),
							termData.getType());
					functionDeclarations.put(fd, ff);
				}
			}
		}		

		GdlVocabulary vocabulary =  new GdlVocabulary(
				this.timeType, this.playerType, this.actionType, 
				this.scoreType, this.allType,

				constantsMap, functionDeclarations, predicateDeclarations,
				variablesPerRule, this.dynamicPredicates);

		System.out.println(vocabulary.toString());

		return vocabulary;
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
