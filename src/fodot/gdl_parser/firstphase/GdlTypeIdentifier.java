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
import org.ggp.base.util.gdl.grammar.GdlLiteral;
import org.ggp.base.util.gdl.grammar.GdlNot;
import org.ggp.base.util.gdl.grammar.GdlOr;
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
import fodot.gdl_parser.firstphase.data.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlPredicateDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlVariableDeclaration;
import fodot.gdl_parser.firstphase.data.occurrences.GdlConstantOccurrence;
import fodot.gdl_parser.firstphase.data.occurrences.GdlFunctionOccurrence;
import fodot.gdl_parser.firstphase.data.occurrences.GdlPredicateOccurrence;
import fodot.gdl_parser.firstphase.data.occurrences.GdlVariableOccurrence;
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
	private void addConstantOccurrence(GdlLiteral occurrencePlace, GdlConstant constant) {
		addConstantOccurrence(occurrencePlace, constant, unfilled);
	}

	private void addConstantOccurrence(GdlLiteral occurrencePlace, GdlConstant constant, FodotType givenType) {

		//Initialize list if first occurrence of constant
		if (constants.get(constant) == null) {
			constants.put(constant, new GdlConstantData(givenType));
		}
		constants.get(constant).addOccurences( new GdlConstantOccurrence(occurrencePlace) );

		//Edit typing if necessary
		if (!givenType.equals(unfilled)) {
			updateConstantType(constant, givenType);
		}

	}

	private void addVariableOccurrence(GdlRule parentRule, GdlLiteral occurrencePlace, GdlVariable variable) {
		addVariableOccurrence(parentRule, occurrencePlace, variable, unfilled);
	}

	private void addVariableOccurrence(GdlRule parentRule, GdlLiteral occurrencePlace, GdlVariable argVariable, FodotType givenType) {

		GdlVariableDeclaration variable = new GdlVariableDeclaration(argVariable, parentRule);

		//Initialize list if first occurrence of variable
		if (variables.get(variable) == null) {
			variables.put(variable, new GdlVariableData(givenType));
		}
		variables.get(argVariable).addOccurences( new GdlVariableOccurrence(occurrencePlace) );

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

	private void addFunctionOccurrence(GdlRule parentRule, Gdl directParent, GdlFunction function) {
		addFunctionOccurrence(parentRule, directParent, function, unfilled);
	}

	private void addFunctionOccurrence(GdlRule parentRule, Gdl directParent, GdlFunction function, FodotType givenType) {
		GdlFunctionDeclaration head = new GdlFunctionDeclaration( function );

		//Initialize list if first occurrence of the predicate
		if (functions.get(head) == null) {
			List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilled, function.arity() );
			functions.put(head, new GdlFunctionData(givenType, argumentTypes) );
		}
		functions.get(head).addOccurence( new GdlFunctionOccurrence(parentRule, directParent, function) );

		if (!givenType.equals(unfilled)) {
			updateFunctionType(head, givenType);
		}
	}

	/**********************************************/

	/**********************************************
	 *  Updating the typing of something
	 ***********************************************/
	private void updateConstantType(GdlConstant constant, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO
	}

	private void updateVariableType(GdlVariableDeclaration variable, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO

	}

	private void updateFunctionType(GdlFunctionDeclaration function, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO		
	}


	private void updatePredicateArgumentType(GdlPredicateDeclaration predicate, int argumentNr, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO
	}

	private void updateFunctionArgumentType(GdlFunctionDeclaration function, int argumentNr, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO
	}

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

		@Override
		public void processRoleRelation(GdlRelation relation) {
			// TODO

		}

		@Override
		public void processInitRelation(GdlRelation relation) {
			// TODO

		}

		@Override
		public void processStaticPredicateRelation(GdlRelation relation) {
			// TODO

		}

		@Override
		public void processLegalRelation(GdlRelation relation) {
			// TODO

		}

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
	}
	
	private class GdlRuleElementsVisitor extends GdlVisitor {
		//We will need this rule so we can tell what variables are the same.
		private GdlRule rule;
		
		public GdlRuleElementsVisitor(GdlRule rule) {
			super();
			this.rule = rule;
		}
		public void visitRelation(GdlRelation relation) {
			// TODO
		}
		public void visitProposition(GdlProposition proposition) {
			// TODO
		}
//		public void visitNot(GdlNot not) {
//			// TODO
//		}
		public void visitDistinct(GdlDistinct distinct) {
			// TODO
		}
		public void visitOr(GdlOr or) {
			// TODO
		}
		public void visitRule(GdlRule rule) {
			throw new GdlTypeIdentificationError("A rule occured in a rule, that's not possible, right?");
		}
		
	}
	
	/**
	 * This will visit the arguments of a predicate.
	 * @author Thomas Winters
	 */
	private class GdlPredicateVisitor extends GdlVisitor {
		private GdlRule rule;
		private GdlRelation predicate;
		
		public GdlPredicateVisitor(GdlRule rule, GdlRelation predicate) {
			super();
			this.rule = rule;
			this.predicate = predicate;
		}
		
		public void visitConstant(GdlConstant constant) {
			// TODO
		}
		public void visitVariable(GdlVariable variable) {
			// TODO
		}
		public void visitFunction(GdlFunction function) {
			// TODO
		}
		
	}

	/**
	 * This will visit the arguments of a function.
	 * @author Thomas Winters
	 */
	private class GdlFunctionVisitor extends GdlVisitor {
		private GdlRule rule;
		private GdlFunction function;
		
		public GdlFunctionVisitor(GdlRule rule, GdlFunction function) {
			super();
			this.rule = rule;
			this.function = function;
		}
		
		public void visitConstant(GdlConstant constant) {
			// TODO
		}
		public void visitVariable(GdlVariable variable) {
			// TODO
		}
		public void visitFunction(GdlFunction function) {
			// TODO
		}
		
	}
	/**********************************************/

}
