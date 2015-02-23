package fodot.tests.idp;

import fodot.communication.PlayIdProcessor;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ParticularGameIdpParseTest {


	private static final String GAME_PATH = "resources/games/singleplayer/pearls.kif";

    @Test
	public void game_test() {
		File toParse = new File(GAME_PATH);
		PlayIdProcessor processor = new PlayIdProcessor();
		try {
			processor.process(toParse);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
