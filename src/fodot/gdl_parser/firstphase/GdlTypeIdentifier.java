package fodot.gdl_parser.firstphase;

import static fodot.objects.FodotElementBuilder.createType;
import static fodot.objects.FodotElementBuilder.getNaturalNumberType;

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
import org.ggp.base.util.gdl.grammar.GdlProposition;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
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

	/**********************************************
	 *  Data maps
	 ***********************************************/
	private Map<GdlConstant, GdlConstantData> constants = new HashMap<GdlConstant, GdlConstantData>();
	private Map<GdlVariableDeclaration, GdlVariableData> variables = new HashMap<GdlVariableDeclaration, GdlVariableData>();
	private Map<GdlPredicateDeclaration, GdlPredicateData> predicates = new HashMap<GdlPredicateDeclaration, GdlPredicateData>();	
	private Map<GdlFunctionDeclaration, GdlFunctionData> functions = new HashMap<GdlFunctionDeclaration, GdlFunctionData>();
	/**********************************************/


	/***********************************************
	 *  Types
	 ***********************************************/
	private FodotType unfilled = createType("Unfilled");
	private FodotType timeType = createType("Time", getNaturalNumberType());
	private FodotType playerType = createType("Player");
	private FodotType actionType = createType("Action");
	private FodotType scoreType = createType("ScoreType", getNaturalNumberType());
	private FodotType allType = createType("All");

	/**********************************************/


	/**********************************************
	 *  Adding occurrences of elements
	 ***********************************************/
	private void addConstantOccurrence(Gdl directParent, int argumentIndex, GdlConstant constant) {
		addConstantOccurrence(directParent, argumentIndex, constant, unfilled);
	}

	private void addConstantOccurrence(Gdl directParent, int argumentIndex, GdlConstant constant, FodotType givenType) {

		//Initialize list if first occurrence of constant
		if (constants.get(constant) == null) {
			constants.put(constant, new GdlConstantData(givenType));
		}
		constants.get(constant).addOccurences( new GdlConstantOccurrence(directParent, argumentIndex) );

		//Edit typing if necessary
		if (!givenType.equals(unfilled)) {
			updateConstantType(constant, givenType);
		}

	}

	private void addVariableOccurrence(GdlRule parentRule, Gdl directParent, int argumentIndex, GdlVariable variable) {
		addVariableOccurrence(parentRule, directParent, argumentIndex, variable, unfilled);
	}

	private void addVariableOccurrence(GdlRule parentRule, Gdl directParent, int argumentIndex, GdlVariable argVariable, FodotType givenType) {

		GdlVariableDeclaration variable = new GdlVariableDeclaration(argVariable, parentRule);

		//Initialize list if first occurrence of variable
		if (variables.get(variable) == null) {
			variables.put(variable, new GdlVariableData(givenType));
		}
		variables.get(argVariable).addOccurences( new GdlVariableOccurrence(directParent, argumentIndex) );

		//Edit typing if necessary
		if (!givenType.equals(unfilled)) {
			updateVariableType(variable, givenType);
		}

	}

	private void addPredicateOccurrence(GdlRule parentRule, GdlRelation predicate) {
		GdlPredicateDeclaration head = new GdlPredicateDeclaration( predicate );

		//Initialize list if first occurrence of the predicate
		if (predicates.get(head) == null) {
			List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilled, predicate.arity() );
			predicates.put(head, new GdlPredicateData(argumentTypes));
		}
		predicates.get(head).addOccurence( new GdlPredicateOccurrence(parentRule, predicate) );

	}

	private void addFunctionOccurrence(GdlRule parentRule, Gdl directParent, int argumentIndex, GdlFunction function) {
		addFunctionOccurrence(parentRule, directParent, argumentIndex, function, unfilled);
	}

	private void addFunctionOccurrence(GdlRule parentRule, Gdl directParent, int argumentIndex, GdlFunction function, FodotType givenType) {
		GdlFunctionDeclaration head = new GdlFunctionDeclaration( function );

		//Initialize list if first occurrence of the predicate
		if (functions.get(head) == null) {
			List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilled, function.arity() );
			functions.put(head, new GdlFunctionData(givenType, argumentTypes) );
		}
		functions.get(head).addOccurence( new GdlFunctionOccurrence(parentRule, directParent, argumentIndex, function) );

		if (!givenType.equals(unfilled)) {
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
		GdlConstantData constantData = constants.get(argConstant);
		updateTermType(constantData, foundType);
	}

	private void updateVariableType(GdlVariableDeclaration argVariable, FodotType foundType) {
		GdlVariableData variableData = variables.get(argVariable);		
		updateTermType(variableData, foundType);
	}

	private void updateFunctionType(GdlFunctionDeclaration argFunction, FodotType foundType) {
		GdlFunctionData functionData = functions.get(argFunction);		
		updateTermType(functionData, foundType);
	}


	/**
	 * Helper method for updating the type of GDL terms.
	 * It also updates the type of arguments when knowing the type of constants, variables and functions
	 */
	private void updateTermType(IGdlTermData data, FodotType foundType) {
		assert !foundType.equals(unfilled);

		if (foundType.equals(data.getType())) {
			return;
		}

		//What if it already has a type?
		if (!data.getType().equals(unfilled)) {
			//TODO: add to both domains OR make a type that's a subtype of both, we'll see!
			throw new GdlTypeIdentificationError("Type collision");
		}

		//Set the new type
		data.setType(foundType);

		//Update all occurrences
		for (IGdlTermOccurrence occ : data.getOccurences()) {
			Gdl parent = occ.getDirectParent();
			if (parent instanceof GdlRelation) {
				updatePredicateArgumentType(new GdlPredicateDeclaration((GdlRelation) parent), occ.getArgumentIndex(), foundType);
			} else if (parent instanceof GdlFunction) {
				updatePredicateArgumentType(new GdlPredicateDeclaration((GdlRelation) parent), occ.getArgumentIndex(), foundType);				
			} else if (parent instanceof GdlDistinct) {
				// Do nothing, we don't know anything new about the typing
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
		GdlPredicateData data = predicates.get(predicate);
		updateArgumentListArgumentType(data, argumentNr, foundType);
	}

	private void updateFunctionArgumentType(GdlFunctionDeclaration function, int argumentNr, FodotType foundType) {
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
		assert !foundType.equals(unfilled);

		//Return if already set
		if (foundType.equals(data.getArgumentType(argumentNr))) {
			return;
		}

		//What if it already has a type?
		if (!data.getArgumentType(argumentNr).equals(unfilled)) {
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


	/**********************************************/




	/**********************************************
	 *  Resulting GdlFodotData generator
	 ***********************************************/
	public GdlFodotData getResultingData() {
		Map<GdlConstant, FodotConstant> constants = new HashMap<GdlConstant, FodotConstant>();
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
				constants, functionDeclarations, predicateDeclarations,
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
		 *  Relation processing: only for type defining
		 ***********************************************/

		@Override
		public void processRoleRelation(GdlRelation relation) {
			visitRelationArguments(relation);
			updateTermTypeInRelation(relation.get(0), playerType);
		}

		@Override
		public void processInitRelation(GdlRelation relation) {
			visitRelationArguments(relation);
		}

		@Override
		public void processStaticPredicateRelation(GdlRelation relation) {
			visitRelationArguments(relation);
		}

		@Override
		public void processLegalRelation(GdlRelation relation) {
			visitRelationArguments(relation);
			updateTermTypeInRelation(relation.get(0), playerType);
			updateTermTypeInRelation(relation.get(0), actionType);
		}		

		private void updateTermTypeInRelation(GdlTerm term, FodotType foundType) {
			if (term instanceof GdlConstant) {
				updateConstantType((GdlConstant) term, foundType);
			} else if (term instanceof GdlFunction) {
				updateFunctionType(new GdlFunctionDeclaration((GdlFunction) term), foundType);
			} else if (term instanceof GdlVariable) {
				throw new GdlTypeIdentificationError("Variables can't be processed in relations");
			}
		}

		private void visitRelationArguments(GdlRelation relation) {
			addPredicateOccurrence(null, relation);
			
			//Rule can be null: only used for variables, but it doesn't contain variables!
			GdlPredicateVisitor visitor = new GdlPredicateVisitor(null, relation);
			visitor.visitElements();
		}
		/**********************************************/

		/**********************************************
		 *  Rule processing: it can 
		 ***********************************************/

		@Override
		public void processNextRule(GdlRule rule) {
			// TODO

		}

		@Override
		public void processLegalRule(GdlRule rule) {
			// TODO

		}

		@Override
		public void processGoalRule(GdlRule rule) {
			// TODO

		}

		@Override
		public void processTerminalRule(GdlRule rule) {
			// TODO

		}

		@Override
		public void processDefinitionRule(GdlRule rule) {
			// TODO

		}		

		/**********************************************/
	}

	private class GdlRelationElementsVisitor extends GdlVisitor {

	}


	private class GdlRuleElementsVisitor extends GdlVisitor {
		//We will need this rule so we can tell what variables are the same.
		private GdlRule rule;

		public GdlRuleElementsVisitor(GdlRule rule) {
			super();
			this.rule = rule;
		}

		public void visitRelation(GdlRelation predicate) {
			GdlPredicateVisitor predicateArgumentsVisitor = new GdlPredicateVisitor(rule, predicate);
			predicateArgumentsVisitor.visitElements();
		}
		public void visitFunction(GdlFunction function) {
			GdlFunctionVisitor functionArgumentsVisitor = new GdlFunctionVisitor(rule, function);
			functionArgumentsVisitor.visitElements();
		}

		public void visitProposition(GdlProposition proposition) {
			// Do nothing?
		}
		
		public void visitDistinct(GdlDistinct distinct) {
			// Do nothing?
		}
		
		public void visitRule(GdlRule rule) {
			throw new GdlTypeIdentificationError("A rule occured in a rule, that's not possible, right?");
		}

	}

	/**
	 * This will visit the arguments of a predicate.
	 * @author Thomas Winters
	 */
	private class GdlPredicateVisitor {
		private GdlRule rule;
		private GdlRelation predicate;

		public GdlPredicateVisitor(GdlRule rule, GdlRelation predicate) {
			this.rule = rule;
			this.predicate = predicate;
		}

		public void visitElements() {
			for (int i = 0; i < predicate.arity(); i++) {
				GdlTerm term = predicate.get(i);
				if (term instanceof GdlConstant) {
					addConstantOccurrence(predicate, i, (GdlConstant) term);
				} else if (term instanceof GdlVariable) {
					addVariableOccurrence(rule, predicate, i, (GdlVariable) term);
				} else if (term instanceof GdlFunction) {
					addFunctionOccurrence(rule, predicate, i, (GdlFunction) term);
				}
			}
		}

	}

	/**
	 * This will visit the arguments of a function.
	 * @author Thomas Winters
	 */
	private class GdlFunctionVisitor {
		private GdlRule rule;
		private GdlFunction function;

		public GdlFunctionVisitor(GdlRule rule, GdlFunction function) {
			this.rule = rule;
			this.function = function;
		}

		public void visitElements() {
			for (int i = 0; i < function.arity(); i++) {
				GdlTerm term = function.get(i);
				if (term instanceof GdlConstant) {
					addConstantOccurrence(function, i, (GdlConstant) term);
				} else if (term instanceof GdlVariable) {
					addVariableOccurrence(rule, function, i, (GdlVariable) term);
				} else if (term instanceof GdlFunction) {
					addFunctionOccurrence(rule, function, i, (GdlFunction) term);
				}
			}
		}

	}
	/**********************************************/

}
