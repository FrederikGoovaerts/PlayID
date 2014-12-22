package fodot.gdl_parser.util;

import static fodot.helpers.FodotPartBuilder.createAnd;
import static fodot.helpers.FodotPartBuilder.createConstant;
import static fodot.helpers.FodotPartBuilder.createEquals;
import static fodot.helpers.FodotPartBuilder.createNot;
import static fodot.helpers.FodotPartBuilder.createPredicate;
import static fodot.helpers.FodotPartBuilder.createPredicateTerm;
import static fodot.helpers.FodotPartBuilder.createPredicateTermDeclaration;
import static fodot.helpers.FodotPartBuilder.createVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ggp.base.util.gdl.grammar.GdlDistinct;
import org.ggp.base.util.gdl.grammar.GdlLiteral;
import org.ggp.base.util.gdl.grammar.GdlNot;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.gdl_parser.GdlFodotTransformer;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

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
        }
        throw new UnsupportedOperationException("Not yet implemented. ( ͡° ͜ʖ ͡°)");
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
            String playerName = relation.get(0).toSentence().getName().getValue();
            GdlSentence actionPredSentence = relation.get(1).toSentence();
            List<FodotType> types = new ArrayList<>();
            for (int i = 0; i < actionPredSentence.arity(); i++) {
                types.add(trans.getAllType());
            }
            FodotPredicateTermDeclaration actionPred = createPredicateTermDeclaration(
                    actionPredSentence.getName().getValue(),
                    types,
                    trans.getActionType()
            );
//            FodotConstant action = createConstant(
//                    buildActionPredicateRepresentation(
//                            actionPred,
//                            trans.getAllType()
//                    ),
//                    trans.getActionType()
//            );
            FodotPredicateTermDeclaration actionTerm = createPredicateTermDeclaration(
                    actionPred.getName(),
                    FodotType.getSameTypeList(
                            actionPred.getAmountOfArgumentTypes(),
                            trans.getAllType()
                    ),
                    trans.getActionType()
            );
            List<IFodotTerm> actionVariables = new ArrayList<>();
            for (GdlTerm term : actionPredSentence.getBody()) {
                //GdlSentence sentence = gdlTerm.toSentence();
                IFodotTerm actionVar;
                if(term.isGround()){
                    actionVar = createConstant("c_" + term.toString(),trans.getAllType());
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
            return createPredicate(
                    trans.getDoPredicate(),
                    createVariable("t",trans.getTimeType()),
                    createConstant("p_" + playerName,trans.getPlayerType()),
                    createPredicateTerm(actionPred, actionVariables)
            );
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
                    element = createConstant("c_" + term.toSentence().getName().getValue(),
                            trans.getAllType());
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
            // process (*staticpred*)
            FodotPredicateDeclaration decl = trans.getPool().getPredicate(
                    relation.getName().getValue());

            List<IFodotTerm> elements = new ArrayList<>();

            for (int i = 0; i < relation.arity(); i++) {
                GdlTerm term = relation.get(i);
                IFodotTerm element;
                if(term.isGround()){
                    element = createConstant("c_" + term.toSentence().getName().getValue(),
                            trans.getAllType());
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
            arg1Fodot = createConstant("c_" + arg1.toString(),trans.getAllType());
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
            arg2Fodot = createConstant("c_" + arg2.toString(),trans.getAllType());
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
