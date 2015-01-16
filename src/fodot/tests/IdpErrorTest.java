package fodot.tests;

import java.io.File;

import fodot.communication.PlayIdProcessor;

public class IdpErrorTest extends ParserExceptionsTest {

	@Override
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		PlayIdProcessor processor = new PlayIdProcessor();
		processor.process(toParse);
	}

}
