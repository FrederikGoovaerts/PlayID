package fodot.gdl_parser;

import static fodot.objects.FodotElementBuilder.createAddition;
import static fodot.objects.FodotElementBuilder.createAnd;
import static fodot.objects.FodotElementBuilder.createBlankLines;
import static fodot.objects.FodotElementBuilder.createComment;
import static fodot.objects.FodotElementBuilder.createCompleteFunctionDeclaration;
import static fodot.objects.FodotElementBuilder.createConstant;
import static fodot.objects.FodotElementBuilder.createConstantFunctionEnumeration;
import static fodot.objects.FodotElementBuilder.createEquals;
import static fodot.objects.FodotElementBuilder.createExists;
import static fodot.objects.FodotElementBuilder.createExistsExactly;
import static fodot.objects.FodotElementBuilder.createFodotFile;
import static fodot.objects.FodotElementBuilder.createForAll;
import static fodot.objects.FodotElementBuilder.createFunction;
import static fodot.objects.FodotElementBuilder.createFunctionEnumeration;
import static fodot.objects.FodotElementBuilder.createFunctionEnumerationElement;
import static fodot.objects.FodotElementBuilder.createImplies;
import static fodot.objects.FodotElementBuilder.createIncludeHolder;
import static fodot.objects.FodotElementBuilder.createIncludeLTC;
import static fodot.objects.FodotElementBuilder.createInductiveDefinition;
import static fodot.objects.FodotElementBuilder.createInductiveDefinitionConnector;
import static fodot.objects.FodotElementBuilder.createInductiveFunctionHead;
import static fodot.objects.FodotElementBuilder.createInductiveQuantifier;
import static fodot.objects.FodotElementBuilder.createInductiveSentence;
import static fodot.objects.FodotElementBuilder.createInteger;
import static fodot.objects.FodotElementBuilder.createLTCVocabulary;
import static fodot.objects.FodotElementBuilder.createNot;
import static fodot.objects.FodotElementBuilder.createNumericalTypeRangeEnumeration;
import static fodot.objects.FodotElementBuilder.createPartialFunctionDeclaration;
import static fodot.objects.FodotElementBuilder.createPredicate;
import static fodot.objects.FodotElementBuilder.createPredicateEnumeration;
import static fodot.objects.FodotElementBuilder.createProcedure;
import static fodot.objects.FodotElementBuilder.createProcedures;
import static fodot.objects.FodotElementBuilder.createSentence;
import static fodot.objects.FodotElementBuilder.createStructure;
import static fodot.objects.FodotElementBuilder.createTheory;
import static fodot.objects.FodotElementBuilder.createTypeDeclaration;
import static fodot.objects.FodotElementBuilder.createVariable;
import static fodot.objects.FodotElementBuilder.getNaturalNumberType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ggp.base.util.Pair;

