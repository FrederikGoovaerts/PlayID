package fodot.gdl_parser.firstphase;

import static fodot.objects.FodotElementBuilder.createType;
import static fodot.objects.FodotElementBuilder.getNaturalNumberType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlDistinct;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlProposition;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.exceptions.gdl.GdlTypeIdentificationError;
import fodot.gdl_parser.GdlFodotData;
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
import fodot.gdl_parser.visitor.GdlRootVisitors;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.FormulaUtil;

public class GdlTypeIdentifier {
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
	private GdlPredicateDeclaration distinct = new GdlPredicateDeclaration ( GdlPool.getConstant("distinct"), 2);
	private GdlPredicateDeclaration does = new GdlPredicateDeclaration ( GdlPool.getConstant("does"), 2);
	private GdlPredicateDeclaration goal = new GdlPredicateDeclaration ( GdlPool.getConstant("goal"), 2);
	private GdlPredicateDeclaration init = new GdlPredicateDeclaration ( GdlPool.getConstant("init"), 1);
	private GdlPredicateDeclaration legal = new GdlPredicateDeclaration ( GdlPool.getConstant("legal"), 2);
	private GdlPredicateDeclaration next = new GdlPredicateDeclaration ( GdlPool.getConstant("next"), 1);
	private GdlPredicateDeclaration role = new GdlPredicateDeclaration ( GdlPool.getConstant("role"), 1);
	private GdlPredicateDeclaration terminal = new GdlPredicateDeclaration ( GdlPool.getConstant("terminal"), 0);
	private GdlPredicateDeclaration truePred = new GdlPredicateDeclaration ( GdlPool.getConstant("true"), 1);
	/**********************************************/

	/**********************************************
	 *  Data maps
	 ***********************************************/
	private Map<GdlConstant, GdlConstantData> constants = new HashMap<GdlConstant, GdlConstantData>();
	private Map<GdlVariableDeclaration, GdlVariableData> variables = new HashMap<GdlVariableDeclaration, GdlVariableData>();
	private Map<GdlPredicateDeclaration, GdlPredicateData> predicates = new HashMap<GdlPredicateDeclaration, GdlPredicateData>();	
	private Map<GdlFunctionDeclaration, GdlFunctionData> functions = new HashMap<GdlFunctionDeclaration, GdlFunctionData>();
	/**********************************************/

	/**********************************************
	 *  Constructor
	 ***********************************************/
	public GdlTypeIdentifier() {
		initializeMaps();
	}
	
	private void initializeMaps() {
		initPredicate(distinct);
		initPredicate(does);
		initPredicate(goal);
		initPredicate(init);
		initPredicate(legal);
		initPredicate(next);
		initPredicate(role);
		initPredicate(terminal);
		initPredicate(truePred);
		
		//TODO: make typelocking system so the "fixed" type don't pass type updates.
		
		//DOES
		updatePredicateArgumentType(does, 0, playerType);
		updatePredicateArgumentType(does, 1, actionType);
		
		//GOAL
		updatePredicateArgumentType(goal, 0, playerType);
		updatePredicateArgumentType(goal, 1, scoreType);
		
		//LEGAL
		updatePredicateArgumentType(legal, 0, playerType);
		updatePredicateArgumentType(legal, 1, actionType);
		
		//ROLE
		updatePredicateArgumentType(role, 0, playerType);
		
	}
	/**********************************************/

	
	
	/**********************************************
	 *  Init entries
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
	private void initPredicate(GdlPredicateDeclaration predicate) {
		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilledType, predicate.getArity() );
		predicates.put(predicate, new GdlPredicateData(argumentTypes));	
	}	

	/**********************************************/


