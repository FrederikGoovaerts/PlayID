package fodot.gdl_parser.second_phase;

import static fodot.objects.FodotElementBuilder.createAnd;
import static fodot.objects.FodotElementBuilder.createDistinct;
import static fodot.objects.FodotElementBuilder.createFunction;
import static fodot.objects.FodotElementBuilder.createNot;
import static fodot.objects.FodotElementBuilder.createOr;
import static fodot.objects.FodotElementBuilder.createPredicate;
import static fodot.objects.FodotElementBuilder.createVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.gdl.grammar.GdlConstant;
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

import fodot.exceptions.gdl.GdlTransformationException;
import fodot.gdl_parser.first_phase.data.declarations.GdlPredicateDeclaration;
import fodot.gdl_parser.util.VariableRegisterer;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotFunction;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.util.FormulaUtil;

/**
 * Make one of these for each sentence you're going to translate.
 * This holds the variablemapping so you don't need to give it in the arguments every time
 */
public class GdlFodotSentenceTransformer {
	private GdlFodotTransformer trans;
	private VariableRegisterer variableReg;

	/**********************************************
	 *  Constructors
	 ***********************************************/

	public GdlFodotSentenceTransformer(GdlFodotTransformer trans,
			VariableRegisterer variableMap) {
		setTransformer(trans);
		setVariableRegisterer(variableMap);
	}

	public GdlFodotSentenceTransformer(GdlFodotTransformer trans,
			Map<GdlVariable, FodotVariable> variables) {
		this(trans, new VariableRegisterer(variables));
	}

	public GdlFodotSentenceTransformer(GdlFodotTransformer trans) {
		this(trans, new VariableRegisterer());
	}
	/**********************************************/

	/**********************************************
	 *  Setters
	 ***********************************************/

	private void setTransformer(GdlFodotTransformer argTrans) {
		this.trans = argTrans;
	}

	private void setVariableRegisterer(VariableRegisterer argVariableReg) {
		if (argVariableReg == null) {
			this.variableReg = new VariableRegisterer();
		} else {
			this.variableReg = argVariableReg;
		}
	}

	/**********************************************/

