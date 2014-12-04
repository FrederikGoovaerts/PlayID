package fodot.gdl_parser;

import fodot.objects.predicate.Predicate;
import fodot.objects.type.Type;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotBuilder implements GdlTransformer{

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public FodotBuilder(){
        this.roles = new TreeSet<>();
        this.predicates = new HashMap<>();
        this.constants = new HashMap<>();
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    //TODO: Al deze shit in een apart Fodot object

    /* Roles in the Gdl Game */
    private Set<String> roles;

    public Set<String> getRoles() {
        return new TreeSet<>(roles);
    }

    private void addRole(String roleName){
        if(roleName == null)
            throw new IllegalArgumentException();
        roles.add(roleName);
    }

    /* Predicates extracted from the Gdl Game */
    private HashMap<String,Predicate> predicates;

    public HashMap<String,Predicate> getPredicates() {
        return new HashMap<>(predicates);
    }

    public boolean isPredicateRegistered(String predName) {
        return predicates.containsKey(predName);
    }

    private void addPredicate(Predicate pred){
        if(pred == null)
            throw new IllegalArgumentException();
        predicates.put(pred.getPredicateName(), pred);
    }

    private Predicate getPredicate(String predName){
        if(!isPredicateRegistered(predName))
            throw new IllegalArgumentException("Predicate not found!");
        return predicates.get(predName);
    }

    //TODO: set van predicaten die nog niet volledig getyped zijn, om te
    // zien of er een nieuwe run van de regels gedaan moet worden om verder te
    // typescannen

    /* Constants in the Gdl Game, with their types */

    private Map<String,Type> constants;

    private void addConstant(String constantName, Type type){
        if(constants.containsKey(constantName)
                && !constants.get(constantName).equals(Type.getPlaceHolderType()))
            throw new IllegalArgumentException("Can't replace an existing constant!");
        constants.put(constantName,type);
    }

    private boolean isConstantRegistered(String constantName){
        return !constants.containsKey(constantName);
    }

    private boolean isConstantTyped(String constantName){
        if(!constants.containsKey(constantName))
            throw new IllegalArgumentException("Constant is not yet in the mapping!");
        return (constants.get(constantName).equals(Type.getPlaceHolderType()));
    }

    /***************************************************************************
     * Class Methods
     **************************************************************************/

    @Override
    public void processRole(GdlRelation relation) {
        // Role has only one body item, the name of a player
        this.addRole(relation.getBody().get(0).toString());
    }

    @Override
    public void processInit(GdlRelation relation) {
        // Init has one body item, namely a ground predicate representing a
        // truth for the initial state
        GdlTerm predTerm = relation.get(0);
        //Needs a cast to function instead of conversion to sentence?
        //This term is always a function
        GdlSentence predSentence = predTerm.toSentence();
        String predName = predSentence.getName().getValue();
        int amountOfArguments = predSentence.arity();

        if(!isPredicateRegistered(predName)){
            Predicate newPred = new Predicate(predName,
                    Type.getPlaceHolderList(amountOfArguments));
        }

        //TODO: check if constants are registered, if so, set pred type

        //TODO: bind constants to predicates (Type has to be updated)
        // Observer structure?

        //TODO: register constants?

        //TODO: register new types?

    }

    @Override
    public void processLegal(GdlRelation relation) {

    }

    @Override
    public void processTerminal(GdlRelation relation) {

    }

    @Override
    public void processGoal(GdlRelation relation) {

    }

    @Override
    public void processStaticPredicate(GdlRelation relation) {

    }

    @Override
    public void processPredicate(GdlTerm gdlTerm) {

    }

}
