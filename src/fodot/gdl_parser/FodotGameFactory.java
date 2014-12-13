package fodot.gdl_parser;

import static fodot.helpers.FodotPartBuilder.*;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.Fodot;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotTheory;
import fodot.objects.vocabulary.FodotLTCVocabulary;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotType;

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
        FodotVocabulary voc = this.buildVocabulary();
        FodotTheory theo = this.buildTheory(voc);
        FodotStructure struc = this.buildStructure(voc);
        FodotProcedures proc = this.buildProcedures();
        FodotIncludeHolder incl = createIncludeHolder(createIncludeLTC());

        Fodot toReturn = new Fodot(voc,theo,struc,proc,incl);
        return toReturn;
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
        defaultVoc.addFunction(createCompleteFunctionDeclaration("Start", this.timeType));

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
        defaultVoc.addType(createTypeDeclaration(this.scoreType, getNaturalNumberType()));

        // Score(Player): ScoreType
        List<FodotType> playerList = new ArrayList<>();
        playerList.add(this.playerType);
        defaultVoc.addFunction(createCompleteFunctionDeclaration("Score", playerList,
                this.scoreType));

        return defaultVoc;
    }

    private FodotTheory getDefaultTheory(FodotVocabulary voc) {
        FodotTheory defaultTheory = createTheory(voc);
        return defaultTheory;
    }

    private FodotStructure getDefaultStructure(FodotVocabulary voc) {
        FodotStructure defaultStructure = createStructure(voc);
        return defaultStructure;
    }

    private FodotProcedures getDefaultProcedures() {
        FodotProcedures defaultProcedures = createProcedures("main");
        return defaultProcedures;
    }

}
