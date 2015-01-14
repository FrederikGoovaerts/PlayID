package fodot.tests;

import java.io.File;

import org.junit.Test;

import fodot.communication.PlayIdProcessor;

public class IdpProcessorabilityTest {


	private static final String GAMES_LOCATION = "resources/games/";
	private static final String DEFAULT_EXTENSION = ".kif";
	
	private void process(String gameName) {
		File toParse = new File(GAMES_LOCATION + gameName + DEFAULT_EXTENSION);
		PlayIdProcessor processor = new PlayIdProcessor();
		processor.process(toParse);
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
