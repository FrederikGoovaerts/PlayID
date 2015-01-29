package fodot.gdl_parser.firstphase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.Pair;
import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlLiteral;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.gdl_parser.firstphase.objects.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.firstphase.objects.declarations.GdlPredicateDeclaration;
import fodot.gdl_parser.firstphase.objects.declarations.GdlVariableDeclaration;
import fodot.gdl_parser.firstphase.objects.occurrences.GdlConstantOccurrence;
import fodot.gdl_parser.firstphase.objects.occurrences.GdlFunctionOccurrence;
import fodot.gdl_parser.firstphase.objects.occurrences.GdlPredicateOccurrence;
import fodot.gdl_parser.firstphase.objects.occurrences.GdlVariableOccurrence;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.FormulaUtil;

public class GdlTypeIdentifier {

	/***********************************************
	 *  Type Mappings
	 ***********************************************/
	private Map<GdlConstant, FodotType> constantTypes;

	//Variables can change between each rule, thus needs to be paired.
	private Map<GdlVariableDeclaration, FodotType> variableTypes;

	//Predicates have GdlConstants as name. There can be multiple predicates with the same name, thus arity is necessary
	private Map<GdlPredicateDeclaration, List<FodotType>> predicateArgumentTypes;	

	private Map<GdlFunctionDeclaration, FodotType> functionTypes;
	private Map<GdlFunctionDeclaration, List<FodotType>> functionArgumentTypes;	

	/**********************************************/

	/**********************************************
	 *  Predicate and constant occurings
	 ***********************************************/
	private Map<GdlConstant, List<GdlConstantOccurrence>> constantOccurings;
	private Map<GdlVariableDeclaration, List<GdlVariableOccurrence>> variableOccurings;
	private Map<GdlFunctionDeclaration, List<GdlFunctionOccurrence>> functionOccurings;
	private Map<GdlPredicateDeclaration, List<GdlPredicateOccurrence>> predicateOccurings;

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
		if (constantOccurings.get(constant) == null) {
			constantOccurings.put(constant, new ArrayList<GdlConstantOccurrence>());
		}
		constantOccurings.get(constant).add(new GdlConstantOccurrence(occurrencePlace));

		//Acknowledge the existence of the constant
		if (!constantTypes.containsKey(constant)) {
			constantTypes.put(constant, givenType);
		} else if (!givenType.equals(unfilled)) {
			updateConstantType(constant, givenType);
		}

	}
	
	private void addVariableOccurrence(GdlRule parentRule, GdlLiteral occurrencePlace, GdlVariable variable) {
		addVariableOccurrence(parentRule, occurrencePlace, variable, unfilled);
	}
	
	private void addVariableOccurrence(GdlRule parentRule, GdlLiteral occurrencePlace, GdlVariable argVariable, FodotType givenType) {

		GdlVariableDeclaration variable = new GdlVariableDeclaration(argVariable, parentRule);
		
		//Initialize list if first occurrence of constant
		if (variableOccurings.get(variable) == null) {
			variableOccurings.put(variable, new ArrayList<GdlVariableOccurrence>());
		}
		variableOccurings.get(argVariable).add(new GdlVariableOccurrence(occurrencePlace));

		//Acknowledge the existence of the constant
		if (!variableTypes.containsKey(variable)) {
			variableTypes.put(variable, givenType);
		} else if (!givenType.equals(unfilled)) {
			updateVariableType(variable, givenType);
		}

	}

	private void addPredicateOccurrence(GdlRule parentRule, GdlRelation predicate) {
		GdlPredicateDeclaration head = new GdlPredicateDeclaration( predicate );
		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilled, predicate.arity() );

		//Initialize list if first occurrence of the predicate
		if (predicateOccurings.get(head) == null) {
			predicateOccurings.put(head, new ArrayList<GdlPredicateOccurrence>());
		}
		predicateOccurings.get(head).add( new GdlPredicateOccurrence(parentRule, predicate) );

		//Acknowledge the existence of the predicate
		predicateArgumentTypes.put(head, argumentTypes);
	}
	
	private void addFunctionOccurrence(GdlRule parentRule, Gdl directParent, GdlFunction function) {
		GdlFunctionDeclaration head = new GdlFunctionDeclaration( function );
		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilled, function.arity() );


		//Initialize list if first occurrence of the predicate
		if (functionOccurings.get(head) == null) {
			functionOccurings.put(head, new ArrayList<GdlFunctionOccurrence>());
		}
		functionOccurings.get(head).add( new GdlFunctionOccurrence(parentRule, directParent, function) );

		//Acknowledge the existence of the predicate
		functionArgumentTypes.put(head, argumentTypes);
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

	private void updatePredicateType(Pair<GdlConstant, Integer> predicate, int argumentNr, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO
	}
	
	/**********************************************/


}
