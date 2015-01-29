package fodot.gdl_parser.firstphase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.Pair;
import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlLiteral;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.FormulaUtil;

public class GdlTypeIdentifier {

	/***********************************************
	 *  Type Mappings
	 ***********************************************/
	private Map<GdlConstant, FodotType> constantTypes;

	//Variables can change between each rule, thus needs to be paired.
	private Map<Pair<GdlVariable,GdlRule>, FodotType> variableTypes;

	//Predicates have GdlConstants as name. There can be multiple predicates with the same name, thus arity is necessary
	private Map<Pair<GdlConstant, Integer>, List<FodotType>> predicateArgumentTypes;	

	/**********************************************/

	/**********************************************
	 *  Predicate and constant occurings
	 ***********************************************/
	//TODO: predicateterms
	private Map<GdlConstant, List<GdlLiteral>> constantOccurings;
	private Map<Pair<GdlVariable,GdlRule>, List<GdlLiteral>> variableOccurings;
	private Map<Pair<GdlConstant, Integer>, List<Pair<GdlRelation,GdlRule>>> predicateOccurings;

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
			constantOccurings.put(constant, new ArrayList<GdlLiteral>());
		}
		constantOccurings.get(constant).add(occurrencePlace);

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

		Pair<GdlVariable, GdlRule> variable = Pair.of(argVariable, parentRule);
		
		//Initialize list if first occurrence of constant
		if (variableOccurings.get(variable) == null) {
			variableOccurings.put(variable, new ArrayList<GdlLiteral>());
		}
		variableOccurings.get(argVariable).add(occurrencePlace);

		//Acknowledge the existence of the constant
		if (!variableTypes.containsKey(variable)) {
			variableTypes.put(variable, givenType);
		} else if (!givenType.equals(unfilled)) {
			updateVariableType(variable, givenType);
		}

	}

	private void addPredicateOccurrence(GdlRule parentRule, GdlRelation predicate) {
		Pair<GdlConstant, Integer> head = Pair.of( predicate.getName(), predicate.arity() );
		List<FodotType> argumentTypes = FormulaUtil.createTypeList( unfilled, predicate.arity() );


		//Initialize list if first occurrence of the predicate
		if (predicateOccurings.get(head) == null) {
			predicateOccurings.put(head, new ArrayList<Pair<GdlRelation,GdlRule>>());
		}
		predicateOccurings.get(head).add( Pair.of(predicate,parentRule) );

		//Acknowledge the existence of the predicate
		predicateArgumentTypes.put(head, argumentTypes);
	}

	/**********************************************/

	/**********************************************
	 *  Updating the typing of something
	 ***********************************************/
	private void updateConstantType(GdlConstant constant, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO Auto-generated method stub		
	}

	private void updateVariableType(Pair<GdlVariable, GdlRule> variable, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO Auto-generated method stub
		
	}

	private void updatePredicateType(Pair<GdlConstant, Integer> predicate, int argumentNr, FodotType foundType) {
		assert !foundType.equals(unfilled);
		// TODO Auto-generated method stub		
	}

	
	/**********************************************/


}