import fodot.gdl_parser.secondphase.GdlFodotTransformer;
import fodot.gdl_parser.secondphase.data.FodotCompoundData;
import fodot.gdl_parser.secondphase.data.FodotNextData;
import fodot.gdl_parser.util.LTCPool;
import fodot.objects.comments.FodotComment;
import fodot.objects.file.IFodotFile;
import fodot.objects.general.IFodotElement;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.objects.procedure.FodotProcedureStatement;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.functionenum.elements.IFodotFunctionEnumerationElement;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.FodotTheory;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.inductivedefinitions.FodotInductiveSentence;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.FodotLTCVocabulary;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionFullDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.util.FormulaUtil;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotGameFactory {

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotGameFactory(GdlFodotTransformer source,
                            LTCPool pool,
                            FodotPredicateDeclaration doPred,
                            FodotPredicateDeclaration terminalPred,
                            int timeLimit) {

        if(!source.isInternalPool(pool))
            throw new IllegalArgumentException("Pool and source are not a match.");
        this.source = source;
        this.pool = pool;
        this.doPredicateDeclaration = doPred;
        this.terminalTimePredicateDeclaration = terminalPred;
        this.turnLimit = timeLimit;
        buildDefaultVocItems();
    }

    public FodotGameFactory(GdlFodotTransformer source,
                            LTCPool pool,
                            FodotPredicateDeclaration doPred,
                            FodotPredicateDeclaration terminalPred) {
        this(source, pool, doPred, terminalPred, DEFAULT_TURN_LIMIT);
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private GdlFodotTransformer source;
    private LTCPool pool;

    private FodotFunctionFullDeclaration startFunctionDeclaration;
    private FodotFunctionFullDeclaration nextFunctionDeclaration;
    private FodotPredicateDeclaration doPredicateDeclaration;
    private FodotPredicateDeclaration terminalTimePredicateDeclaration;
    private FodotTypeDeclaration scoreTypeDeclaration;
    private FodotFunctionFullDeclaration scoreFunctionDeclaration;

    private int turnLimit = DEFAULT_TURN_LIMIT;

    private final static int DEFAULT_TURN_LIMIT = 20;

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    public IFodotFile createFodot() {
    	FodotComment topComment = createComment(0,
    			"This FO(.) file was generated with PlayID.\n" //TODO add from what file it was generated?
    			+ "PlayID is a program made by Frederik Goovaerts and Thomas Winters.");
        FodotVocabulary voc = this.buildVocabulary();
        FodotTheory theo = this.buildTheory(voc);
        FodotStructure struc = this.buildStructure(voc);
        FodotProcedures proc = this.buildProcedures();
        FodotIncludeHolder incl = createIncludeHolder(createIncludeLTC());

        return createFodotFile(incl, Arrays.asList(topComment, voc,theo,struc,proc));
    }

    private void buildDefaultVocItems() {

        this.startFunctionDeclaration = createCompleteFunctionDeclaration("Start", source.getTimeType());

        List<FodotType> timeList = new ArrayList<>();
        timeList.add(source.getTimeType());
        this.nextFunctionDeclaration = createPartialFunctionDeclaration("Next", timeList,
                source.getTimeType());

        this.scoreTypeDeclaration = createTypeDeclaration(source.getScoreType());

        List<FodotType> playerList = new ArrayList<>();
        playerList.add(source.getPlayerType());
        scoreFunctionDeclaration = createCompleteFunctionDeclaration("Score", playerList,
                source.getScoreType());
    }

    private FodotVocabulary buildVocabulary() {
        FodotVocabulary toReturn = getDefaultVocabulary();

        toReturn.addElement(createBlankLines(1));
        toReturn.addElement(createComment("Vocabulary elements derived from GDL file"));
    
        /**
         * nodig: alle roles
         * resultaat:
         * type Player constructed from {*alle roles*}
         */

        toReturn.addElement(
                createTypeDeclaration(
                        source.getPlayerType()
                )
        );


        /**
         * nodig: alle constanten
         * resultaat:
         * type Unfilled constructed from {*alle constanten*}
         */
        toReturn.addElement(
                createTypeDeclaration(
                        source.getAllType()
                )
        );

        /**
         * nodig: alle static predicaten
         * resultaat:
         * pred(*standaard argumenten*)
         */
        for (FodotPredicateDeclaration declaration : this.pool.getStaticPredicates()) {
            toReturn.addElement(declaration);
        }

        /**
         * nodig: alle compound static predicaten
         * resultaat:
         * pred(*standaard argumenten*)
         */
        for (FodotPredicateDeclaration declaration : this.pool.getCompoundTimedDeclarations()) {
            toReturn.addElement(declaration);
        }

    	toReturn.addElement(createBlankLines(1));
        toReturn.addElement(createComment("LTC predicates for the fluent predicates"));
        /**
         * nodig: alle fluent predicaten
         * resultaat voor elk predicaat:
         * pred(Time,*standaard argumenten*)
         * I_pred(*standaard argumenten*)
         * C_pred(Time,*standaard argumenten*)
         */
        for (FodotPredicateDeclaration declaration : this.pool.getFluentPredicates()) {
//        	toReturn.addElement(createComment("LTC for " + declaration.getName()));
            toReturn.addElement(this.pool.getTimedVerionOf(declaration));
            toReturn.addElement(this.pool.getInitialOf(declaration));
            toReturn.addElement(this.pool.getCauseOf(declaration));
            toReturn.addElement(this.pool.getCauseNotOf(declaration));
        	toReturn.addElement(createBlankLines(1));
        }

        return toReturn;
    }

    private FodotTheory buildTheory(FodotVocabulary voc) {
        FodotTheory toReturn = getDefaultTheory(voc);

        /**
         * nodig: alle fluent predicaten
         * resultaat voor elk predicaat:
         * {
         *     !(var [Unfilled])*aantal argumenten keer* : *pred*(Start,*vars*) <- I_*pred*(*vars*).
         *     !(var [Unfilled])*aantal argumenten keer* t [Time]: *pred*(Next(t),*vars*) <- C_*pred*(t,*vars*).
         * }
         */
        if (!pool.getFluentPredicates().isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("Inductive definitions for the fluent predicates"));
        }
        for (FodotPredicateDeclaration declaration : pool.getFluentPredicates()) {
            List<FodotInductiveSentence> definitions = new ArrayList<>();

            int originalArity = declaration.getAmountOfArgumentTypes();

            List<IFodotTerm> argList = new ArrayList<>();
            List<IFodotTerm> iArgList = new ArrayList<>();
            Set<FodotVariable> varSet = new HashSet<>();
            argList.add(createFunction(this.startFunctionDeclaration));

            for (int i = 0; i < originalArity; i++) {
                FodotVariable newVar = createVariable(source.getAllType(), varSet);
                argList.add(newVar);
                iArgList.add(newVar);
                varSet.add(newVar);
            }

            definitions.add(
                    createInductiveSentence(
                            createInductiveDefinitionConnector(
                                    createPredicate(pool.getTimedVerionOf(declaration), argList),
                                    createPredicate(pool.getInitialOf(declaration), iArgList)
                            )
                    )
            );

            argList = new ArrayList<>(iArgList);
            List<IFodotTerm> cArgList = new ArrayList<>(iArgList);
            varSet = new HashSet<>(varSet);

            FodotVariable timeVar = createVariable(source.getTimeType(), varSet);

            argList.add(0,createFunction(this.nextFunctionDeclaration,timeVar));
            cArgList.add(0,timeVar);
            varSet.add(timeVar);

            definitions.add(createInductiveSentence(
                    createInductiveQuantifier(
                        createForAll(
                                varSet,
                                createInductiveDefinitionConnector(
                                        createPredicate(pool.getTimedVerionOf(declaration),argList),
                                        createPredicate(pool.getCauseOf(declaration),cArgList)
                                )
                        )
                    )
            ));

            toReturn.addElement(createInductiveDefinition(definitions));
        }

        /**
         * nodig: alle causations van elk fluent predicaat
         * resultaat voor elk predicaat:
         * {
         *     !(var [Unfilled])*aantal argumenten keer* t [Time]: C_*pred*(t,*vars*) <- *causation*).
         *     *dit voor elke causation van hetzelfde predicaat*
         * }
         */
        Map<FodotPredicateDeclaration, Set<FodotNextData>> nextMap = source.getNextMap();
        if (!nextMap.isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("The fluent predicates' causations"));
        }
        for (FodotPredicateDeclaration predicate : nextMap.keySet()) {
            List<FodotInductiveSentence> definitions = new ArrayList<>();
            for (FodotNextData data : nextMap.get(predicate)) {
                definitions.add(createInductiveSentence(createInductiveDefinitionConnector(data.getPredicate(), data.getFormula())));
            }
            toReturn.addElement(createInductiveDefinition(definitions));
        }

        /**
         * nodig: alle causations van elk static predicaat
         * resultaat voor elk predicaat:
         * {
         *     !(var [Unfilled])*aantal argumenten keer*: *pred*(*vars*) <- *causation*).
         *     *dit voor elke causation van hetzelfde predicaat*
         * }
         */
        Map<FodotPredicateDeclaration, Set<FodotCompoundData>> compoundMap = source.getCompoundMap();
        if (!compoundMap.isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("The static predicates' causations"));
        }
        for (FodotPredicateDeclaration predicate : compoundMap.keySet()) {
            List<FodotInductiveSentence> definitions = new ArrayList<>();
            for (FodotCompoundData data : compoundMap.get(predicate)) {
                definitions.add(createInductiveSentence(createInductiveDefinitionConnector(data.getPredicate(), data.getFormula())));
            }
            toReturn.addElement(createInductiveDefinition(definitions));
        }

        /**
         * nodig: alle legals, als legal head en legal body
         * resultaat voor elk koppel:
         * !(var [Unfilled])*aantal argumenten keer* t [Time]: *legal head* => *legal body*
         */
        if (!source.getLegalMap().isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("Translation of the LEGAL sentences"));
        }
        for (Map.Entry<FodotPredicate, Set<IFodotFormula>> entry : source.getLegalMap().entrySet()) {
            for (IFodotFormula body : entry.getValue()) {
                toReturn.addElement(createSentence(
                        createImplies(entry.getKey(),body)
                ));
            }
        }

        /**
         * nodig: elke goal, als *player*, *score*, *voorwaarden*
         * resultaat:
         * {
         *    Score(*player*) = *score* <- (!t [Time]: terminalTime(t) => *voorwaarden*)
         *    *en dit voor elk tripel*
         * }
         */
        Map<Pair<IFodotTerm, IFodotTerm>, Set<IFodotFormula>> scoreMap = source.getScoreMap();
        if (!scoreMap.isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("Translation of the SCORE sentences"));
        }
        List<FodotInductiveSentence> definitions = new ArrayList<>();
        for (Pair<IFodotTerm, IFodotTerm> scorePair: scoreMap.keySet()) {
        	IFodotTerm playerTerm = scorePair.left;
            IFodotTerm score = scorePair.right;
            for (IFodotFormula formula : scoreMap.get(scorePair)) {
                definitions.add(
                        createInductiveSentence(createInductiveDefinitionConnector(
                                        createInductiveFunctionHead(
                                                createFunction(
                                                        scoreFunctionDeclaration,
                                                        playerTerm
                                                ),
                                                score
                                        ), FormulaUtil.makeVariableFree(formula)
                                )
                        )
                );
            }
        }
        toReturn.addElement(createInductiveDefinition(definitions));


        /**
         * nodig: elke terminal *voorwaarde*
         * resultaat:
         * {
         *     !t [Time]: terminalTime(t) <- *voorwaarde*
         *     *voor elke voorwaarde*
         * }
         */
        definitions = new ArrayList<>();
        if (!scoreMap.isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("Translation of the TERMINAL sentences"));
        }
        for (IFodotFormula formula : source.getTerminalSet()) {
        	Set<FodotVariable> formulaVariables = new HashSet<FodotVariable>();
        	FodotVariable timeVar = null;
        	for (IFodotElement el : formula.getAllInnerElementsOfClass(FodotVariable.class)) {
        		FodotVariable current = (FodotVariable) el;
        		formulaVariables.add(current);
        		if (current.getType().equals(source.getTimeType())) {
        			timeVar = current;
        		}
        	}
        	
        	if (timeVar == null) {
        		timeVar = createVariable("t", source.getTimeType(), formulaVariables);
        	}
        	
        	
            definitions.add(
                    createInductiveSentence(
                            createInductiveDefinitionConnector(
                                    createPredicate(
                                            this.terminalTimePredicateDeclaration,
                                            timeVar
                                    ),
                                    formula
                            )
                    )
            );
        }
        toReturn.addElement(createInductiveDefinition(definitions));

        return toReturn;
    }

    private FodotStructure buildStructure(FodotVocabulary voc) {
        FodotStructure toReturn = getDefaultStructure(voc);

        /**
         * nodig: *naam* van onze speler
         * resultaat:
         * Score={*naam*()->100}
         */
        toReturn.addElement(createBlankLines(1));
        toReturn.addElement(createComment("Desired result"));
       	List<IFodotFunctionEnumerationElement> desiredResult = new ArrayList<IFodotFunctionEnumerationElement>();
       	List<? extends IFodotTypeEnumerationElement> ownRole = Arrays.asList(source.getOwnRole());
        desiredResult.add(
        		createFunctionEnumerationElement(ownRole,createConstant("100", getNaturalNumberType())) );
        toReturn.addElement(
                createFunctionEnumeration(
                        this.scoreFunctionDeclaration, desiredResult
                )
        );

        /**
         * nodig: Initiele *waarden* voor elk fluent *predicaat*
         * resultaat voor elk predicaat:
         * I_*predicaat* = {*waarden()*}
         * OPGEPAST: in de structure moet achter elke constante een ()
         */
        Map<FodotPredicateDeclaration, Set<IFodotPredicateEnumerationElement>> initMap
                = this.source.getInitialValues();
        if (!initMap.isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("Initial values for the fluent predicates"));
        }
        for (FodotPredicateDeclaration declaration : initMap.keySet()) {
            toReturn.addElement(
                    createPredicateEnumeration(this.pool.getInitialOf(declaration),
                            new ArrayList<>(initMap.get(declaration)))
            );
        }

        /**
         * nodig: *waarden* voor elk statisch *predicaat*
         * resultaat:
         * *predicaat*={*waarden()*}
         */
        Map<FodotPredicateDeclaration, Set<IFodotPredicateEnumerationElement>> staticMap
                = this.source.getStaticValues();
        if (!staticMap.isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("All values found in the static predicates"));
        }
        for (FodotPredicateDeclaration declaration : staticMap.keySet()) {
            toReturn.addElement(
                    createPredicateEnumeration(declaration,
                            new ArrayList<>(staticMap.get(declaration)))
            );
        }

        return toReturn;
    }

    private FodotProcedures buildProcedures() {
        return getDefaultProcedures();
    }

    /***************************************************************************
     * Default fodot contents
     **************************************************************************/

    private FodotVocabulary getDefaultVocabulary() {
        FodotLTCVocabulary defaultVoc = createLTCVocabulary();

        defaultVoc.addElement(createComment("Default vocabulary elements (and types needed by the default elements)"));
        
        // type Time isa nat
        defaultVoc.addElement(createTypeDeclaration(source.getTimeType()));

        // Start: Time
        defaultVoc.addElement(startFunctionDeclaration);

        // partial Next(Time):Time
        defaultVoc.addElement(nextFunctionDeclaration);

        // type Action constructed from {*action*}
        defaultVoc.addElement(createTypeDeclaration(source.getActionType()));

        // terminalTime(Time)
        defaultVoc.addElement(terminalTimePredicateDeclaration);

        // type ScoreType isa nat
        defaultVoc.addElement(scoreTypeDeclaration);

        // Score(Player): ScoreType
        defaultVoc.addElement(scoreFunctionDeclaration);

        // do(Time, Player, Action)
        defaultVoc.addElement(doPredicateDeclaration);

        return defaultVoc;
    }

    private FodotTheory getDefaultTheory(FodotVocabulary voc) {
        FodotTheory defaultTheory = createTheory(voc);

        defaultTheory.addElement(createComment("Default theory elements:"));
        
        Set<FodotVariable> variables = new HashSet<FodotVariable>();
        FodotVariable a_Action = createVariable("a", source.getActionType(), variables);
        variables.add(a_Action);
        FodotVariable p_Player = createVariable("p", source.getPlayerType(), variables);
        variables.add(p_Player);
        FodotVariable t_Time = createVariable("t", source.getTimeType(), variables);
        variables.add(t_Time);
        FodotVariable t2_Time = createVariable("t2",source.getTimeType(), variables);
        variables.add(t2_Time);
        
        //!a [Action] p [Player] t [Time]: do(t,p,a) => ~terminalTime(t) & (?t2 [Time]: Next(t) = t2).
        variables = new HashSet<>(Arrays.asList(a_Action,p_Player,t_Time));
        defaultTheory.addElement(createSentence(createForAll(variables,
                createImplies(
                        createPredicate(this.doPredicateDeclaration
                                , new ArrayList<IFodotTerm>(Arrays.asList(t_Time,
                                p_Player,
                                a_Action)))
                        , createAnd(
                                createNot(createPredicate(
                                        this.terminalTimePredicateDeclaration,
                                        t_Time)),
                                createExists(t2_Time,
                                        createEquals(createFunction(
                                                        this.nextFunctionDeclaration, t_Time),
                                                t2_Time)
                                )
                        )
                )
        )));

        //! t [Time] p [Player]: ~terminalTime(t) & (?t2 [Time]: Next(t) = t2) => ?1 a [Action]: do(t,p,a).
        variables = new HashSet<>(Arrays.asList(t_Time,p_Player));
        defaultTheory.addElement(createSentence(createForAll(variables,
                createImplies(
                        createAnd(
                                createNot(createPredicate(
                                        this.terminalTimePredicateDeclaration,
                                        t_Time)),
                                createExists(t2_Time,
                                        createEquals(createFunction(
                                                        this.nextFunctionDeclaration, t_Time),
                                                t2_Time)
                                )
                        ), createExistsExactly(1, a_Action,
                                createPredicate(this.doPredicateDeclaration,
                                        t_Time, p_Player, a_Action)
                        )
                )
        )));


        /**
         * {
         *    !t: Next(t) = t+1 <- ~terminalTime(t) & (?t2 [Time]: Next(t2)=t).
         *    Next(0) = 1.
         * }
         */
        List<FodotInductiveSentence> definitions = new ArrayList<>();
        definitions.add(
                createInductiveSentence(
                        createInductiveQuantifier(
                                createForAll(t_Time,
                                        createInductiveDefinitionConnector(
                                                createInductiveFunctionHead(
                                                        createFunction(this.nextFunctionDeclaration, t_Time),
                                                        createAddition(t_Time, createInteger(1))
                                                ), createAnd(
                                                        createNot(createPredicate(
                                                                this.terminalTimePredicateDeclaration,
                                                                t_Time)),
                                                        createExists(t2_Time,
                                                                createEquals(createFunction(
                                                                                this.nextFunctionDeclaration, t2_Time),
                                                                        t_Time)
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        definitions.add(
                createInductiveSentence(
                        createInductiveFunctionHead(createFunction(this.nextFunctionDeclaration,createConstant("0",source.getTimeType())),
                                createConstant("1", source.getTimeType())
                        )
                )
        );
        defaultTheory.addElement(createInductiveDefinition(definitions));

        return defaultTheory;
    }

    private FodotStructure getDefaultStructure(FodotVocabulary voc) {
        FodotStructure defaultStructure = createStructure(voc);

        defaultStructure.addElement(createComment("Default structure elements:"));
        //Start=0
        defaultStructure.addElement(
                createConstantFunctionEnumeration(
                        this.startFunctionDeclaration,
                        createInteger(0)
                )
        );

        //Time={0..Constant(timeLimit)}
        defaultStructure.addElement(
                createNumericalTypeRangeEnumeration(
                        source.getTimeType(),
                        createInteger(0),
                        createInteger(this.turnLimit)
                )
        );

        //ScoreType={0..100}
        defaultStructure.addElement(
                createNumericalTypeRangeEnumeration(
                        source.getScoreType(),
                        createInteger(0),
                        createInteger(100)
                )
        );

        // This is dependent on the name of the player, cannot be instantiated here
        //Score={p_robot(),100}
        //defaultStructure.addEnumeration(
        //        createPredicateEnumeration()
        //);



        return defaultStructure;
    }

    public static final int DEFAULT_IDP_TIME_LIMIT = 15;
    private int idpTimeLimit = DEFAULT_IDP_TIME_LIMIT;
    private static final int DEFAULT_MODEL_LIMIT = 5;
    private int idpModelLimit = DEFAULT_MODEL_LIMIT;
    
    private FodotProcedures getDefaultProcedures() {
    	//stdoptions.timeout=timeLimit
        //stdoptions.nbmodels=5
        //printmodels(modelexpand(T,S))
        List<FodotProcedureStatement> proc = new ArrayList<>(
                Arrays.asList(
                        createProcedure("stdoptions.timeout="+idpTimeLimit),
                        createProcedure("stdoptions.nbmodels="+idpModelLimit),
                        createProcedure("printmodels(modelexpand(T,S))")
                )
        );

        return createProcedures("main",proc);
    }

}
