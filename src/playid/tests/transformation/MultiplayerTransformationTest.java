package playid.tests.transformation;

import org.junit.Test;

import playid.domain.gdl_transformers.GdlParser;

import java.io.File;

public class MultiplayerTransformationTest {

	private static final File GAMES_LOCATION = new File("resources/games/multiplayer/");

	/**
	 * Just parses to see if it throws an exception
	 * @param path	path to the file
	 */
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		GdlParser parser = new GdlParser(toParse);
		parser.run();
	}

	protected static File toFile(String gameName) {
		return new File(GAMES_LOCATION, gameName);
	}


	@Test
	public void transform_connectFour_test() {
		testFor("connectFour.kif");
	}

	@Test
	public void transform_ticTacToe_test() {
		testFor("ticTacToe.kif");
	}

}