	/**********************************************
	 *  Formula transformers
	 ***********************************************/
	public IFodotFormula generateFodotFormulaFrom(List<GdlLiteral> originalFormulas){
		List<IFodotFormula> resultingFormulas = new ArrayList<>();
		for (GdlLiteral literal : originalFormulas) {
			IFodotFormula formula = generateFodotFormula(literal);
			if (formula != null) {
				resultingFormulas.add(formula);
			}
		}
		if (resultingFormulas.isEmpty()) {
			return null;
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

		//		FodotPredicateDeclaration decl = trans.processCompoundStaticPredicate(literal);
		//		FodotPredicateDeclaration timedDecl = trans.getPool().getCompoundTimedVerionOf(decl);

		FodotPredicateDeclaration declaration = 
				trans.getGdlVocabulary().getPredicateDeclaration(
						new GdlPredicateDeclaration(literal.getName(), literal.arity()));

		return generateTimedPredicate(literal, declaration);
	}


	private IFodotFormula generateNot(
			GdlNot not) {
		IFodotFormula formula = generateFodotFormula(not.getBody());
		if (formula == null) {
			return null;
		}
		return createNot(formula);
	}

	private IFodotFormula generateOr(
			GdlOr or) {
		List<IFodotFormula> disjuncts = new ArrayList<IFodotFormula>();
		for (GdlLiteral disj : or.getDisjuncts()) {
			IFodotFormula formula = generateFodotFormula(disj);
			if (formula != null) {
				disjuncts.add( formula );
			}
		}

		return createOr(disjuncts);
	}

	public FodotPredicate generateRelation(
			GdlRelation relation) {

		String relationName = relation.getName().getValue();

		switch (relationName) {
		case "does":
			return generateDoes(relation);
		case "true":
			return generateTrue(relation);
		case "role":
			/* Role is already translated to elements of playertype:
			 * the inner variable will have the correct type already */
			return generateRole(relation);
		case "legal":
            return generateLegal(relation);
		default:
			// process (*staticpred*) or (*compoundstaticpred*) or (*proposition*)

            if(relation.arity() == 0){
                GdlProposition prop = this.trans.getGdlVocabulary().getProposition(relation.getName());
                return createPredicate(
                        this.trans.getGdlVocabulary().getPropositionDeclaration(prop),
                        createTimeVariable()
                );
            } else {

                GdlPredicateDeclaration gdlDeclaration = new GdlPredicateDeclaration(relation);


                FodotPredicateDeclaration decl = trans.getGdlVocabulary().getPredicateDeclaration(gdlDeclaration);

                List<IFodotTerm> arguments;

                if (relation.arity() == 0) {
                    arguments = new ArrayList<>();
                } else {
                    arguments = generateTerms(
                            relation.getBody(),
                            FormulaUtil.removeTypes(decl.getArgumentTypes(), trans.getTimeType())
                    );
                }
                if (trans.getGdlVocabulary().isDynamic(relation)) {
                    arguments.add(0, createTimeVariable());
                }

                return createPredicate(decl, arguments);
            }
		}
	}

	private FodotPredicate generateRole(GdlRelation relation) {
		return createPredicate(
				trans.getPlayerType().getTypePredicateDeclaration(),
				generateTerm(relation.getBody().get(0), trans.getPlayerType()));
	}

	private FodotPredicate generateTrue(GdlRelation relation) {

		GdlSentence innerPredicate = relation.get(0).toSentence();
		return generateRelation( GdlPool.getRelation(innerPredicate.getName(), innerPredicate.getBody()) );
	}

	private FodotPredicate generateDoes(GdlRelation relation) {

		List<IFodotTerm> arguments = generateTerms(relation.getBody(), Arrays.asList(trans.getPlayerType(), trans.getActionType()));
		arguments.add(0, createTimeVariable());

		FodotPredicate actionPredicate = createPredicate(
				trans.getDoPredicateDeclaration(),
				arguments);
		return actionPredicate;
	}

    private FodotPredicate generateLegal(GdlRelation relation) {

        List<IFodotTerm> arguments = generateTerms(relation.getBody(), Arrays.asList(trans.getPlayerType(), trans.getActionType()));
        arguments.add(0, createTimeVariable());

        FodotPredicate legalPredicate = createPredicate(
                trans.getLegalmovePredicateDeclaration(),
                arguments);
        return legalPredicate;
    }

	private IFodotFormula generateDistinct(GdlDistinct distinct) {
		//these are either constants or variables
		GdlTerm arg1 = distinct.getArg1();
		GdlTerm arg2 = distinct.getArg2();

		IFodotTerm arg1Fodot = generateTerm(arg1, null);
		IFodotTerm arg2Fodot = generateTerm(arg2, null);

		return createDistinct(
				arg1Fodot,
				arg2Fodot
				);
	}
	/**********************************************/


	/**********************************************
	 *  Term processing
	 ***********************************************/
	public List<IFodotTerm> generateTerms(List<GdlTerm> terms, List<FodotType> types) {
		List<IFodotTerm> result = new ArrayList<>();
		for (int i = 0; i < terms.size(); i++) {
			FodotType type = types == null ? null : types.get(i);
			result.add(generateTerm(terms.get(i), type));
		}
		return result;
	}

	public IFodotTerm generateTerm(GdlTerm term, FodotType type) {
		if (term instanceof GdlConstant) {
			return generateConstant((GdlConstant) term, type);
		} else if (term instanceof GdlVariable) {
			return generateVariable((GdlVariable) term);
		} else if (term instanceof GdlFunction){
			return generateFunction((GdlFunction) term);
		}
		throw new IllegalStateException("Unsupported type of term term: " + term.getClass() + "["+term+"]");			

	}

	//	public IFodotTerm generateTerm(GdlTerm term) {
	//		return generateTerm(term, trans.getAllType());
	//	}

	public FodotConstant generateConstant(GdlConstant gdlConst, FodotType type) {
		FodotConstant result =  trans.getGdlVocabulary().getConstant(gdlConst, type);
		if (result == null) {
			throw new GdlTransformationException("Not known constant and type: " + gdlConst + ", " + type);
		}
		return result;
	}
	//	
	public FodotVariable generateVariable(GdlVariable gdlVar) {	
		//		FodotVariable toReturn;
		//Check if the variable is already known in the variablemapping
		if (variableReg.hasTranslationFor(gdlVar)) { 
			return variableReg.translate(gdlVar);

			//Check if the given type matches the type of the already known variable
			//			if (toReturn.getType() != argType && argType != trans.getAllType()) {
			//				((FodotVariable) toReturn).setType(argType);
			//TODO: Something should probably signal all other instances using this variable so they update their types!
			//			}
		}// else { //Term is an new variable
		throw new GdlTransformationException("A new variable has occurred in the second phase. This is the job of the first phase!");
		//			FodotVariable temp = createVariable(gdlVar.getName(), argType, variableReg.getRegisteredVariables());
		//			if (variableReg != null) {
		//				variableReg.addTranslation(gdlVar, temp);
		//			}
		//			toReturn = temp;
		//		}
		//		return toReturn;
	}


	public FodotFunction generateFunction(GdlFunction gdlFunc) {		
		FodotTypeFunctionDeclaration decl = trans.getGdlVocabulary().getFunctionDeclaration(gdlFunc);
		return createFunction( decl, generateTerms(gdlFunc.getBody(), decl.getArgumentTypes()) );
	}

	/**********************************************/


	/**********************************************
	 *  Default Variables
	 ***********************************************/

	//We only allow one time variable per sentence
	FodotVariable timeVariable;
	public FodotVariable createTimeVariable() {
		if (timeVariable == null) {
			timeVariable = createVariable("t", trans.getTimeType(), variableReg.getRegisteredVariables());
			variableReg.registerVariable(timeVariable);
		}
		return timeVariable;
	}

	public FodotVariable createActionVariable() {
		FodotVariable result = createVariable("act", trans.getActionType(), variableReg.getRegisteredVariables());
		variableReg.registerVariable(result);
		return result;
	}

	public FodotVariable createPlayerVariable() {
		FodotVariable result = createVariable("p", trans.getPlayerType(), variableReg.getRegisteredVariables());
		variableReg.registerVariable(result);
		return result;
	}

	/**********************************************/


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
	 * @return
	 */
	//	public List<IFodotTerm> processSentenceArguments(
	//			GdlSentence sentence, FodotArgumentListDeclaration declaration,
	//			int argumentOffset) {
	//
	//		List<IFodotTerm> elements = new ArrayList<IFodotTerm>(); 
	//		for (int i = 0; i < sentence.arity(); i++) {
	////			FodotType currentArgType = declaration.getArgumentType(i+argumentOffset);
	//
	//			GdlTerm term = sentence.get(i);
	//			IFodotTerm element = generateTerm(term);
	//			elements.add(element);
	//
	//			//Improve declaration types (if variable was already mapped)
	////			FodotType actualType = element.getType();
	////			if (currentArgType != actualType && currentArgType == trans.getAllType()) {
	////				System.out.println("fixed " + declaration.getName() + " arg("+i+") to " + actualType);
	////				declaration.setArgumentType(i+argumentOffset, actualType);
	////			}
	//
	//
	//		}
	//		return elements;
	//	}

	//	private List<IFodotTerm> processSentenceArguments(
	//			GdlSentence sentence,FodotArgumentListDeclaration declaration) {
	//		return processSentenceArguments(sentence, declaration, 0);
	//	}

	private List<IFodotTerm> processSentenceArgumentsTimed(GdlSentence sentence,
			FodotArgumentListDeclaration declaration) {
		List<IFodotTerm> arguments = new ArrayList<IFodotTerm>();		
		arguments.add(createTimeVariable());
		arguments.addAll(generateTerms(
				sentence.getBody(), 
				FormulaUtil.removeTypes(declaration.getArgumentTypes(), trans.getTimeType())));
		return arguments;
	}

	//	public FodotFunction generateTypeFunction(GdlFunction function, FodotTypeFunctionDeclaration declaration) {
	//		FodotFunction result = createFunction(declaration, generateTerms(function.toSentence().getBody()));
	//		return result;
	//	}

	public FodotPredicate generateTimedPredicate(GdlSentence sentence,
			FodotPredicateDeclaration declaration) {
		return createPredicate(declaration, processSentenceArgumentsTimed(sentence, declaration));
	}
	/**********************************************/


}
