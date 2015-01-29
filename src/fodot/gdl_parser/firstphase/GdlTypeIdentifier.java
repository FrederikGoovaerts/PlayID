package fodot.gdl_parser.firstphase;

import java.util.List;
import java.util.Map;

import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlLiteral;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlVariable;

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
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.FormulaUtil;

public class GdlTypeIdentifier {

	/**********************************************
	 *  Data maps
	 ***********************************************/
	private Map<GdlConstant, GdlConstantData> constants;
	private Map<GdlVariableDeclaration, GdlVariableData> variables;
	private Map<GdlPredicateDeclaration, GdlPredicateData> predicates;	
	private Map<GdlFunctionDeclaration, GdlFunctionData> functions;
	/**********************************************/


	/***********************************************
	 *  Types
	 ***********************************************/
	private FodotType unfilled = new FodotType("Unfilled");

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
	 *  GdlTransformer creator
	 ***********************************************/
	public GdlTransformer createTransformer() {
		return new GdlTypeIdentifierTransformer();
	}

	/**********************************************/

	
	
	
	
	/* =======================================
	 * ====== TRANSFORMER: THE VISITOR ======
	 * ======================================= */
	
	private class GdlTypeIdentifierTransformer implements GdlTransformer {

		@Override
		public void processRoleRelation(GdlRelation relation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processInitRelation(GdlRelation relation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processStaticPredicateRelation(GdlRelation relation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processLegalRelation(GdlRelation relation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processNextRule(GdlRule rule) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processLegalRule(GdlRule rule) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processGoalRule(GdlRule rule) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processTerminalRule(GdlRule rule) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processDefinitionRule(GdlRule rule) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
