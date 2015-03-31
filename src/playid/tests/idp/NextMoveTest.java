package playid.tests.idp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.ggp.base.util.gdl.grammar.GdlPool;
import org.junit.Test;

import playid.domain.PlayIdProcessor;
import playid.domain.gdl_transformers.movesequence.MoveSequence;
import playid.domain.gdl_transformers.movesequence.MoveSequence.MoveSequenceBuilder;

public class NextMoveTest {

	@Test
	public void process_blocks_with_first_step() {
		File toParse = new File(EasyGamesIdpParseTest.GAMES_LOCATION + "singleplayer/blocks.kif");
		PlayIdProcessor processor = new PlayIdProcessor(toParse);
		try {
			MoveSequenceBuilder builder = new MoveSequence.MoveSequenceBuilder();
			builder.addMove(0, GdlPool.getConstant("robot"), GdlPool.getFunction(GdlPool.getConstant("s"), Arrays.asList(GdlPool.getConstant("b"), GdlPool.getConstant("c"))));
			System.out.println(
					processor.calculateNextMove(builder.buildMoveSequence())
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
