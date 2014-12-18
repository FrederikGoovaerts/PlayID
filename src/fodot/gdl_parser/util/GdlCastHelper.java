package fodot.gdl_parser.util;

import fodot.gdl_parser.GdlFodotTransformer;
import fodot.gdl_parser.GdlTransformer;
import fodot.objects.sentence.IFodotSentenceElement;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import org.ggp.base.util.gdl.grammar.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        return createAnd();
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
            FodotPredicateDeclaration actionPred = new FodotPredicateDeclaration(
                    actionPredSentence.getName().getValue(),
                    types
            );
            FodotConstant action = createConstant(
                    buildActionPredicateRepresentation(
                            actionPred,
                            trans.getAllType()
                    ),
                    trans.getAllType()
            );
            List<IFodotSentenceElement> actionVariables = new ArrayList<>();
            for (GdlTerm gdlTerm : actionPredSentence.getBody()) {
                GdlSentence sentence = gdlTerm.toSentence();
                IFodotTerm actionVar;
                if(sentence.isGround()){
                    actionVar = createConstant(sentence.getName().getValue(),trans.getAllType());
                } else {
                    if(variables.containsKey(gdlTerm)){
                        actionVar = variables.get(gdlTerm);
                    } else {
                        FodotVariable temp = createVariable(sentence.getName().getValue(), trans.getAllType());
                        variables.put((GdlVariable) gdlTerm,temp);
                        actionVar = temp;
                    }
                }
                actionVariables.add(actionVar);
            }
            return createPredicate(
                    trans.getDoPredicate(),
                    createVariable("t",trans.getTimeType()),
                    createConstant(playerName,trans.getPlayerType()),
                    createPredicate(actionPred,actionVariables)
            );
        } else if(relation.getName().toString().equals("true")) {
            // process (true (*fluentpred*))
            GdlSentence fluentPredSentence = relation.get(0).toSentence();
            trans.processPredicate(fluentPredSentence);
            FodotPredicateDeclaration decl = trans.getPool().getPredicate(
                    fluentPredSentence.getName().getValue());

            List<IFodotSentenceElement> elements = new ArrayList<>();
            elements.add(createVariable("t",trans.getTimeType()));

            for (int i = 0; i < fluentPredSentence.arity(); i++) {
                GdlTerm term = fluentPredSentence.get(i);
                IFodotSentenceElement element;
                if(term.isGround()){
                    element = createConstant(term.toSentence().getName().getValue(),
                            trans.getAllType());
                } else {
                    if(variables.containsKey(term)){
                        element = variables.get(term);
                    } else {
                        FodotVariable temp = createVariable(
                                term.toSentence().getName().getValue(),
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

            List<IFodotSentenceElement> elements = new ArrayList<>();

            for (int i = 0; i < relation.arity(); i++) {
                GdlTerm term = relation.get(i);
                IFodotSentenceElement element;
                if(term.isGround()){
                    element = createConstant(term.toSentence().getName().getValue(),
                            trans.getAllType());
                } else {
                    if(variables.containsKey(term)){
                        element = variables.get(term);
                    } else {
                        FodotVariable temp = createVariable(
                                term.toSentence().getName().getValue(),
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
        }
    }

    private static String buildActionPredicateRepresentation(
            FodotPredicateDeclaration decl,
            FodotType allType){
        StringBuilder b = new StringBuilder();
        b.append(decl.getName() + "(");
        for (int i = 0; i < decl.getAmountOfArgumentTypes(); i++) {
            if(i<0) {
                b.append(",");
            }
            b.append(allType.getName());
        }
        b.append(")");
        return b.toString();
    }

    private static IFodotFormula generateDistinct(
            GdlDistinct distinct,
            HashMap<GdlVariable,FodotVariable> variables,
            GdlFodotTransformer trans) {
        //these are either constants or variables
        GdlSentence arg1 = distinct.getArg1().toSentence();
        GdlSentence arg2 = distinct.getArg2().toSentence();

        IFodotTerm arg1Fodot;
        IFodotTerm arg2Fodot;

        if(arg1.isGround()){
            arg1Fodot = createConstant(arg1.getName().getValue(),trans.getAllType());
        } else {
            if(variables.containsKey(distinct.getArg1())){
                arg1Fodot = variables.get(distinct.getArg1());
            } else {
                FodotVariable temp = createVariable(arg1.getName().getValue(), trans.getAllType());
                variables.put((GdlVariable) distinct.getArg1(),temp);
                arg1Fodot = temp;
            }
        }

        if(arg2.isGround()){
            arg2Fodot = createConstant(arg2.getName().getValue(),trans.getAllType());
        } else {
            if(variables.containsKey(distinct.getArg2())){
                arg2Fodot = variables.get(distinct.getArg2());
            } else {
                FodotVariable temp = createVariable(arg2.getName().getValue(), trans.getAllType());
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
