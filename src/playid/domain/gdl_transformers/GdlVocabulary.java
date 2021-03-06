package playid.domain.gdl_transformers;

import java.util.*;

import org.ggp.base.util.gdl.grammar.*;

import playid.domain.communication.gdloutput.IFodotGdlTranslator;
import playid.domain.exceptions.gdl.GdlTransformationException;
import playid.domain.fodot.structure.elements.IFodotStructureElement;
import playid.domain.fodot.structure.elements.typeenum.elements.FodotInteger;
import playid.domain.fodot.structure.elements.typeenum.elements.FodotTypeFunctionEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.theory.elements.terms.FodotConstant;
import playid.domain.fodot.theory.elements.terms.FodotVariable;
import playid.domain.fodot.vocabulary.elements.FodotFunctionDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotPredicateDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.FodotTypeFunctionDeclaration;
import playid.domain.gdl_transformers.first_phase.data.declarations.GdlFunctionDeclaration;
import playid.domain.gdl_transformers.first_phase.data.declarations.GdlPredicateDeclaration;
import playid.util.CollectionPrinter;

/**
 * Class that contains info from the firstphase of the translation from GDL to FO(.)
 * @author Thomas Winters
 *
 */
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
            Map<GdlProposition,FodotPredicateDeclaration> propositions,
			Set<GdlPredicateDeclaration> dynamicPredicates,
			Collection<? extends IFodotStructureElement> structureElements
			) {
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
        this.propositions = propositions;
		this.structureElements = new LinkedHashSet<IFodotStructureElement>(structureElements);
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
		return dynamicPredicates.contains(declaration) || propositions.containsValue(declaration);
	}
	
	public Set<FodotPredicateDeclaration> getDynamicPredicates() {
		Set<FodotPredicateDeclaration> result = new LinkedHashSet<FodotPredicateDeclaration>();
		for (GdlPredicateDeclaration gdlPred : dynamicPredicates) {
			result.add(getPredicateDeclaration(gdlPred));
		}
		return result;
	}

    private Map<GdlProposition,FodotPredicateDeclaration> propositions;

    public FodotPredicateDeclaration getPropositionDeclaration(GdlProposition proposition){
        return propositions.get(proposition);
    }


    public GdlProposition getProposition(GdlConstant name) {
        for (GdlProposition proposition : propositions.keySet()) {
            if (proposition.getName().equals(name)){
                return proposition;
            }
        }
        throw new GdlTransformationException("Given constant has no matching proposition!");
    }


	
	//Direct info for copying:

	
	public Map<GdlConstant, Map<FodotType, FodotConstant>> getConstants() {
		return constants;
	}
	public FodotVariable getVariable(GdlRule rule, GdlVariable variable) {
		return getVariables(rule).get(variable);
	}	
	public Map<GdlFunctionDeclaration, FodotTypeFunctionDeclaration> getFunctionDeclarations() {
		return functionDeclarations;
	}
	public Map<GdlPredicateDeclaration, FodotPredicateDeclaration> getPredicateDeclarations() {
		return predicateDeclarations;
	}
	public Map<GdlRule, Map<GdlVariable, FodotVariable>> getVariablesPerRule() {
		return variablesPerRule;
	}
    public Map<GdlProposition,FodotPredicateDeclaration> getPropositions() {
        return propositions;
    }
	public Set<GdlPredicateDeclaration> getGdlDynamicPredicates() {
		return dynamicPredicates;
	}

	/**********************************************/

	/**********************************************
	 *  Structure info
	 ***********************************************/
	Set<IFodotStructureElement> structureElements = new LinkedHashSet<IFodotStructureElement>();
	
	public Set<IFodotStructureElement> getStructureElements() {
		return new LinkedHashSet<IFodotStructureElement>(this.structureElements);
	}
	
	public boolean containsStructureElements() {
		return !this.structureElements.isEmpty();
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
		if (fodot instanceof FodotInteger) {
			FodotInteger casted = (FodotInteger) fodot;

			assert getGdlConstant(casted.getConstant()) != null;			
			return getGdlConstant(casted.getConstant());
		}
		if (fodot instanceof FodotTypeFunctionEnumerationElement) {			
			FodotTypeFunctionEnumerationElement casted = (FodotTypeFunctionEnumerationElement) fodot;
			
			if (casted.getElements().isEmpty()) {
				return getGdlConstant(new FodotConstant(casted.getDeclaration().getName(), casted.getDeclaration().getType()));
			}
			
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
		if (containsStructureElements()) {
			builder.append("\n== Structure elements ==\n");
		}
		for (IFodotStructureElement strucEl : getStructureElements()) {
			builder.append(strucEl.toCode()+"\n");				
		}
		builder.append("====================\n");
		return builder.toString();
	}

}