	/**********************************************
	 *  Adding occurrences of elements
	 ***********************************************/
	public void addConstantOccurrence(IGdlArgumentListDeclaration directParent, int argumentIndex, GdlConstant constant) {
		addConstantOccurrence(directParent, argumentIndex, constant, unfilledType);
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
		addVariableOccurrence(parentRule, directParent, argumentIndex, variable, unfilledType);
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
		addFunctionOccurrence(parentRule, directParent, argumentIndex, function, unfilledType);
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

	/**********************************************/

	/**********************************************
	 *  Updating the typing of something
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
	
	/**
	 * This method can be used by the typechangers if they don't know the type of their terms
	 * Use this for things like "next, "does", "legal" etc.
	 * @param rule
	 * @param term
	 * @param foundType
	 */
	@Deprecated
	private void updateTermType(GdlRule rule, GdlTerm term, FodotType foundType) {
		if (term instanceof GdlConstant) {
			updateConstantType((GdlConstant) term, foundType);
		} else if (term instanceof GdlFunction) {
			updateFunctionType(new GdlFunctionDeclaration((GdlFunction) term), foundType);
		} else if (term instanceof GdlVariable) {
			updateVariableType(new GdlVariableDeclaration((GdlVariable) term, rule), foundType);
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
			throw new GdlTypeIdentificationError("Type collision");
		}

		//Set typing
		data.setArgumentType(argumentNr, foundType);

		//Update all occurrences
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

	/**********************************************
	 *  Dynamic spotting
	 ***********************************************/
	private void makeDynamic(GdlRelation predicate) {
		predicates.get(new GdlPredicateDeclaration(predicate)).makeDynamic();
	}

	/**********************************************/

	

	/**********************************************/




	/**********************************************
	 *  Resulting GdlFodotData generator
	 ***********************************************/
	public GdlFodotData getResultingData() {
		Map<GdlConstant, FodotConstant> constantsMap = new HashMap<GdlConstant, FodotConstant>();
		Map<GdlFunctionDeclaration, FodotFunctionDeclaration> functionDeclarations = new HashMap<GdlFunctionDeclaration, FodotFunctionDeclaration>();
		Map<GdlPredicateDeclaration, FodotPredicateDeclaration> predicateDeclarations = new HashMap<GdlPredicateDeclaration, FodotPredicateDeclaration>();
		Map<GdlRule, Map<GdlVariable, FodotVariable>> variablesPerRule = new HashMap<GdlRule, Map<GdlVariable, FodotVariable>>();
		Set<GdlPredicateDeclaration> dynamicPredicates = new HashSet<GdlPredicateDeclaration>();

		//TODO: mapping of TypeIdentifier data to GdlFodotData data

		//		System.out.println("c"+constants);
		//		System.out.println("f"+functionDeclarations);
		//		System.out.println("p"+predicateDeclarations);
		//		System.out.println("v"+variablesPerRule);
		//		System.out.println("d"+dynamicPredicates);

		return new GdlFodotData(
				this.timeType, this.playerType, this.actionType, this.scoreType, this.allType,
				constantsMap, functionDeclarations, predicateDeclarations,
				variablesPerRule, dynamicPredicates);
	}

	/**********************************************/


	/**********************************************
	 *  GdlTransformer creator
	 ***********************************************/
	public GdlTransformer createTransformer() {
		return new GdlTypeIdentifierTransformer();
	}

	/* =======================================
	 * ====== TRANSFORMER: THE VISITOR ======
	 * ======================================= */

	/**
	 * This class will be used to visit the relations and rules.
	 * It will use the GdlRuleElementsVisitor to visit the rules deeper.
	 * @author Thomas Winters
	 */
	private class GdlTypeIdentifierTransformer implements GdlTransformer {

		/**********************************************
		 *  Relation processing: only for type defining.
		 *  Most of the time, you won't add the occurrence
		 *  because init, legal etc are not part of our GDL file
		 ***********************************************/

		@Override
		public void processRoleRelation(GdlRelation relation) {
			visitPredicateArguments(null, relation);
			updateTermType(null, relation.get(0), playerType);
		}

		@Override
		public void processInitRelation(GdlRelation relation) {
			visitPredicateArguments(null, relation);
//			makeDynamic(relation.get(0)); //Is this right? It has an initial status, so it must be dynamic, right?
		}

		@Override
		public void processStaticPredicateRelation(GdlRelation relation) {
			addPredicateOccurrence(null, relation);
			visitPredicateArguments(null, relation);
		}

		@Override
		public void processLegalRelation(GdlRelation relation) {
			visitPredicateArguments(null, relation);
			updateTermType(null, relation.get(0), playerType);
			updateTermType(null, relation.get(0), actionType);
		}
		
		/**********************************************/

		/**********************************************
		 *  Rule processing
		 ***********************************************/

		@Override
		public void processNextRule(GdlRule rule) {
			visitRuleArguments(rule);
			visitRuleBody(rule);
			updateTermType(rule, rule.getHead().get(0), actionType);
		}

		@Override
		public void processLegalRule(GdlRule rule) {
			visitRuleArguments(rule);
			visitRuleBody(rule);
			updateTermType(rule, rule.getHead().get(0), playerType);
			updateTermType(rule, rule.getHead().get(1), actionType);
		}

		@Override
		public void processGoalRule(GdlRule rule) {
			visitRuleArguments(rule);
			visitRuleBody(rule);
			updateTermType(rule, rule.getHead().get(0), playerType);
			updateTermType(rule, rule.getHead().get(1), scoreType);
		}

		@Override
		public void processTerminalRule(GdlRule rule) {
			visitRuleArguments(rule);
			visitRuleBody(rule);
		}

		@Override
		public void processDefinitionRule(GdlRule rule) {
			addPredicateOccurrence(rule, GdlPool.getRelation(rule.getHead().getName()));
			visitRuleBody(rule);
		}		

		/**
		 * We don't want to visit the head itself, only the contents of the head and the body.
		 * Things like "next", "does", "goal" ... are not intrinsic parts of our GDL file!
		 */		
		private void visitRuleArguments(GdlRule rule) {
			visitSentenceElements(rule, rule.getHead(), null);
		}
		
		private void visitRuleBody(GdlRule rule) {
			GdlRuleElementsVisitor bodyVisitor = new GdlRuleElementsVisitor(rule);
			GdlRootVisitors.visitAll(rule.getBody(), bodyVisitor);
		}
		/**********************************************/
	}

	private class GdlRuleElementsVisitor extends GdlVisitor {
		//We will need this rule so we can tell what variables are the same.
		private GdlRule rule;

		public GdlRuleElementsVisitor(GdlRule rule) {
			super();
			this.rule = rule;
		}

		public void visitRelation(GdlRelation predicate) {
			addPredicateOccurrence(rule, predicate);
			visitPredicateArguments(rule, predicate);
		}
//		public void visitFunction(GdlFunction function) {
//			GdlSentenceVisitor functionArgumentsVisitor = new GdlSentenceVisitor(rule, function.toSentence(), function);
//			functionArgumentsVisitor.visitElements();
//		}

		public void visitProposition(GdlProposition proposition) {
			// Do nothing?
		}

		public void visitDistinct(GdlDistinct distinct) {
			visitElements(rule, Arrays.asList(distinct.getArg1(), distinct.getArg2()), null);
		}

		public void visitRule(GdlRule rule) {
			throw new GdlTypeIdentificationError("A rule occured in a rule, that's not possible, right?");
		}

	}


	/**********************************************
	 *  Sentence visitor
	 ***********************************************/
	
	public void visitElements(GdlRule rule, List<GdlTerm> terms, IGdlArgumentListDeclaration parent) {
		for (int i = 0; i < terms.size(); i++) {
			GdlTerm term = terms.get(i);
			if (term instanceof GdlConstant) {
				addConstantOccurrence(parent, i, (GdlConstant) term);
			} else if (term instanceof GdlVariable) {
				addVariableOccurrence(rule, parent, i, (GdlVariable) term);
			} else if (term instanceof GdlFunction) {
				GdlFunction func = (GdlFunction) term;
				addFunctionOccurrence(rule, parent, i, func);
			}
		}
	}
	
	public void visitSentenceElements(GdlRule rule, GdlSentence sentence, IGdlArgumentListDeclaration parent) {
		visitElements(rule, sentence.getBody(), parent);
	}
	
//	public void visitSentenceElements(GdlRule rule, GdlSentence sentence) {
//		this.visitSentenceElements(rule, sentence, null);
//	}
	
	public void visitPredicateArguments(GdlRule rule, GdlRelation predicate) {
		this.visitSentenceElements(rule, predicate.toTerm().toSentence(), new GdlPredicateDeclaration(predicate) );
	}
	
	public void visitFunctionArguments(GdlRule rule, GdlFunction function) {
		this.visitSentenceElements(rule, function.toSentence(), new GdlFunctionDeclaration(function) );
	}

	/**********************************************/


	/**********************************************/

}
