package fodot.tests.specialcases;

import java.io.File;
import java.io.IOException;

import fodot.communication.PlayIdProcessor;

public class SpecialCaseIdpParseTest extends SpecialCasesTransformationTest {

	@Override
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		PlayIdProcessor processor = new PlayIdProcessor();
		try {
			processor.process(toParse);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
