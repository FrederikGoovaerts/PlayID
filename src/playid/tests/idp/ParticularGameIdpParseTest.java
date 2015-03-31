package playid.tests.idp;

import org.junit.Test;

import playid.domain.communication.PlayIdProcessor;
import playid.domain.communication.input.IdpFileWriter;

import java.io.File;
import java.io.IOException;

public class ParticularGameIdpParseTest {


	private static final String GAME_PATH = "resources/games/singleplayer/factoringApertureScience.kif";

    @Test
	public void game_test() {
		File toParse = new File(GAME_PATH);
		PlayIdProcessor processor = new PlayIdProcessor(toParse);
		try {
			processor.processSingleplayerGame(IdpFileWriter.createIDPFileBasedOn(toParse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
