package fodot.gdl_parser;

import static fodot.objects.FodotPartBuilder.createAnd;
import static fodot.objects.FodotPartBuilder.createDistinct;
import static fodot.objects.FodotPartBuilder.createNot;
import static fodot.objects.FodotPartBuilder.createOr;
import static fodot.objects.FodotPartBuilder.createPredicate;
import static fodot.objects.FodotPartBuilder.createPredicateTerm;
import static fodot.objects.FodotPartBuilder.createPredicateTermDeclaration;
import static fodot.objects.FodotPartBuilder.createVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.gdl.grammar.GdlDistinct;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlLiteral;
import org.ggp.base.util.gdl.grammar.GdlNot;
import org.ggp.base.util.gdl.grammar.GdlOr;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlProposition;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

/**
 * Make one of these for each sentence you're going to translate.
 * This holds the variablemapping so you don't need to give it in the arguments every time
 * @author Thomas
 *
 */
public class GdlFodotSentenceTransformer {
	private GdlFodotTransformer trans;
	private Map<GdlVariable, FodotVariable> variableMap;
	
	public GdlFodotSentenceTransformer(GdlFodotTransformer trans,
			Map<GdlVariable, FodotVariable> variableMap) {
		super();
		this.trans = trans;
		setVariableMap(variableMap);
	}
	
	private void setVariableMap(Map<GdlVariable, FodotVariable> argVariableMap) {
		if (argVariableMap == null) {
			this.variableMap = new HashMap<GdlVariable, FodotVariable>();
		} else {
			this.variableMap = argVariableMap;
		}
	}

	public GdlFodotSentenceTransformer(GdlFodotTransformer trans) {
		this(trans, null);
	}

	
	
	
	public IFodotFormula generateFodotFormulaFrom(List<GdlLiteral> originalFormulas){
        List<IFodotFormula> resultingFormulas = new ArrayList<>();
        for (GdlLiteral literal : originalFormulas) {
            resultingFormulas.add(generateFodotFormula(literal));
        }
        return createAnd(resultingFormulas);
    }

    private IFodotFormula generateFodotFormula(GdlLiteral literal) {
        if(literal instanceof GdlDistinct){
            return generateDistinct((GdlDistinct) literal);
        } else if(literal instanceof GdlRelation){
            return generateRelation((GdlRelation) literal);
        } else if(literal instanceof GdlNot){
            return generateNot((GdlNot) literal);
        } else if (literal instanceof GdlProposition) {
            return generateProposition((GdlProposition) literal);
        } else if (literal instanceof GdlOr) {
        	return generateOr((GdlOr) literal);
        }
        throw new UnsupportedOperationException("Not yet implemented. ( ͡° ͜ʖ ͡°)");
    }

    private IFodotFormula generateProposition(
            GdlProposition literal) {
        // process (*compoundStatic* *vars*)

        FodotPredicateDeclaration decl = trans.processCompoundStaticPredicate(literal);

        List<IFodotTerm> elements = processSentenceArgumentsTimed(literal, decl);


        return createPredicate(
                trans.getPool().getCompoundTimedVerionOf(decl),
                elements
        );
    }


    private IFodotFormula generateNot(
            GdlNot not) {
        return createNot(generateFodotFormula(not.getBody()));
    }
    
    private IFodotFormula generateOr(
            GdlOr or) {
    	List<IFodotFormula> disjuncts = new ArrayList<IFodotFormula>();
    	for (GdlLiteral disj : or.getDisjuncts()) {
    		disjuncts.add( generateFodotFormula(disj) );
    	}
    	
        return createOr(disjuncts);
    }

