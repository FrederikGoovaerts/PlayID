package fodot.gdl_parser;

import java.util.Map;
import java.util.Set;

import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.gdl_parser.firstphase.data.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlPredicateDeclaration;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlFodotData {

	public GdlFodotData(
			FodotType timeType,
			FodotType playerType,
			FodotType actionType,
			FodotType scoreType,
			FodotType allType,
			Map<GdlConstant, FodotConstant> constants,
			Map<GdlFunctionDeclaration, FodotFunctionDeclaration> functionDeclarations,
			Map<GdlPredicateDeclaration, FodotPredicateDeclaration> predicateDeclarations,
			Map<GdlRule, Map<GdlVariable, FodotVariable>> variablesPerRule,
			Set<GdlPredicateDeclaration> dynamicPredicates) {
		super();
		this.timeType = timeType;
		this.playerType = playerType;
		this.actionType = actionType;
		this.scoreType = scoreType;
		this.allType = allType;
		this.constants = constants;
		this.functionDeclarations = functionDeclarations;
		this.predicateDeclarations = predicateDeclarations;
		this.variablesPerRule = variablesPerRule;
		this.dynamicPredicates = dynamicPredicates;
	}

	/**********************************************
	 *  Types
	 ***********************************************/
	private FodotType timeType;
	private FodotType playerType;
	private FodotType actionType;
	private FodotType scoreType;
	private FodotType allType;

	public FodotType getTimeType() {
		return timeType;
	}
	public FodotType getPlayerType() {
		return playerType;
	}
	public FodotType getActionType() {
		return actionType;
	}
	public FodotType getScoreType() {
		return scoreType;
	}
	public FodotType getAllType() {
		return allType;
	}
	/**********************************************/


	/**********************************************
	 *  Mappings
	 ***********************************************/
	//Translations
	private Map<GdlConstant, FodotConstant> constants;
	private Map<GdlFunctionDeclaration, FodotFunctionDeclaration> functionDeclarations;
	private Map<GdlPredicateDeclaration, FodotPredicateDeclaration> predicateDeclarations;

	//Info
	private Map<GdlRule, Map<GdlVariable, FodotVariable>> variablesPerRule;
	private Set<GdlPredicateDeclaration> dynamicPredicates;

	public FodotConstant getConstant(GdlConstant constant) {
		return constants.get(constant);
	}

	public Map<GdlVariable, FodotVariable> getVariables(GdlRule rule) {
		return variablesPerRule.get(rule);
	}

	public FodotFunctionDeclaration getFunctionDeclaration(GdlFunction function) {
		GdlFunctionDeclaration declaration = new GdlFunctionDeclaration(function);	
		return functionDeclarations.get(declaration);
	}

	public FodotPredicateDeclaration getPredicateDeclaration(GdlRelation predicate) {
		GdlPredicateDeclaration declaration = new GdlPredicateDeclaration(predicate);		
		return predicateDeclarations.get(declaration);
	}

	public boolean isDynamic(GdlRelation predicate) {
		GdlPredicateDeclaration declaration = new GdlPredicateDeclaration(predicate);
		return dynamicPredicates.contains(declaration);
	}

	/**********************************************/


}
