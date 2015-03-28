package fodot.tests.idp;

import java.io.File;
import java.io.IOException;

import fodot.communication.PlayIdProcessor;
import fodot.tests.transformation.EdgeCasesTransformationTest;

public class EdgeCasesIdpTest extends EdgeCasesTransformationTest {

	@Override
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		PlayIdProcessor processor = new PlayIdProcessor(toParse);
		try {
			processor.process(toParse);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
