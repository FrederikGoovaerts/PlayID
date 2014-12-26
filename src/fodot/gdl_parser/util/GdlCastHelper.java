package fodot.gdl_parser.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ggp.base.util.gdl.grammar.*;

import fodot.gdl_parser.GdlFodotTransformer;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.formulas.argumented.FodotPredicate;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
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
            resultingFormulas.add(generateFodot(literal,variables,trans));
        }
        return createAnd(resultingFormulas);
    }

    private static IFodotFormula generateFodot(
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

        for (int i = 0; i < literal.arity(); i++) {
            GdlTerm term = literal.get(i);
            IFodotTerm element;
            if(term.isGround()){
                element = trans.convertRawConstantName(term.toSentence().getName().getValue());
            } else {
                if(variables.containsKey(term)){
                    element = variables.get(term);
                } else {
                    FodotVariable temp = createVariable(
                            trans.getAllType());
                    variables.put((GdlVariable) term,temp);
                    element = temp;
                }
            }
            elements.add(element);
        }

        return createPredicate(
                trans.getPool().getCompoundTimedVerionOf(decl),
                elements
        );
    }


    private static IFodotFormula generateNot(
            GdlNot not,
            HashMap<GdlVariable, FodotVariable> variables,
            GdlFodotTransformer trans) {
        return createNot(generateFodot(not.getBody(),variables,trans));
    }

    private static IFodotFormula generateRelation(
            GdlRelation relation,
            HashMap<GdlVariable, FodotVariable> variables,
            GdlFodotTransformer trans) {
        if(relation.getName().toString().equals("does")) {
            // process (does *player* *actionpred*)
        	
        	/*
        	 * NOTE: toSentence geeft exceptions wanneer je dit op een variabele doet!
        	 */
        	String playerName;
        	IFodotTerm playerTerm;
        	try {
        		playerName = relation.get(0).toSentence().getName().getValue();
        		playerTerm = trans.convertRawRole(playerName);
        	} catch (RuntimeException e) {
        		playerName = relation.get(0).toString();
        		playerTerm = createVariable(playerName, trans.getPlayerType());
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
            
            
            List<IFodotTerm> actionVariables = new ArrayList<>();
            for (GdlTerm term : actionPredSentence.getBody()) {
                //GdlSentence sentence = gdlTerm.toSentence();
                IFodotTerm actionVar;
                if(term.isGround()){
                    actionVar = trans.convertRawConstantName(term.toString());
                } else {
                    if(variables.containsKey(term)){
                        actionVar = variables.get(term);
                    } else {
                        FodotVariable temp = createVariable(trans.getAllType());
                        variables.put((GdlVariable) term,temp);
                        actionVar = temp;
                    }
                }
                actionVariables.add(actionVar);
            }
            FodotPredicate actionPredicate = createPredicate(
                    trans.getDoPredicate(),
                    createVariable("t",trans.getTimeType()),
                    playerTerm,
                    createPredicateTerm(actionTermDecl, actionVariables)
            );
            //TODO: uncomment this as soon as translations are working
//            trans.addTranslation(actionPredicate, actionPredSentence.toString());
            return actionPredicate;
        } else if(relation.getName().toString().equals("true")) {
            // process (true (*fluentpred*))
            GdlSentence fluentPredSentence = relation.get(0).toSentence();
            trans.processPredicate(fluentPredSentence);
            FodotPredicateDeclaration decl = trans.getPool().getPredicate(
                    fluentPredSentence.getName().getValue());

            List<IFodotTerm> elements = new ArrayList<>();
            elements.add(createVariable("t",trans.getTimeType()));

            for (int i = 0; i < fluentPredSentence.arity(); i++) {
                GdlTerm term = fluentPredSentence.get(i);
                IFodotTerm element;
                if(term.isGround()){
                    element = trans.convertRawConstantName(term.toSentence().getName().getValue());
                } else {
                    if(variables.containsKey(term)){
                        element = variables.get(term);
                    } else {
                        FodotVariable temp = createVariable(
                                trans.getAllType());
                        variables.put((GdlVariable) term,temp);
                        element = temp;
                    }
                }
                elements.add(element);
            }

            return createPredicate(
                    trans.getPool().getTimedVerionOf(decl),
                    elements
            );
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

            for (int i = 0; i < relation.arity(); i++) {
                GdlTerm term = relation.get(i);
                IFodotTerm element;
                if(term.isGround()){
                    element = trans.convertRawConstantName(term.toSentence().getName().getValue());
                } else {
                    if(variables.containsKey(term)){
                        element = variables.get(term);
                    } else {
                        FodotVariable temp = createVariable(
                                term.toString(),
                                trans.getAllType());
                        variables.put((GdlVariable) term,temp);
                        element = temp;
                    }
                }
                elements.add(element);
            }

            return createPredicate(
                    decl,
                    elements
            );
        }
    }
    private static IFodotFormula generateDistinct(
            GdlDistinct distinct,
            HashMap<GdlVariable,FodotVariable> variables,
            GdlFodotTransformer trans) {
        //these are either constants or variables
        GdlTerm arg1 = distinct.getArg1();
        GdlTerm arg2 = distinct.getArg2();

        IFodotTerm arg1Fodot;
        IFodotTerm arg2Fodot;

        if(arg1.isGround()){
            arg1Fodot = trans.convertRawConstantName(arg1.toString());
        } else {
            if(variables.containsKey(distinct.getArg1())){
                arg1Fodot = variables.get(distinct.getArg1());
            } else {
                FodotVariable temp = createVariable(arg1.toString(), trans.getAllType());
                variables.put((GdlVariable) distinct.getArg1(),temp);
                arg1Fodot = temp;
            }
        }

        if(arg2.isGround()){
            arg2Fodot = trans.convertRawConstantName(arg2.toString());
        } else {
            if(variables.containsKey(distinct.getArg2())){
                arg2Fodot = variables.get(distinct.getArg2());
            } else {
                FodotVariable temp = createVariable(arg2.toString(), trans.getAllType());
                variables.put((GdlVariable) distinct.getArg2(),temp);
                arg2Fodot = temp;
            }
        }

        return createNot(
                createEquals(
                        arg1Fodot,
                        arg2Fodot
                )
        );
    }

}
