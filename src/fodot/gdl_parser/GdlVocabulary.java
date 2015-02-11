package fodot.gdl_parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
import fodot.gdl_parser.first_phase.data.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.first_phase.data.declarations.GdlPredicateDeclaration;
import fodot.objects.structure.elements.typeenum.elements.FodoTypeFunctionEnumerationElement;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.util.CollectionPrinter;

public class GdlVocabulary implements IFodotGdlTranslator {

	public GdlVocabulary(
			FodotType timeType,
			FodotType playerType,
			FodotType actionType,
			FodotType scoreType,
			Collection<FodotType> otherTypes,
			Map<GdlConstant, Map<FodotType, FodotConstant>> constants,
			Map<GdlRule, Map<GdlVariable, FodotVariable>> variablesPerRule,
			Map<GdlFunctionDeclaration, FodotTypeFunctionDeclaration> functionDeclarations,
			Map<GdlPredicateDeclaration, FodotPredicateDeclaration> predicateDeclarations,
			Set<GdlPredicateDeclaration> dynamicPredicates) {
		super();
		this.timeType = timeType;
		this.playerType = playerType;
		this.actionType = actionType;
		this.scoreType = scoreType;
		this.otherTypes = otherTypes;
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
	private Collection<FodotType> otherTypes;

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
	public Collection<FodotType> getOtherTypes() {
		return otherTypes;
	}
	/**********************************************/


	/**********************************************
	 *  Mappings
	 ***********************************************/
	//Translations
	private Map<GdlConstant, Map<FodotType, FodotConstant>> constants;
	private Map<GdlFunctionDeclaration, FodotTypeFunctionDeclaration> functionDeclarations;
	private Map<GdlPredicateDeclaration, FodotPredicateDeclaration> predicateDeclarations;

	//Info
	private Map<GdlRule, Map<GdlVariable, FodotVariable>> variablesPerRule;
	private Set<GdlPredicateDeclaration> dynamicPredicates;

	public FodotConstant getConstant(GdlConstant constant, FodotType type) {
		if (type == null) {
			//TODO doe iets
			return constants.get(constant).values().iterator().next();
		}
		return constants.get(constant).get(type);
	}

	public Map<GdlVariable, FodotVariable> getVariables(GdlRule rule) {
		return variablesPerRule.get(rule);
	}

	public FodotVariable getVariable(GdlRule rule, GdlVariable variable) {
		return getVariables(rule).get(variable);
	}

	public FodotTypeFunctionDeclaration getFunctionDeclaration(GdlFunction function) {
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

	public Collection<GdlPredicateDeclaration> getGdlPredicates() {
		return predicateDeclarations.keySet();
	}

	public Collection<FodotPredicateDeclaration> getFodotPredicates() {
		return predicateDeclarations.values();
	}
	
	public boolean isDynamic(GdlRelation predicate) {
		GdlPredicateDeclaration declaration = new GdlPredicateDeclaration(predicate);
		return isDynamic(declaration);
	}

	public boolean isDynamic(GdlPredicateDeclaration declaration) {
		return dynamicPredicates.contains(declaration);
	}
	
	public Set<FodotPredicateDeclaration> getDynamicPredicates() {
		Set<FodotPredicateDeclaration> result = new LinkedHashSet<FodotPredicateDeclaration>();
		for (GdlPredicateDeclaration gdlPred : dynamicPredicates) {
			result.add(getPredicateDeclaration(gdlPred));
		}
		return result;
	}

	/**********************************************/

	/**********************************************
	 *  Translations
	 ***********************************************/

	private Map<FodotConstant, GdlConstant> constantsInverse = new HashMap<FodotConstant, GdlConstant>();
	private Map<FodotFunctionDeclaration, GdlFunctionDeclaration> functionsInverse = new HashMap<FodotFunctionDeclaration, GdlFunctionDeclaration>();
	private Map<FodotPredicateDeclaration, GdlPredicateDeclaration> predicatesInverse = new HashMap<FodotPredicateDeclaration, GdlPredicateDeclaration>();

	private void initialiseInverseMaps() {
		for (GdlConstant gdlConstant : constants.keySet()) {
			Map<FodotType, FodotConstant> map = constants.get(gdlConstant);
			for (FodotConstant foConstant : map.values()) {
				constantsInverse.put(foConstant, gdlConstant);
			}
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


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("== GDL VOCABULARY ==\n");
		builder.append(CollectionPrinter.printStringList("", "\n\n", "\n", CollectionPrinter.toCode(predicateDeclarations.values())));		

		builder.append(timeType.getDeclaration().toCode()+"\n");
		builder.append(playerType.getDeclaration().toCode()+"\n");
		builder.append(actionType.getDeclaration().toCode()+"\n");
		builder.append(scoreType.getDeclaration().toCode()+"\n");
		for (FodotType t : otherTypes) {
			builder.append(t.getDeclaration().toCode()+"\n");			
		}
		builder.append("====================\n");
		return builder.toString();
	}

}
