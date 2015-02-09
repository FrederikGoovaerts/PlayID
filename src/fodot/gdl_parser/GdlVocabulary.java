package fodot.gdl_parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

import fodot.communication.gdloutput.IFodotGdlTranslator;
import fodot.exceptions.gdl.GdlTransformationException;
import fodot.gdl_parser.firstphase.data.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.firstphase.data.declarations.GdlPredicateDeclaration;
import fodot.objects.structure.elements.typenum.elements.FodoTypeFunctionEnumerationElement;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;

public class GdlVocabulary implements IFodotGdlTranslator {

	public GdlVocabulary(
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
		
		initialiseInverseMaps();
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
		return getPredicateDeclaration(declaration);
	}
	
	public FodotPredicateDeclaration getPredicateDeclaration(GdlPredicateDeclaration declaration) {
		return predicateDeclarations.get(declaration);
	}
	
	public Collection<GdlPredicateDeclaration> getPredicates() {
		return predicateDeclarations.keySet();
	}

	public boolean isDynamic(GdlRelation predicate) {
		GdlPredicateDeclaration declaration = new GdlPredicateDeclaration(predicate);
		return isDynamic(declaration);
	}
	
	public boolean isDynamic(GdlPredicateDeclaration declaration) {
		return dynamicPredicates.contains(declaration);
	}

	/**********************************************/

	/**********************************************
	 *  Translations
	 ***********************************************/
	
	private Map<FodotConstant, GdlConstant> constantsInverse = new HashMap<FodotConstant, GdlConstant>();
	private Map<FodotFunctionDeclaration, GdlFunctionDeclaration> functionsInverse = new HashMap<FodotFunctionDeclaration, GdlFunctionDeclaration>();
	private Map<FodotPredicateDeclaration, GdlPredicateDeclaration> predicatesInverse = new HashMap<FodotPredicateDeclaration, GdlPredicateDeclaration>();
	
	private void initialiseInverseMaps() {
		for (GdlConstant constant : constants.keySet()) {
			constantsInverse.put(constants.get(constant), constant);
		}

		for (GdlFunctionDeclaration func : functionDeclarations.keySet()) {
			functionsInverse.put(functionDeclarations.get(func), func);
		}
		
		for (GdlPredicateDeclaration pred : predicateDeclarations.keySet()) {
			predicatesInverse.put(predicateDeclarations.get(pred), pred);
		}
	}
	
	public GdlConstant getGdlConstant(FodotConstant constant) {
		return constantsInverse.get(constant);
	}
	
	public GdlFunctionDeclaration getGdlFunctionDeclaration(FodotFunctionDeclaration func) {
		return functionsInverse.get(func);
	}
	
	public GdlPredicateDeclaration getGdlPredicateDeclaration(FodotPredicateDeclaration pred) {
		return predicatesInverse.get(pred);
	}
	

	@Override
	public GdlTerm translate(IFodotTypeEnumerationElement fodot) {
		if (fodot instanceof FodotConstant) {
			FodotConstant casted = (FodotConstant) fodot;
			
			assert getGdlConstant(casted) != null;			
			return getGdlConstant(casted);
		}
		if (fodot instanceof FodoTypeFunctionEnumerationElement) {
			FodoTypeFunctionEnumerationElement casted = (FodoTypeFunctionEnumerationElement) fodot;
			FodotTypeFunctionDeclaration decl = casted.getDeclaration();
			GdlFunctionDeclaration gdlFunc = getGdlFunctionDeclaration(decl);
			GdlConstant name = gdlFunc.getName();
			
			List<GdlTerm> body = new ArrayList<GdlTerm>();
			for (IFodotTypeEnumerationElement el : casted.getElements()) {
				body.add(translate(el));
			}
			return GdlPool.getRelation(name, body).toTerm();
		}
		throw new GdlTransformationException("Can't translate " + fodot);
	}

	/**********************************************/


}
