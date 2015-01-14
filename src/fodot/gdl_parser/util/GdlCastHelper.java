package fodot.gdl_parser.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ggp.base.util.gdl.grammar.*;

import fodot.gdl_parser.GdlFodotTransformer;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import static fodot.helpers.FodotPartBuilder.*;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlCastHelper {

    public static IFodotFormula generateFodotFormulaFrom(
            List<GdlLiteral> originalFormulas,
            HashMap<GdlVariable,FodotVariable> variables,
            GdlFodotTransformer trans){
        List<IFodotFormula> resultingFormulas = new ArrayList<>();
        for (GdlLiteral literal : originalFormulas) {
            resultingFormulas.add(generateFodotFormula(literal,variables,trans));
        }
        return createAnd(resultingFormulas);
    }

    private static IFodotFormula generateFodotFormula(
            GdlLiteral literal,
            HashMap<GdlVariable,FodotVariable> variables,
            GdlFodotTransformer trans) {
        if(literal instanceof GdlDistinct){
            return generateDistinct((GdlDistinct) literal,variables,trans);
        } else if(literal instanceof GdlRelation){
            return generateRelation((GdlRelation) literal, variables, trans);
        } else if(literal instanceof GdlNot){
            return generateNot((GdlNot) literal, variables, trans);
        } else if (literal instanceof GdlProposition) {
            return generateProposition((GdlProposition) literal, variables, trans);
        } else if (literal instanceof GdlOr) {
        	return generateOr((GdlOr) literal, variables, trans);
        }
        throw new UnsupportedOperationException("Not yet implemented. ( ͡° ͜ʖ ͡°)");
    }

    private static IFodotFormula generateProposition(
            GdlProposition literal,
            HashMap<GdlVariable, FodotVariable> variables,
            GdlFodotTransformer trans) {
        // process (*compoundStatic* *vars*)

        FodotPredicateDeclaration decl = trans.processCompoundStaticPredicate(literal);

        List<IFodotTerm> elements = new ArrayList<>();
        elements.add(createVariable("t",trans.getTimeType()));

        List<IFodotTerm> arguments = trans.processSentenceArguments(literal, decl, variables);
        elements.addAll(arguments);

        return createPredicate(
                trans.getPool().getCompoundTimedVerionOf(decl),
                elements
        );
    }


    private static IFodotFormula generateNot(
            GdlNot not,
            HashMap<GdlVariable, FodotVariable> variables,
            GdlFodotTransformer trans) {
        return createNot(generateFodotFormula(not.getBody(),variables,trans));
    }
    
    private static IFodotFormula generateOr(
            GdlOr or,
            HashMap<GdlVariable, FodotVariable> variables,
            GdlFodotTransformer trans) {
    	List<IFodotFormula> disjuncts = new ArrayList<IFodotFormula>();
    	for (GdlLiteral disj : or.getDisjuncts()) {
    		disjuncts.add( generateFodotFormula(disj, variables, trans) );
    	}
    	
        return createOr(disjuncts);
    }

    private static IFodotFormula generateRelation(
            GdlRelation relation,
            HashMap<GdlVariable, FodotVariable> variables,
            GdlFodotTransformer trans) {
        if(relation.getName().toString().equals("does")) {
        	
        	return generateDoes(relation, variables, trans);    
        	
        } else if(relation.getName().toString().equals("true")) {
        	
        	return generateTrue(relation, variables, trans);
        	
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
                elements.add(createVariable(trans.getTimeType()));
            } else {
                decl = trans.processCompoundStaticPredicate(relation);
            }

            List<IFodotTerm> arguments = trans.processSentenceArguments(relation, decl, variables);
            elements.addAll(arguments);

            return createPredicate(
                    decl,
                    elements
            );
        }
    }
    private static IFodotFormula generateTrue(GdlRelation relation,
			HashMap<GdlVariable, FodotVariable> variables,
			GdlFodotTransformer trans) {
        // process (true (*fluentpred*))
        GdlSentence fluentPredSentence = relation.get(0).toSentence();
        trans.processPredicate(fluentPredSentence);
        FodotPredicateDeclaration decl = trans.getPool().getPredicate(
                fluentPredSentence.getName().getValue());

        List<IFodotTerm> elements = new ArrayList<>();
        elements.add(createVariable("t",trans.getTimeType()));

        List<IFodotTerm> arguments = trans.processSentenceArguments(fluentPredSentence, decl, variables);
        elements.addAll(arguments);
        

        return createPredicate(
                trans.getPool().getTimedVerionOf(decl),
                elements
        );
	}

	private static IFodotFormula generateDoes(GdlRelation relation,
			HashMap<GdlVariable, FodotVariable> variables,
			GdlFodotTransformer trans) {
        // process (does *player* *actionpred*)
    	
    	/*
    	 * NOTE: toSentence geeft exceptions wanneer je dit op een variabele doet!
    	 */
    	
    	GdlTerm playerGdlTerm = relation.get(0);
    	IFodotTerm playerTerm;
    	if (playerGdlTerm instanceof GdlVariable) {
    		playerTerm = trans.processTerm(playerGdlTerm, trans.getPlayerType(), variables);
    	} else {
    		String playerName = relation.get(0).toSentence().getName().getValue();
        	playerTerm = trans.convertRawRole(playerName);
    	}
    	
        GdlSentence actionPredSentence = relation.get(1).toSentence();
        FodotPredicateTermDeclaration actionTermDecl = createPredicateTermDeclaration(
                actionPredSentence.getName().getValue(),
                FodotType.getSameTypeList(
                		actionPredSentence.arity(),
                        trans.getAllType()
                ),
                trans.getActionType()
        );
        
        
        List<IFodotTerm> actionVariables = trans.processSentenceArguments(actionPredSentence, actionTermDecl, variables);
        
        FodotPredicate actionPredicate = createPredicate(
                trans.getDoPredicate(),
                createVariable("t",trans.getTimeType()),
                playerTerm,
                createPredicateTerm(actionTermDecl, actionVariables)
        );
        //TODO: uncomment this as soon as translations are working
//        trans.addTranslation(actionPredicate, actionPredSentence.toString());
        return actionPredicate;
    
		
	}

	private static IFodotFormula generateDistinct(
            GdlDistinct distinct,
            HashMap<GdlVariable,FodotVariable> variables,
            GdlFodotTransformer trans) {
        //these are either constants or variables
        GdlTerm arg1 = distinct.getArg1();
        GdlTerm arg2 = distinct.getArg2();

        IFodotTerm arg1Fodot = trans.processTerm(arg1, variables);
        IFodotTerm arg2Fodot = trans.processTerm(arg2, variables);

        return createDistinct(
                    arg1Fodot,
                    arg2Fodot
        		);
    }

}
