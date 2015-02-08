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
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.exceptions.gdl.GdlTypeIdentificationError;
import fodot.gdl_parser.GdlVocabulary;
import fodot.gdl_parser.GdlTransformer;
import fodot.gdl_parser.firstphase.data.GdlConstantData;
import fodot.gdl_parser.firstphase.data.GdlFunctionData;
import fodot.gdl_parser.firstphase.data.GdlPredicateData;
import fodot.gdl_parser.firstphase.data.GdlVariableData;
import fodot.gdl_parser.firstphase.data.IGdlArgumentListData;
import fodot.gdl_parser.firstphase.data.IGdlTermData;
import fodot.gdl_parser.firstphase.data.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlPredicateDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlVariableDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.IGdlArgumentListDeclaration;
import fodot.gdl_parser.firstphase.data.occurrences.GdlArgumentListOccurrence;
import fodot.gdl_parser.firstphase.data.occurrences.GdlConstantOccurrence;
import fodot.gdl_parser.firstphase.data.occurrences.GdlFunctionOccurrence;
import fodot.gdl_parser.firstphase.data.occurrences.GdlPredicateOccurrence;
import fodot.gdl_parser.firstphase.data.occurrences.GdlVariableOccurrence;
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
	private Map<GdlConstant, GdlConstantData> constants = new HashMap<GdlConstant, GdlConstantData>();
	private Map<GdlVariableDeclaration, GdlVariableData> variables = new HashMap<GdlVariableDeclaration, GdlVariableData>();
	private Map<GdlPredicateDeclaration, GdlPredicateData> predicates = new HashMap<GdlPredicateDeclaration, GdlPredicateData>();	
	private Map<GdlFunctionDeclaration, GdlFunctionData> functions = new HashMap<GdlFunctionDeclaration, GdlFunctionData>();
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

		defaultPredicates = Arrays.asList(distinctPred, doesPred, goalPred, initPred, legalPred, nextPred, rolePred, terminalPred, truePred);
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
	private void initConstant(GdlConstant constant, FodotType givenType) {
		constants.put(constant, new GdlConstantData(givenType));
	}
	private void initVariable(GdlVariableDeclaration variable, FodotType givenType) {
		variables.put(variable, new GdlVariableData(givenType));
	}
	private void initFunction(GdlFunctionDeclaration function, FodotType givenType) {
		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilledType, function.getArity() );
		functions.put(function, new GdlFunctionData(givenType, argumentTypes) );
	}	
	private void initPredicate(GdlPredicateDeclaration predicate, List<FodotType> argumentTypes, boolean lockedTypes) {
		predicates.put(predicate, new GdlPredicateData(argumentTypes, lockedTypes));	
	}
	private void initPredicate(GdlPredicateDeclaration predicate) {
		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilledType, predicate.getArity() );
		initPredicate(predicate, argumentTypes, false);
	}
	/**********************************************/

	private FodotType getArgumentType(IGdlArgumentListDeclaration decl,
			int argumentIndex) {
		if (functions.containsKey(decl)) {
			return functions.get(decl).getArgumentType(argumentIndex);
		}
		if (predicates.containsKey(decl)) {
			return predicates.get(decl).getArgumentType(argumentIndex);
		}
		throw new GdlTypeIdentificationError("Given declaration isn't a known predicate or function:" + decl);
	}



	/**********************************************
	 *  Adding occurrences of elements.
	 *  This should be done by a visitor.
	 ***********************************************/
	public void addConstantOccurrence(IGdlArgumentListDeclaration directParent, int argumentIndex, GdlConstant constant) {
		FodotType foundType = getArgumentType(directParent, argumentIndex);
		addConstantOccurrence(directParent, argumentIndex, constant, foundType);
	}

	public void addConstantOccurrence(IGdlArgumentListDeclaration directParent, int argumentIndex, GdlConstant constant, FodotType givenType) {

		//Initialize list if first occurrence of constant
		if (constants.get(constant) == null) {
			initConstant(constant, givenType);
		}
		constants.get(constant).addOccurences( new GdlConstantOccurrence(directParent, argumentIndex) );

		//Edit typing if necessary
		if (!givenType.equals(unfilledType)) {
			updateConstantType(constant, givenType);
		}

	}

	public void addVariableOccurrence(GdlRule parentRule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlVariable variable) {
		FodotType foundType = getArgumentType(directParent, argumentIndex);
		addVariableOccurrence(parentRule, directParent, argumentIndex, variable, foundType);
	}

	public void addVariableOccurrence(GdlRule parentRule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlVariable argVariable, FodotType givenType) {

		GdlVariableDeclaration variable = new GdlVariableDeclaration(argVariable, parentRule);

		//Initialize list if first occurrence of variable
		if (variables.get(variable) == null) {
			initVariable(variable, givenType);
		}
		variables.get(variable).addOccurences( new GdlVariableOccurrence(directParent, argumentIndex) );

		//Edit typing if necessary
		if (!givenType.equals(unfilledType)) {
			updateVariableType(variable, givenType);
		}

	}

	public void addPredicateOccurrence(GdlRule parentRule, GdlRelation predicate) {
		GdlPredicateDeclaration head = new GdlPredicateDeclaration( predicate );

		//Initialize list if first occurrence of the predicate
		if (predicates.get(head) == null) {
			initPredicate(head);
		}
		predicates.get(head).addOccurence( new GdlPredicateOccurrence(parentRule, predicate) );

	}

	public void addFunctionOccurrence(GdlRule parentRule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlFunction function) {
		FodotType foundType = getArgumentType(directParent, argumentIndex);
		addFunctionOccurrence(parentRule, directParent, argumentIndex, function, foundType);
	}

	public void addFunctionOccurrence(GdlRule parentRule, IGdlArgumentListDeclaration directParent, int argumentIndex, GdlFunction function, FodotType givenType) {
		GdlFunctionDeclaration head = new GdlFunctionDeclaration( function );

		//Initialize list if first occurrence of the predicate
		if (functions.get(head) == null) {
			initFunction(head, givenType);
		}
		functions.get(head).addOccurence( new GdlFunctionOccurrence(parentRule, directParent, argumentIndex, function) );

		if (!givenType.equals(unfilledType)) {
			updateFunctionType(head, givenType);
		}
	}

	public void makeDynamic(GdlRelation predicate) {
		makeDynamic(new GdlPredicateDeclaration(predicate));
	}

	public void makeDynamic(GdlPredicateDeclaration predicate) {
		predicates.get(predicate).makeDynamic();		
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
	private void updateConstantType(GdlConstant argConstant, FodotType foundType) {
		if (!constants.containsKey(argConstant)) {
			initConstant(argConstant, foundType);
		}
		GdlConstantData constantData = constants.get(argConstant);
		updateTermType(constantData, foundType);
	}

	private void updateVariableType(GdlVariableDeclaration argVariable, FodotType foundType) {
		if (!variables.containsKey(argVariable)) {
			initVariable(argVariable, foundType);
		}
		GdlVariableData variableData = variables.get(argVariable);		
		updateTermType(variableData, foundType);
	}

	private void updateFunctionType(GdlFunctionDeclaration argFunction, FodotType foundType) {
		if (!functions.containsKey(argFunction)) {
			initFunction(argFunction, foundType);
		}
		GdlFunctionData functionData = functions.get(argFunction);		
		updateTermType(functionData, foundType);
	}

	/**
	 * Helper method for updating the type of GDL terms.
	 * It also updates the type of arguments when knowing the type of constants, variables and functions
	 */
	private void updateTermType(IGdlTermData data, FodotType foundType) {
		assert !foundType.equals(unfilledType);

		if (foundType.equals(data.getType())) {
			return;
		}

		//What if it already has a type?
		if (!data.getType().equals(unfilledType)) {
			//TODO: add to both domains OR make a type that's a subtype of both, we'll see!
			throw new GdlTypeIdentificationError("Type collision: \nOld: " + data.getType() + "\nNew: " + foundType);
		}

		//Set the new type
		data.setType(foundType);

		//Update all occurrences
		for (IGdlTermOccurrence occ : data.getOccurences()) {
			IGdlArgumentListDeclaration parent = occ.getDirectParent();
			if (parent == null) {
				//no op
			} else if (parent instanceof GdlPredicateDeclaration) {
				updatePredicateArgumentType( (GdlPredicateDeclaration) parent, occ.getArgumentIndex(), foundType);
			} else if (parent instanceof GdlFunctionDeclaration) {
				updateFunctionArgumentType( (GdlFunctionDeclaration) parent, occ.getArgumentIndex(), foundType);				
			} else {
				throw new GdlTypeIdentificationError("Terms should appear in anything else but functions and predicates, right?");
			}		
		}
	}
	/**********************************************/


	/**********************************************
	 *  Argument types
	 ***********************************************/
	private void updatePredicateArgumentType(GdlPredicateDeclaration predicate, int argumentNr, FodotType foundType) {
		if (!predicates.containsKey(predicate)) {
			initPredicate(predicate);
		}
		GdlPredicateData data = predicates.get(predicate);
		if (data.isTypeLocked()) {
			return;
		}
		updateArgumentListArgumentType(data, argumentNr, foundType);
	}

	private void updateFunctionArgumentType(GdlFunctionDeclaration function, int argumentNr, FodotType foundType) {
		if (!functions.containsKey(function)) {
			initFunction(function, unfilledType);
		}
		GdlFunctionData data = functions.get(function);
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
		for (GdlArgumentListOccurrence occ : data.getOccurences()) {
			GdlTerm term = occ.getArgument(argumentNr);
			if (term instanceof GdlConstant) {
				updateConstantType( (GdlConstant) term, foundType );
			} else if (term instanceof GdlVariable) {
				GdlRule rule = occ.getRule();
				updateVariableType( new GdlVariableDeclaration((GdlVariable) term, rule), foundType );
			} else if (term instanceof GdlFunction) {
				updateFunctionType( new GdlFunctionDeclaration((GdlFunction) term), foundType);
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
		for (GdlPredicateDeclaration predicate : predicates.keySet()) {
			GdlPredicateData data = predicates.get(predicate);
			if (data.isDynamic()) {
				data.addArgumentType(timeType);
			}
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
		for (GdlConstant constant : constants.keySet()) {
			GdlConstantData data = constants.get(constant);
			if (data.getType().equals(unfilledType)) {
				//				updateConstantType(constant, fillType);
				data.setType(fillType);
			}
		}

		//Variables to all
		for (GdlVariableDeclaration variable : variables.keySet()) {
			GdlVariableData data = variables.get(variable);
			if (data.getType().equals(unfilledType)) {
				//				updateVariableType(variable, fillType);
				data.setType(fillType);
			}
		}


		//Function types to all
		for (GdlFunctionDeclaration function : functions.keySet()) {
			GdlFunctionData data = functions.get(function);
			if (data.getType().equals(unfilledType)) {
				//				updateFunctionType(function, fillType);
				data.setType(fillType);
			}
		}

		//Predicate arguments to all
		for (GdlPredicateDeclaration predicate : predicates.keySet()) {
			GdlPredicateData data = predicates.get(predicate);
			for (int i = 0; i < data.getAmountOfArguments(); i++) {
				if (data.getArgumentType(i).equals(unfilledType)) {
					//					updatePredicateArgumentType(predicate, i, fillType);
					if (!data.isTypeLocked()) {
						data.setArgumentType(i, fillType);
					}
				}				
			}
		}

		//Function arguments to all
		for (GdlFunctionDeclaration function : functions.keySet()) {
			GdlFunctionData data = functions.get(function);
			for (int i = 0; i < data.getAmountOfArguments(); i++) {
				if (data.getArgumentType(i).equals(unfilledType)) {
					//					updateFunctionArgumentType(function, i, fillType);
					data.setArgumentType(i, fillType);
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
		Set<GdlPredicateDeclaration> dynamicPredicates = new HashSet<GdlPredicateDeclaration>();

		//GENERATE SETS FOR GDLFODOTDATA
		for (GdlConstant c : constants.keySet()) {
			GdlConstantData data = constants.get(c);
			FodotConstant fc = createConstant( NameUtil.convertToValidConstantName(c.getValue(), data.getType()) , data.getType());
			constantsMap.put(c, fc);
		}

		for (GdlFunctionDeclaration f : functions.keySet()) {
			GdlFunctionData data = functions.get(f);
			FodotFunctionDeclaration ff = createTypeFunctionDeclaration(
					NameUtil.convertToValidPredicateName(f.getName().getValue()),
					data.getArgumentTypes(),
					data.getType());
			functionDeclarations.put(f, ff);
//			System.out.println(ff.toCode() + " :: function");
		}

		for (GdlPredicateDeclaration p : predicates.keySet()) {
			if (!getDefaultPredicates().contains(p)) {
				GdlPredicateData data = predicates.get(p);
				FodotPredicateDeclaration pf = createPredicateDeclaration(
						NameUtil.convertToValidPredicateName(p.getName().getValue()),
						data.getArgumentTypes());
				predicateDeclarations.put(p, pf);
//				System.out.println(pf.toCode());

				if (data.isDynamic()) {
					dynamicPredicates.add(p);
				}
			}
		}		

		for (GdlVariableDeclaration var : variables.keySet()) {
			GdlRule location = var.getLocation();
			if (!variablesPerRule.containsKey(var.getLocation())) {
				variablesPerRule.put(location, new HashMap<GdlVariable,FodotVariable>());
			}
			Set<FodotVariable> usedVariables = new HashSet<FodotVariable>( variablesPerRule.get( location ).values() );

			GdlVariableData data = variables.get(var);
			FodotVariable fvar = createVariable(var.getVariable().getName(), data.getType(), usedVariables);

			variablesPerRule.get(location).put(var.getVariable(), fvar);			
		}

//		System.out.println(timeType.getDeclaration().toCode());
//		System.out.println(playerType.getDeclaration().toCode());
//		System.out.println(actionType.getDeclaration().toCode());
//		System.out.println(scoreType.getDeclaration().toCode());
//		System.out.println(allType.getDeclaration().toCode());

		return new GdlVocabulary(
				this.timeType, this.playerType, this.actionType, 
				this.scoreType, this.allType,

				constantsMap, functionDeclarations, predicateDeclarations,
				variablesPerRule, dynamicPredicates);
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