    private IFodotFormula generateRelation(
            GdlRelation relation) {
        if(relation.getName().toString().equals("does")) {
        	
        	return generateDoes(relation);    
        	
        } else if(relation.getName().toString().equals("true")) {
        	
        	return generateTrue(relation);
        	
        } else {
            // process (*staticpred*) or (*compoundstaticpred*)

            List<IFodotTerm> elements = new ArrayList<>();

            String predName = relation.getName().getValue();

            FodotPredicateDeclaration decl;

            if(trans.getPool().isStaticPredicateRegistered(predName)) {
                 decl = trans.getPool().getPredicate(predName);
            } else if (trans.getPool().isCompoundStaticPredicateRegistered(predName)){
                 decl = trans.getPool().getCompoundTimedVerionOf(
                                trans.getPool().getCompoundStaticPredicate(predName)
                        );
                 elements.add(createTimeVariable()); 
            } else {
                decl = trans.getPool().getCompoundTimedVerionOf(trans.processCompoundStaticPredicate(relation));
                elements.add(createTimeVariable());
            }

            List<IFodotTerm> arguments = processSentenceArguments(relation, decl, elements.size());
            elements.addAll(arguments);

            return createPredicate(
                    decl,
                    elements
            );
        }
    }
    private IFodotFormula generateTrue(GdlRelation relation) {
        // process (true (*fluentpred*))
        GdlSentence fluentPredSentence = relation.get(0).toSentence();
        trans.processPredicate(fluentPredSentence);
        FodotPredicateDeclaration decl = 
        		trans.getPool().getTimedVerionOf(
        				trans.getPool().getPredicate(fluentPredSentence.getName().getValue()));

        List<IFodotTerm> elements = processSentenceArgumentsTimed(fluentPredSentence, decl);

        return createPredicate(
                decl,
                elements
        );
	}

	private IFodotFormula generateDoes(GdlRelation relation) {
        // process (does *player* *actionpred*)
    	
    	/*
    	 * NOTE: toSentence geeft exceptions wanneer je dit op een variabele doet!
    	 */
    	
		//ProcessPlayer
    	GdlTerm playerGdlTerm = relation.get(0);
    	IFodotTerm playerTerm;
    	if (playerGdlTerm instanceof GdlVariable) {
    		playerTerm = processTerm(playerGdlTerm, trans.getPlayerType());
    	} else {
    		String playerName = relation.get(0).toSentence().getName().getValue();
        	playerTerm = trans.convertRawRole(playerName);
    	}
    	
    	
    	//ProcessAction
    	GdlTerm actionGdlTerm = relation.get(1);
    	IFodotTerm actionFodotTerm;
    	if (actionGdlTerm instanceof GdlVariable) {
    		actionFodotTerm = processTerm(actionGdlTerm, trans.getActionType());
    	} else {
    		GdlSentence actionPredSentence = actionGdlTerm.toSentence();
    		FodotPredicateTermDeclaration actionTermDecl = createPredicateTermDeclaration(
    				actionPredSentence.getName().getValue(),
    				FodotType.getSameTypeList(
    						actionPredSentence.arity(),
    						trans.getAllType()
    						),
    						trans.getActionType()
    				);

    		List<IFodotTerm> actionVariables = processSentenceArguments(actionPredSentence, actionTermDecl);
    	
    		actionFodotTerm = createPredicateTerm(actionTermDecl, actionVariables);
    	}
    	
    	FodotVariable timeVariable = createTimeVariable();
    	
    	FodotPredicate actionPredicate = createPredicate(
				trans.getDoPredicateDeclaration(),
				timeVariable,
				playerTerm,
				actionFodotTerm
				);
		//TODO: uncomment this as soon as translations are working
		//        trans.addTranslation(actionPredicate, actionPredSentence.toString());
		return actionPredicate;
    
		
	}

	private IFodotFormula generateDistinct(GdlDistinct distinct) {
        //these are either constants or variables
        GdlTerm arg1 = distinct.getArg1();
        GdlTerm arg2 = distinct.getArg2();

        IFodotTerm arg1Fodot = processTerm(arg1);
        IFodotTerm arg2Fodot = processTerm(arg2);

        return createDistinct(
                    arg1Fodot,
                    arg2Fodot
        		);
    }
	
	/**********************************************
	 *  Term processing
	 ***********************************************/
	public IFodotTerm processTerm(GdlTerm term, FodotType argType) {
		IFodotTerm fodotTerm;

		if (term.isGround()) { //Term is a constant
			fodotTerm = trans.convertConstantName(term.toString(), argType);
		} else if(variableMap != null && variableMap.containsKey(term)) { //Term is already known in the variablemapping
			fodotTerm = variableMap.get(term);
			if (fodotTerm.getType() != argType && argType != trans.getAllType()) {
				((FodotVariable) fodotTerm).setType(argType); //TODO: Something should probably signal all other instances using this variable so they update their types!
			}
			
		} else if (term instanceof GdlVariable) { //Term is an new variable
			GdlVariable gdlVar = (GdlVariable) term;
			FodotVariable temp = createVariable(gdlVar.getName(), argType, new HashSet<FodotVariable>(variableMap.values()));
			if (variableMap != null) {
				variableMap.put(gdlVar, temp);
			}
			fodotTerm = temp;
		} else if (term instanceof GdlFunction){
			//TODO
			throw new IllegalStateException("Support for GDL functions is not implemented.\n"+term);
		} else {
			throw new IllegalStateException("Unsupported type of term term: " + term.getClass() + "["+term+"]");			
		}
		return fodotTerm;
	}

