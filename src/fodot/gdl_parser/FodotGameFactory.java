package fodot.gdl_parser;

import static fodot.helpers.FodotPartBuilder.*;

import fodot.objects.Fodot;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotTheory;
import fodot.objects.vocabulary.FodotLTCVocabulary;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotGameFactory {

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotGameFactory(GdlFodotTransformer source) {
        this.source = source;
    }

    public FodotGameFactory(GdlFodotTransformer source, int timeLimit) {
        this(source);
        this.timeLimit = timeLimit;
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private GdlFodotTransformer source;

    private FodotType timeType = new FodotType("Time");
    private FodotType playerType = new FodotType("Player");
    private FodotType actionType = new FodotType("Action");
    private FodotType scoreType = new FodotType("Score");

    private int timeLimit = DEFAULT_TIME;

    private final static int DEFAULT_TIME = 20;

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    Fodot createFodot(){
        Fodot toReturn = new Fodot(this.createVocabulary(),this.createTheory(),
                this.createStructure(),this.createProcedures());
        toReturn.addIncludes("LTC");
        return toReturn;
    }

    private FodotVocabulary createVocabulary() {
        FodotVocabulary toReturn = getDefaultVocabulary();

        //TODO: rest
        return toReturn;
    }

    private FodotTheory createTheory() {
        FodotTheory toReturn = getDefaultTheory();

        //TODO: rest
        return toReturn;
    }

    private FodotStructure createStructure() {
        FodotStructure toReturn = getDefaultStructure();

        //TODO: rest
        return toReturn;
    }

    private FodotProcedures createProcedures() {
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

        Set<FodotType> natSet = new HashSet<>();
        natSet.add(FodotType.NATURAL_NUMBER);
        defaultVoc.addType(createTypeDeclaration(this.timeType, new HashSet<>(),
                natSet));

        // Start: Time
        defaultVoc.addFunction(createCompleteFunctionDeclaration("Start",
                new ArrayList<>(), this.timeType));

        // partial Next(Time):Time
        List<FodotType> timeList = new ArrayList<>();
        timeList.add(this.timeType);
        defaultVoc.addFunction(createPartialFunctionDeclaration("Next", timeList,
                this.timeType));

        // do(Time, Player, Action)
        List<FodotType> typeList = new ArrayList<>();
        typeList.add(this.timeType);
        typeList.add(this.playerType);
        typeList.add(this.actionType);
        defaultVoc.addPredicate(createPredicateDeclaration("do", typeList));


        // terminalTime(Time)
        List<FodotType> typeList2 = new ArrayList<>();
        typeList2.add(this.timeType);
        defaultVoc.addPredicate(createPredicateDeclaration("terminalTime", typeList2));

        // type ScoreType isa nat
        natSet.add(FodotType.NATURAL_NUMBER);
        defaultVoc.addType(createTypeDeclaration(this.scoreType, null,
                natSet));

        // Score(Player): ScoreType
        List<FodotType> playerList = new ArrayList<>();
        playerList.add(this.playerType);
        defaultVoc.addFunction(createCompleteFunctionDeclaration("Score", playerList,
                this.scoreType));

        return defaultVoc;
    }

    private FodotTheory getDefaultTheory() {
        return null;
    }

    private FodotStructure getDefaultStructure() {
        return null;
    }

    private FodotProcedures getDefaultProcedures() {
        return null;
    }

}
