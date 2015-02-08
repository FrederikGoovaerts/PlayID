package fodot.tests.idp;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import fodot.communication.PlayIdProcessor;

public class EasyGamesIdpParseTest {


	private static final String GAMES_LOCATION = "resources/games/";
	private static final String DEFAULT_EXTENSION = ".kif";
	
	protected void process(String gameName) {
		File toParse = new File(GAMES_LOCATION + gameName + DEFAULT_EXTENSION);
		PlayIdProcessor processor = new PlayIdProcessor();
		try {
			processor.process(toParse);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void process_maze_test() {
		process("maze");
	}

	@Test
	public void process_blocks_test() {
		process("blocks");
	}

	@Test
	public void process_choice_test() {
		process("choice");
	}

}
