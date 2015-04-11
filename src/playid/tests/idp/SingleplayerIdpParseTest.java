package playid.tests.idp;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import playid.domain.PlayIdProcessor;
import playid.domain.communication.input.IdpFileWriter;
import playid.tests.transformation.SingleplayerTransformationTest;
import playid.util.PlayIdReportCreator;
import playid.util.PlayIdReportCreator.PlayIdAction;

public class SingleplayerIdpParseTest extends SingleplayerTransformationTest {

	private static PlayIdReportCreator reportWriter;
	private static final File reportFolder = new File("resources/reports/");

	@BeforeClass
	public static void setupReport() {
		reportWriter = new PlayIdReportCreator();
	}

	@Override
	protected void testFor(String gameName) throws Exception {
		File toParse = toFile(gameName);
		PlayIdProcessor processor = new PlayIdProcessor(toParse);

		// Create the action to record
		PlayIdAction action = new PlayIdAction() {
			@Override
			public void runAction() throws Exception {
				processor.processSingleplayerGame(IdpFileWriter
						.createIDPFileBasedOn(toParse));
			}

			@Override
			public String getName() {
				return gameName;
			}
		};

		// Record exceptions happening during the action
		reportWriter.recordEventsOf(action);
	}

	@AfterClass
	public static void report() throws IOException {
		reportWriter.writeResultsTo(reportFolder, "singleplayer");
	}

}
