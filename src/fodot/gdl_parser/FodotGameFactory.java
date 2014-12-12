package fodot.gdl_parser;

import fodot.objects.Fodot;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.theory.FodotTheory;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

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

    private int timeLimit = DEFAULT_TIME;

    private final static int DEFAULT_TIME = 20;

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    public Fodot createFodot(){
        Fodot toReturn = new Fodot(this.createVocabulary(),this.createTheory(),
                this.createStructure(),this.createProcedures());
        // TODO: add include<LTC>
        return toReturn;
    }

    private FodotVocabulary createVocabulary() {
        FodotVocabulary toReturn = getDefaultVocabulary();
        return toReturn;
    }

    private FodotTheory createTheory() {
        FodotTheory toReturn = getDefaultTheory();
        return toReturn;
    }

    private FodotStructure createStructure() {
        FodotStructure toReturn = getDefaultStructure();
        return toReturn;
    }

    private FodotProcedures createProcedures() {
        FodotProcedures toReturn = getDefaultProcedures();
        return toReturn;
    }

    /***************************************************************************
     * Default fodot contents
     **************************************************************************/

    private FodotVocabulary getDefaultVocabulary() {
        //TODO: Make this LTCvocabulary
        FodotVocabulary defaultVoc = new FodotVocabulary();
        // type Time isa nat
        List<FodotType> natList = new ArrayList<>();
        natList.add(FodotType.NATURAL_NUMBER);
        defaultVoc.addType(new FodotTypeDeclaration(this.timeType, null,
                natList, null));
        // Start: Time
        defaultVoc.addFunction(new FodotFunctionDeclaration("Start",
                new ArrayList<FodotType>(), this.timeType));
        // partial Next(Time):Time
        List<FodotType> timeList = new ArrayList<>();
        timeList.add(this.timeType);
        defaultVoc.addFunction(new FodotFunctionDeclaration("Next",timeList,
                this.timeType));
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
