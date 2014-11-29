package parser;

import org.ggp.base.util.gdl.GdlVisitor;
import org.ggp.base.util.gdl.GdlVisitors;
import org.ggp.base.util.gdl.grammar.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class GdlInspector extends GdlVisitor{

    /***************************************************************************
     * Constructor
     *************************************************************************
     * @param rules*/

    public GdlInspector(List<Gdl> rules){
        GdlVisitors.visitAll(rules, this);
        System.out.println(this.getRoles());
    }

    /***************************************************************************
     * Class Properties
     **************************************************************************/

    private Set<String> roles = new TreeSet<>();

    public Set<String> getRoles() {
        return new TreeSet<>(roles);
    }

    private void addRole(String roleName){
        if(roleName == null)
            throw new IllegalArgumentException();
        roles.add(roleName);
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
        System.out.println("function = [" + function + "]");
    }

    @Override
    public void visitDistinct(GdlDistinct distinct) {
        System.out.println("distinct = [" + distinct + "]");
    }

    @Override
    public void visitRule(GdlRule rule) {
        System.out.println("rule = [" + rule + "]");
    }
}
