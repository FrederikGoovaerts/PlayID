package playid.tests.idp;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.statemachine.Role;
import org.junit.Test;

import playid.domain.PlayIdProcessor;
import playid.domain.gdl_transformers.GdlParser;
import playid.domain.gdl_transformers.movesequence.MoveSequence;
import playid.domain.gdl_transformers.movesequence.MoveSequence.MoveSequenceBuilder;

public class NextMoveTest {

	@Test
	public void process_blocks_with_correct_first_step() throws IOException {

		Game blocks = GdlParser.parseGame(new File("resources/games/singleplayer/blocks.kif"));
		Role role = GdlParser.findFirstRole(blocks);
		PlayIdProcessor player = new PlayIdProcessor(blocks, role);

		MoveSequenceBuilder builder = new MoveSequenceBuilder();
		builder.addMove(0, role.getName(), buildFunction("u", "c", "a"));
		MoveSequence inputMoves = builder.buildMoveSequence();

		MoveSequence plannedMoves = player.calculateNextMove(inputMoves);
		
		//Expected
		MoveSequenceBuilder builder2 = new MoveSequenceBuilder();
		builder2.addMove(0, role.getName(), buildFunction("u", "c", "a"));
		builder2.addMove(1, role.getName(), buildFunction("s", "b", "c"));
		builder2.addMove(2, role.getName(), buildFunction("s", "a", "b"));
		MoveSequence expectedMoves = builder2.buildMoveSequence();
		
		assertEquals(expectedMoves, plannedMoves);
		
	}
	
	@Test
	public void process_blocks_with_alternative_first_step() throws IOException {
		File toParse = new File(EasyGamesIdpParseTest.GAMES_LOCATION + "singleplayer/blocks.kif");
		PlayIdProcessor processor = new PlayIdProcessor(toParse);
		MoveSequenceBuilder builder = new MoveSequence.MoveSequenceBuilder();
		builder.addMove(0, GdlPool.getConstant("robot"), buildFunction("s", "b", "c"));
		System.out.println(
				processor.calculateNextMove(builder.buildMoveSequence())
				);
	}
	
	private GdlFunction buildFunction(String name, String... arguments) {
		List<GdlTerm> body = new ArrayList<GdlTerm>();
		for (String arg : arguments) {
			body.add(GdlPool.getConstant(arg));
		}
		
		return GdlPool.getFunction(GdlPool.getConstant(name), body);
	}
}