	public IFodotTerm processTerm(GdlTerm term) {
		return processTerm(term, trans.getAllType());
	}

	//We only allow one time variable per sentence
	private static final GdlVariable mockTimeGdlVar = GdlPool.getVariable("fodottime");
	public FodotVariable createTimeVariable() {
		FodotVariable timeVar;
		if (variableMap.containsKey(mockTimeGdlVar)) {
			timeVar = variableMap.get(mockTimeGdlVar);
		} else {
			timeVar = createVariable("t", trans.getTimeType(), new HashSet<FodotVariable>(variableMap.values()));
			variableMap.put(mockTimeGdlVar, timeVar);
		}
		return timeVar;
	}

	public FodotVariable createAllVariable() {
		FodotVariable result = createVariable("v", trans.getAllType(), new HashSet<FodotVariable>(variableMap.values()));
		
		//Reserve this name by adding it to the variablemap
		GdlVariable mockVariable = GdlPool.getVariable(result.getName());
		variableMap.put(mockVariable, result);
		return result;
	}
	
	public FodotVariable createActionVariable() {
		FodotVariable result = createVariable("act", trans.getActionType(), new HashSet<FodotVariable>(variableMap.values()));
		
		//Reserve this name by adding it to the variablemap
		GdlVariable mockVariable = GdlPool.getVariable(result.getName());
		variableMap.put(mockVariable, result);
		return result;
	}
	
	public FodotVariable createPlayerVariable() {
		FodotVariable result = createVariable("p", trans.getPlayerType(), new HashSet<FodotVariable>(variableMap.values()));
		
		//Reserve this name by adding it to the variablemap
		GdlVariable mockVariable = GdlPool.getVariable(result.getName());
		variableMap.put(mockVariable, result);
		return result;
	}
	
	/**********************************************
	 *  Processing of Sentence Arguments to List of Fodot Terms
	 ***********************************************/
	
	/**
	 * This method can process all GdlTerms of a GdlSentence and converts them to FodotTerms
	 * All variables will be added to the variablemap
	 * If a variablemap contains a variable who's type is more specific than the "all"-type,
	 * it will replace that type in th given FodotDeclaration
	 * @param sentence
	 * @param declaration
	 * @param variableMap
	 * @return
	 */
	public List<IFodotTerm> processSentenceArguments(
			GdlSentence sentence, FodotArgumentListDeclaration declaration,
			int argumentOffset) {

		List<IFodotTerm> elements = new ArrayList<IFodotTerm>(); 
		for (int i = 0; i < sentence.arity(); i++) {
			FodotType currentArgType = declaration.getArgumentType(i+argumentOffset);
			
			GdlTerm term = sentence.get(i);
			IFodotTerm element = processTerm(term, currentArgType);
			elements.add(element);

			//Improve declaration types (if variable was already mapped)
			FodotType actualType = element.getType();
			if (currentArgType != actualType && currentArgType == trans.getAllType()) {
				System.out.println("fixed " + declaration.getName() + " arg("+i+") to " + actualType);
				declaration.setArgumentType(i+argumentOffset, actualType);
			}


		}
		return elements;
	}
	
	public List<IFodotTerm> processSentenceArguments(
			GdlSentence sentence,FodotArgumentListDeclaration declaration) {
		return processSentenceArguments(sentence, declaration, 0);
	}
	
	public List<IFodotTerm> processSentenceArgumentsTimed(GdlSentence sentence,
			FodotArgumentListDeclaration declaration) {
		List<IFodotTerm> arguments = new ArrayList<IFodotTerm>();		
		arguments.add(createTimeVariable());
		arguments.addAll(processSentenceArguments(sentence, declaration, arguments.size()));
		return arguments;
	}
	
	public FodotPredicate createPredicateFromSentence(GdlSentence sentence,
			FodotPredicateDeclaration declaration) {
		return createPredicate(declaration, processSentenceArguments(sentence, declaration));
	}
	
	public FodotPredicate createTimedPredicateFromSentence(GdlSentence sentence,
			FodotPredicateDeclaration declaration) {
		return createPredicate(declaration, processSentenceArgumentsTimed(sentence, declaration));
	}
	
	/**********************************************/


}
