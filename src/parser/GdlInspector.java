package parser;

import fodot.predicate.Predicate;
import fodot.type.Type;
import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.GdlVisitors;
import org.ggp.base.util.gdl.grammar.*;

import java.util.*;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlInspector extends GdlVisitor{

    /***************************************************************************
     * Constructor
     **************************************************************************/

    public GdlInspector(List<Gdl> rules){
        this.roles = new TreeSet<>();
        this.predicates = new TreeSet<>();
        this.constants = new HashMap<>();

        GdlVisitors.visitAll(rules, this);
        System.out.println(this.getRoles());
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

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
    private Set<Predicate> predicates;

    public Set<Predicate> getPredicates() {
        return new TreeSet<>(predicates);
    }

    private void addPredicate(Predicate pred){
        if(pred == null)
            throw new IllegalArgumentException();
        predicates.add(pred);
    }

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
            throw new IllegalArgumentException("constant is not yet in the mapping!");
        return (constants.get(constantName).equals(Type.getPlaceHolderType()));
    }

    /***************************************************************************
     * Visitor Methods
     **************************************************************************/

    @Override
    public void visitRelation(GdlRelation relation) {
        String relationName = relation.getName().getValue();
        switch (relationName) {
            case "role": this.processRole(relation);
                break;
            case "init": this.processInit(relation);
                break;
            case "legal": this.processLegal(relation);
                break;
            case "terminal": this.processTerminal(relation);
                break;
            case "goal": this.processGoal(relation);
                break;
            default: this.processStaticRelation(relation);
                break;
        }
    }

    private void processRole(GdlRelation relation) {
        this.addRole(relation.getBody().get(0).toString());
    }

    private void processInit(GdlRelation relation) {

    }

    private void processLegal(GdlRelation relation) {

    }

    private void processTerminal(GdlRelation relation) {

    }

    private void processGoal(GdlRelation relation) {

    }

    private void processStaticRelation(GdlRelation relation) {

    }

    @Override
    public void visitConstant(GdlConstant constant) {
        System.out.println("constant = [" + constant + "]");
    }

    @Override
    public void visitVariable(GdlVariable variable) {
        System.out.println("variable = [" + variable + "]");
    }

    @Override
    public void visitFunction(GdlFunction function) {
        //Add this stuff as a predicate to the theory
    }

    @Override
    public void visitDistinct(GdlDistinct distinct) {
        //This has to be picked up earlier in the recursive tree?
        //Needs a second run?
    }

    @Override
    public void visitRule(GdlRule rule) {
        // Important. Declares next, goals, terminals and more
    }
}
