package playid.tests.idp;

import java.io.File;
import java.io.IOException;

import playid.domain.communication.PlayIdProcessor;
import playid.domain.communication.input.IdpFileWriter;
import playid.tests.transformation.EdgeCasesTransformationTest;

public class EdgeCasesIdpTest extends EdgeCasesTransformationTest {

	@Override
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		PlayIdProcessor processor = new PlayIdProcessor(toParse);
		try {
			processor.processSingleplayerGame(IdpFileWriter.createIDPFileBasedOn(toParse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
