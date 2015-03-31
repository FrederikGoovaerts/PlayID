package playid.tests.idp;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import playid.domain.communication.PlayIdProcessor;
import playid.domain.communication.input.IdpFileWriter;

public class EasyGamesIdpParseTest {


	private static final String GAMES_LOCATION = "resources/games/";
	private static final String DEFAULT_EXTENSION = ".kif";
	
	protected void process(String gameName) {
		File toParse = new File(GAMES_LOCATION + gameName + DEFAULT_EXTENSION);
		PlayIdProcessor processor = new PlayIdProcessor(toParse);
		try {
			processor.processSingleplayerGame(IdpFileWriter.createIDPFileBasedOn(toParse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void process_maze_test() {
		process("singleplayer/maze");
	}

	@Test
	public void process_blocks_test() {
		process("singleplayer/blocks");
	}

	@Test
	public void process_choice_test() {
		process("custom/choice");
	}

    @Test
    public void process_buttons_test() {
        process("singleplayer/buttons");
    }

}
