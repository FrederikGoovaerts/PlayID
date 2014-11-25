package parser;

import java.io.File;
import java.util.List;

import org.ggp.base.util.files.FileUtils;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.grammar.Gdl;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class Parser {

    public static void main(String[] args) {
        File file = new File("resources/games/choice.kif");
        System.out.println(file.exists());
        Parser test = new Parser(file);
        List<Gdl> rules = test.game.getRules();
        System.out.println(rules);
    }

    public Parser(File file) {
        String fileContents = FileUtils.readFileAsString(file);
        game = Game.createEphemeralGame(Game.preprocessRulesheet(fileContents));
    }

    private final Game game;


}
