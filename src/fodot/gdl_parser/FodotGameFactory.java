package fodot.gdl_parser;

import fodot.gdl_parser.util.LTCPool;
import fodot.objects.Fodot;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.objects.procedure.FodotProcedure;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.sentence.IFodotSentenceElement;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.formulas.argumented.FodotPredicate;
import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.sentence.terms.FodotPredicateTerm;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotSentence;
import fodot.objects.theory.FodotTheory;
import fodot.objects.theory.definitions.FodotInductiveDefinitionConnector;
import fodot.objects.theory.definitions.FodotInductiveSentence;
import fodot.objects.vocabulary.FodotLTCVocabulary;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.util.FormulaUtil;
import org.ggp.base.util.Pair;

import java.util.*;

import static fodot.helpers.FodotPartBuilder.*;

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
                            FodotPredicateDeclaration terminalPred) {
        if(!source.isInternalPool(pool))
            throw new IllegalArgumentException("Pool and source are not a match.");
        this.source = source;
        this.pool = pool;
        this.doPredicateDeclaration = doPred;
        this.terminalTimePredicateDeclaration = terminalPred;
        buildDefaultVocItems();
    }


    public FodotGameFactory(GdlFodotTransformer source,
                            LTCPool pool,
                            FodotPredicateDeclaration doPred,
                            FodotPredicateDeclaration terminalPred,
                            int timeLimit) {
        this(source, pool, doPred, terminalPred);
        this.timeLimit = timeLimit;
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private GdlFodotTransformer source;
    private LTCPool pool;

    private FodotFunctionDeclaration startFunctionDeclaration;
    private FodotFunctionDeclaration nextFunctionDeclaration;
    private FodotPredicateDeclaration doPredicateDeclaration;
    private FodotPredicateDeclaration terminalTimePredicateDeclaration;
    private FodotTypeDeclaration scoreTypeDeclaration;
    private FodotFunctionDeclaration scoreFunctionDeclaration;

    private int timeLimit = DEFAULT_TIME;

    private final static int DEFAULT_TIME = 20;

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    Fodot createFodot(){
        FodotVocabulary voc = this.buildVocabulary();
        FodotTheory theo = this.buildTheory(voc);
        FodotStructure struc = this.buildStructure(voc);
        FodotProcedures proc = this.buildProcedures();
        FodotIncludeHolder incl = createIncludeHolder(createIncludeLTC());

        return new Fodot(voc,theo,struc,proc,incl);
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

        /**
         * nodig: alle roles
         * resultaat:
         * type Player constructed from {*alle roles*}
         */

        toReturn.addType(
                createTypeDeclaration(
                        source.getPlayerType()
                )
        );


        /**
         * nodig: alle constanten
         * resultaat:
         * type Unfilled constructed from {*alle constanten*}
         */
        toReturn.addType(
                createTypeDeclaration(
                        source.getAllType()
                )
        );


        /**
         * nodig: alle fluent predicaten
         * resultaat voor elk predicaat:
         * pred(Time,*standaard argumenten*)
         * I_pred(*standaard argumenten*)
         * C_pred(Time,*standaard argumenten*)
         */
        for (FodotPredicateDeclaration declaration : this.pool.getFluentPredicates()) {
            toReturn.addPredicate(this.pool.getTimedVerionOf(declaration));
            toReturn.addPredicate(this.pool.getInitialOf(declaration));
            toReturn.addPredicate(this.pool.getCauseOf(declaration));
            toReturn.addPredicate(this.pool.getCauseNotOf(declaration));
        }

        /**
         * nodig: alle static predicaten
         * resultaat:
         * pred(*standaard argumenten*)
         */
        for (FodotPredicateDeclaration declaration : this.pool.getStaticPredicates()) {
            toReturn.addPredicate(declaration);
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
        for (FodotPredicateDeclaration declaration : pool.getFluentPredicates()) {
            List<FodotInductiveSentence> definitions = new ArrayList<>();

            int originalArity = declaration.getAmountOfArgumentTypes();

            List<IFodotTerm> argList = new ArrayList<>();
            List<IFodotTerm> iArgList = new ArrayList<>();
            Set<FodotVariable> varSet = new HashSet<>();
            argList.add(createFunction(this.startFunctionDeclaration));

            for (int i = 0; i < originalArity; i++) {
                FodotVariable newVar = createVariable(source.getAllType());
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

            FodotVariable timeVar = createVariable(source.getTimeType());

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

            toReturn.addInductiveDefinition(createInductiveDefinition(definitions));
        }

        /**
         * nodig: alle causations van elk fluent predicaat
         * resultaat voor elk predicaat:
         * {
         *     !(var [Unfilled])*aantal argumenten keer* t [Time]: C_*pred*(t,*vars*) <- *causation*).
         *     *dit voor elke causation van hetzelfde predicaat*
         * }
         */
        Map<FodotPredicateDeclaration, Set<Pair<FodotPredicate, IFodotFormula>>> nextMap = source.getNextMap();
        for (FodotPredicateDeclaration predicate : nextMap.keySet()) {
            List<FodotInductiveSentence> definitions = new ArrayList<>();
            for (Pair<FodotPredicate, IFodotFormula> pair : nextMap.get(predicate)) {
                definitions.add(createInductiveSentence(createInductiveDefinitionConnector(pair.left, pair.right)));
            }
            toReturn.addInductiveDefinition(createInductiveDefinition(definitions));
        }

        /**
         * nodig: alle causations van elk static predicaat
         * resultaat voor elk predicaat:
         * {
         *     !(var [Unfilled])*aantal argumenten keer*: *pred*(*vars*) <- *causation*).
         *     *dit voor elke causation van hetzelfde predicaat*
         * }
         */
        //TODO

        /**
         * nodig: alle legals, als legal head en legal body
         * resultaat voor elk koppel:
         * !(var [Unfilled])*aantal argumenten keer* t [Time]: *legal head* => *legal body*
         */
        for (Map.Entry<FodotPredicate, Set<IFodotFormula>> entry : source.getLegalMap().entrySet()) {
            for (IFodotFormula body : entry.getValue()) {
                toReturn.addSentence(createSentence(
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
        Map<Pair<String, Integer>, Set<IFodotFormula>> scoreMap = source.getScoreMap();
        List<FodotInductiveSentence> definitions = new ArrayList<>();
        for (Pair<String, Integer> scorePair: scoreMap.keySet()) {
            String playerName = scorePair.left;
            int score = scorePair.right;
            for (IFodotFormula formula : scoreMap.get(scorePair)) {
                definitions.add(
                        createInductiveSentence(createInductiveDefinitionConnector(
                                        createInductiveFunctionHead(
                                                createFunction(
                                                        scoreFunctionDeclaration,
                                                        createConstant("p_" + playerName, source.getPlayerType())
                                                ),
                                                createConstant(Integer.toString(score), source.getScoreType())
                                        ), FormulaUtil.makeVariableFree(formula)
                                )
                        )
                );
            }
        }
        toReturn.addInductiveDefinition(createInductiveDefinition(definitions));


        /**
         * nodig: elke terminal *voorwaarde*
         * resultaat:
         * {
         *     !t [Time]: terminalTime(t) <- *voorwaarde*
         *     *voor elke voorwaarde*
         * }
         */
        definitions = new ArrayList<>();
        for (IFodotFormula formula : source.getTerminalSet()) {
            definitions.add(
                    createInductiveSentence(
                            createInductiveDefinitionConnector(
                                    createPredicate(
                                            this.terminalTimePredicateDeclaration,
                                            createVariable("t", source.getTimeType())
                                    ),
                                    formula
                            )
                    )
            );
        }
        toReturn.addInductiveDefinition(createInductiveDefinition(definitions));

        return toReturn;
    }

    private FodotStructure buildStructure(FodotVocabulary voc) {
        FodotStructure toReturn = getDefaultStructure(voc);

        /**
         * nodig: *naam* van onze speler
         * resultaat:
         * Score={*naam*()->100}
         */
        Map<FodotConstant[],FodotConstant> desiredResult = new HashMap<>();
        FodotConstant[] ownRole = {source.getOwnRole()};
        desiredResult.put(ownRole,createConstant("100", getNaturalNumberType()));
        toReturn.addEnumeration(
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
        Map<FodotPredicateDeclaration, Set<FodotConstant[]>> initMap
                = this.source.getInitialValues();
        for (FodotPredicateDeclaration declaration : initMap.keySet()) {
            toReturn.addEnumeration(
                    createPredicateEnumeration(this.pool.getInitialOf(declaration),
                            new ArrayList<>(initMap.get(declaration)))
            );
        }

        /**
         * nodig: *waarden* voor elk statisch *predicaat*
         * resultaat:
         * *predicaat*={*waarden()*}
         */
        Map<FodotPredicateDeclaration, Set<FodotConstant[]>> staticMap
                = this.source.getStaticValues();
        for (FodotPredicateDeclaration declaration : staticMap.keySet()) {
            toReturn.addEnumeration(
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

        // type Time isa nat
        defaultVoc.addType(createTypeDeclaration(source.getTimeType()));

        // Start: Time
        defaultVoc.addFunction(startFunctionDeclaration);

        // partial Next(Time):Time
        defaultVoc.addFunction(nextFunctionDeclaration);

        // do(Time, Player, Action)
        defaultVoc.addPredicate(doPredicateDeclaration);

        // type Action constructed from {*action*}
        defaultVoc.addType(createTypeDeclaration(source.getActionType()));

        // terminalTime(Time)
        defaultVoc.addPredicate(terminalTimePredicateDeclaration);

        // type ScoreType isa nat
        defaultVoc.addType(scoreTypeDeclaration);

        // Score(Player): ScoreType
        defaultVoc.addFunction(scoreFunctionDeclaration);

        return defaultVoc;
    }

    private FodotTheory getDefaultTheory(FodotVocabulary voc) {
        FodotTheory defaultTheory = createTheory(voc);

        //!a [Action] p [Player] t [Time]: do(t,p,a) => ~terminalTime(t) & (?t2 [Time]: Next(t) = t2).
        FodotVariable a_Action = createVariable("a", source.getActionType());
        FodotVariable p_Player = createVariable("p", source.getPlayerType());
        FodotVariable t_Time = createVariable("t", source.getTimeType());
        FodotVariable t2_Time = createVariable("t2",source.getTimeType());
        Set<FodotVariable> variables =
                new HashSet<>(Arrays.asList(a_Action,p_Player,t_Time));
        variables.add(a_Action);
        variables.add(p_Player);
        variables.add(t_Time);
        defaultTheory.addSentence(createSentence(createForAll(variables,
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
        defaultTheory.addSentence(createSentence(createForAll(variables,
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
                                                                                this.nextFunctionDeclaration, t_Time),
                                                                        t2_Time)
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
        defaultTheory.addInductiveDefinition(createInductiveDefinition(definitions));

        return defaultTheory;
    }

    private FodotStructure getDefaultStructure(FodotVocabulary voc) {
        FodotStructure defaultStructure = createStructure(voc);

        //Start=0
        defaultStructure.addEnumeration(
                createConstantFunctionEnumeration(
                        this.startFunctionDeclaration,
                        createInteger(0)
                )
        );

        //Time={0..Constant(timeLimit)}
        defaultStructure.addEnumeration(
                createNumericalTypeRangeEnumeration(
                        source.getTimeType(),
                        createInteger(0),
                        createInteger(this.timeLimit)
                )
        );

        //ScoreType={0..100}
        defaultStructure.addEnumeration(
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

    private FodotProcedures getDefaultProcedures() {

        //stdoptions.nbmodels=5
        //printmodels(modelexpand(T,S))
        List<FodotProcedure> proc = new ArrayList<>(
                Arrays.asList(
                        createProcedure("stdoptions.nbmodels=5"),
                        createProcedure("printmodels(modelexpand(T,S))")
                )
        );

        return createProcedures("main",proc);
    }

}
