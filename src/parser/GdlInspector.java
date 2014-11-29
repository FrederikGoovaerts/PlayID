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

    /***************************************************************************
     * Visitor Methods
     **************************************************************************/

    @Override
    public void visitRelation(GdlRelation relation) {
        if(relation.getName().getValue().equals("role")){
            this.addRole(relation.getBody().get(0).toString());
        }
        System.out.println("relation = [" + relation + "]");
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
