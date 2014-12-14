package fodot.gdl_parser;

import static fodot.helpers.FodotPartBuilder.*;

import java.util.*;

import fodot.objects.Fodot;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.objects.procedure.FodotProcedure;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.sentence.formulas.argumented.FodotPredicate;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotSentence;
import fodot.objects.theory.FodotTheory;
import fodot.objects.vocabulary.FodotLTCVocabulary;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotGameFactory {

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotGameFactory(GdlFodotTransformer source) {
        this.source = source;
        buildDefaultVocItems();
    }


    public FodotGameFactory(GdlFodotTransformer source, int timeLimit) {
        this(source);
        this.timeLimit = timeLimit;
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private GdlFodotTransformer source;

    private FodotType timeType;
    private FodotType playerType;
    private FodotType actionType;
    private FodotType scoreType;

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

        Fodot toReturn = new Fodot(voc,theo,struc,proc,incl);
        return toReturn;
    }

    private void buildDefaultVocItems() {
        this.timeType = new FodotType("Time");
        this.playerType = new FodotType("Player");
        this.actionType = new FodotType("Action");
        this.scoreType = new FodotType("Score");

        this.startFunctionDeclaration = createCompleteFunctionDeclaration("Start", this.timeType);

        List<FodotType> timeList = new ArrayList<>();
        timeList.add(this.timeType);
        this.nextFunctionDeclaration = createPartialFunctionDeclaration("Next", timeList,
                this.timeType);

        List<FodotType> typeList = new ArrayList<>();
        typeList.add(this.timeType);
        typeList.add(this.playerType);
        typeList.add(this.actionType);
        this.doPredicateDeclaration = createPredicateDeclaration("do", typeList);

        ArrayList<FodotType> typeList2 = new ArrayList<>();
        typeList2.add(this.timeType);
        this.terminalTimePredicateDeclaration = createPredicateDeclaration("terminalTime", typeList2);

        this.scoreTypeDeclaration = createTypeDeclaration(this.scoreType, getNaturalNumberType());

        List<FodotType> playerList = new ArrayList<>();
        playerList.add(this.playerType);
        scoreFunctionDeclaration = createCompleteFunctionDeclaration("Score", playerList,
                this.scoreType);
    }

    private FodotVocabulary buildVocabulary() {
        FodotVocabulary toReturn = getDefaultVocabulary();

        //TODO: rest
        return toReturn;
    }

    private FodotTheory buildTheory(FodotVocabulary voc) {
        FodotTheory toReturn = getDefaultTheory(voc);

        //TODO: rest
        return toReturn;
    }

    private FodotStructure buildStructure(FodotVocabulary voc) {
        FodotStructure toReturn = getDefaultStructure(voc);

        //TODO: rest
        return toReturn;
    }

    private FodotProcedures buildProcedures() {
        FodotProcedures toReturn = getDefaultProcedures();

        //TODO: rest
        return toReturn;
    }

    /***************************************************************************
     * Default fodot contents
     **************************************************************************/

    private FodotVocabulary getDefaultVocabulary() {
        FodotLTCVocabulary defaultVoc = createLTCVocabulary();

        // type Time isa nat
        defaultVoc.addType(createTypeDeclaration(this.timeType, getNaturalNumberType()));

        // Start: Time
        defaultVoc.addFunction(startFunctionDeclaration);

        // partial Next(Time):Time
        defaultVoc.addFunction(nextFunctionDeclaration);

        // do(Time, Player, Action)
        defaultVoc.addPredicate(doPredicateDeclaration);


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
        FodotVariable a_Action = createVariable("a", this.actionType);
        FodotVariable p_Player = createVariable("p", this.playerType);
        FodotVariable t_Time = createVariable("t", this.timeType);
        FodotVariable t2_Time = createVariable("t2",this.timeType);
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
        List<FodotSentence> definitions = new ArrayList<>();
        definitions.add(
                createSentence(
                        createForAll(t_Time,
                                createInductiveDefinitionConnector(
                                        createInductiveFunctionHead(
                                                createFunction(this.nextFunctionDeclaration, t_Time),
                                                createAddition(t_Time,createInteger(1))
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
        );
        definitions.add(
                createSentence(
                        createEquals(
                                createFunction(this.nextFunctionDeclaration,createInteger(0)),
                                createInteger(1)
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
                        this.timeType,
                        createInteger(0),
                        createInteger(this.timeLimit)
                )
        );

        //ScoreType={0..100}
        defaultStructure.addEnumeration(
                createNumericalTypeRangeEnumeration(
                        this.scoreType,
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
