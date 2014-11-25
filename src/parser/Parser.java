package parser;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ggp.base.util.Pair;
import org.ggp.base.util.files.FileUtils;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.grammar.Gdl;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 *
 * Parsing class for reading a basic .kif file containing a gdl specification
 * and forming data objects which can be used to inspect and adapt the game.
 */
public class Parser {

    //---------------------------------------------
    // Main method
    //---------------------------------------------

    public static void main(String[] args) {
        File file = new File("resources/games/choice.kif");
        System.out.println(file.exists());
        Parser test = new Parser(file);
        List<Gdl> rules = test.game.getRules();
        for (Gdl rule :rules) {
            System.out.println(rule);
        }
    }

    //---------------------------------------------
    // Constructor
    //---------------------------------------------

    public Parser(File file) {
        String fileContents = FileUtils.readFileAsString(file);
        game = Game.createEphemeralGame(Game.preprocessRulesheet(fileContents));
    }

    //---------------------------------------------
    // Class properties
    //---------------------------------------------

    /* The game for this parser */
    private final Game game;

    /* The roles in the game, as a set of text Strings */
    private Set<String> roles;

    public Set<String> getRoles(){
        if (roles == null)
            roles = this.parseRoles();
        return new HashSet<>(roles);
    }

    /* The INIT predicates of the game, as GDL sentences, without the INIT predicate */
    private Set<Gdl> inits;

    public Set<Gdl> getInits(){
        if (inits == null)
            inits = this.parseInits();
        return new HashSet<>(inits);
    }

    /* The BASE predicates of the game, as GDL sentences,
        where the implication is made into pairs of Heads and Bodies */
    private Set<Pair<Gdl,List<Gdl>>> base;

    public Set<Pair<Gdl,List<Gdl>>> getBase(){
        if (base == null)
            base = this.parseBase();
        return new HashSet<>(base);
    }

    /* The INPUT predicates of the game, as GDL sentences,
        where the implication is made into pairs of Heads and Bodies */
    private Set<Pair<Gdl,List<Gdl>>> input;

    public Set<Pair<Gdl,List<Gdl>>> getInput(){
        if (input == null)
            input = this.parseInput();
        return new HashSet<>(input);
    }

    /* The NEXT predicates of the game, as GDL sentences,
        where the implication is made into pairs of Heads and Bodies */
    private Set<Pair<Gdl,List<Gdl>>> next;

    public Set<Pair<Gdl,List<Gdl>>> getNext(){
        if (next == null)
            next = this.parseNext();
        return new HashSet<>(next);
    }

    /* The LEGAL predicates of the game, as GDL sentences,
        where the implication is made into pairs of Heads and Bodies */
    private Set<Pair<Gdl,List<Gdl>>> legal;

    public Set<Pair<Gdl,List<Gdl>>> getLegal(){
        if (legal == null)
            legal = this.parseLegal();
        return new HashSet<>(legal);
    }

    /* The GOAL predicates of the game, as GDL sentences,
        where the implication is made into pairs of Heads and Bodies */
    private Set<Pair<Gdl,List<Gdl>>> goal;

    public Set<Pair<Gdl,List<Gdl>>> getGoal(){
        if (goal == null)
            goal = this.parseGoal();
        return new HashSet<>(input);
    }

    /* The TERMINAL predicates of the game, as GDL sentences,
        where the implication is made into pairs of Heads and Bodies */
    private Set<Pair<Gdl,List<Gdl>>> terminal;

    public Set<Pair<Gdl,List<Gdl>>> getTerminal(){
        if (terminal == null)
            terminal = this.parseTerminal();
        return new HashSet<>(terminal);
    }



    //---------------------------------------------
    // Class Methods
    //---------------------------------------------

    private Set<String> parseRoles() {
        return null;
    }


    private Set<Gdl> parseInits() {
        return null;
    }

    private Set<Pair<Gdl, List<Gdl>>> parseBase() {
        return null;
    }

    private Set<Pair<Gdl, List<Gdl>>> parseInput() {
        return null;
    }

    private Set<Pair<Gdl, List<Gdl>>> parseNext() {
        return null;
    }

    private Set<Pair<Gdl, List<Gdl>>> parseLegal() {
        return null;
    }

    private Set<Pair<Gdl, List<Gdl>>> parseGoal() {
        return null;
    }

    private Set<Pair<Gdl, List<Gdl>>> parseTerminal() {
        return null;
    }
}
