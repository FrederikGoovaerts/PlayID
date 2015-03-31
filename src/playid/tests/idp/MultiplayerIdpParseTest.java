package playid.tests.idp;

import org.ggp.base.util.Pair;
import org.ggp.base.util.files.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import playid.domain.communication.PlayIdProcessor;
import playid.domain.communication.input.IdpFileWriter;
import playid.domain.exceptions.answer.AnswerException;
import playid.domain.exceptions.fodot.FodotException;
import playid.domain.exceptions.gdl.GdlTransformationException;
import playid.domain.exceptions.idp.*;
import playid.domain.gdl_transformers.FodotGameFactory;
import playid.tests.transformation.MultiplayerTransformationTest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MultiplayerIdpParseTest extends MultiplayerTransformationTest {

	private static final boolean generateReport = true;


	private static List<String> success;
	private static List<String> gdlTransformError;
	private static List<String> fodotTransformError;
	private static List<String> idpSyntaxError;
	private static List<String> idpError;
	private static List<String> outOfResources;
	private static List<String> unsatisfiable;
	private static List<String> noModels;
	private static List<String> answerError;
	private static Map<String, Exception> otherExceptions;
	private static List<Pair<String,List<String>>> lists;

	@BeforeClass
	public static void setupLists() {
		gdlTransformError = new ArrayList<String>();
		fodotTransformError = new ArrayList<String>();
		idpError = new ArrayList<String>();
		idpSyntaxError = new ArrayList<String>();
		outOfResources = new ArrayList<String>();
		unsatisfiable = new ArrayList<String>();
		noModels = new ArrayList<String>();
		answerError = new ArrayList<String>();
		success = new ArrayList<String>();
		otherExceptions = new HashMap<String, Exception>();

		lists = new ArrayList<Pair<String, List<String>>>();
		lists.add(Pair.of("Successful", success));
		lists.add(Pair.of("GDL transformation error", gdlTransformError));
		lists.add(Pair.of("FO(.) transformation error", fodotTransformError));
		lists.add(Pair.of("IDP syntax error", idpSyntaxError));
		lists.add(Pair.of("IDP error", idpError));
		lists.add(Pair.of("Out of Resources", outOfResources));
		lists.add(Pair.of("Unsatisfiable", unsatisfiable));
		lists.add(Pair.of("No models found", noModels));
		lists.add(Pair.of("Answerer error", answerError));
	}	

	@Override
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		PlayIdProcessor processor = new PlayIdProcessor(toParse);
		try {
			processor.processSingleplayerGame(IdpFileWriter.createIDPFileBasedOn(toParse));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GdlTransformationException e) {
			gdlTransformError.add(gameName);
			throw e;
		} catch (FodotException e) {
			fodotTransformError.add(gameName);
			throw e;
		} catch (UnsatisfiableIdpFileException e) {
			unsatisfiable.add(gameName);
			throw e;
		} catch (NoValidModelsException e) {
			noModels.add(gameName);
			throw e;
		} catch (IdpErrorException e) {
			idpError.add(gameName);
			throw e;
        } catch (IdpSyntaxErrorException e) {
            idpSyntaxError.add(gameName);
            throw e;
		} catch (OutOfResourcesException e) {
			outOfResources.add(gameName);
			throw e;
		} catch (AnswerException e) {
			answerError.add(gameName);
			throw e;
		} catch (Exception e) {
			otherExceptions.put(gameName, e);
			throw e;
		}
		success.add(gameName);
	}

	@AfterClass
	public static void report() throws IOException {
		if (generateReport) {
			// Create file with name based on date
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
			DateFormat dateFormatMinutes = new SimpleDateFormat("mm");
			Date date = new Date();
			File reportLocation = new File("resources/reports/multi " + dateFormat.format(date) + "h" + dateFormatMinutes.format(date) +".txt");
			reportLocation.createNewFile();

			StringBuilder b = new StringBuilder();
			DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			b.append("REPORT FOR PROCESSIBILITY OF MULTIPLAYER GAMES\n");
			b.append("\n===================================================\n");
			b.append(pad("Created on ",dateFormat2.format(date))+"\n");
			b.append(pad("Default time limit: ", Integer.toString(FodotGameFactory.DEFAULT_IDP_TIME_LIMIT)) + "s\n");
            b.append(pad("Default turn limit: ", Integer.toString(FodotGameFactory.DEFAULT_TURN_LIMIT)) + "\n");
			b.append(  "===================================================\n\n\n");
			for (Pair<String,List<String>> p : lists) {
				b.append(formatAmount(p.left, p.right));
			}
			b.append(formatAmount("Other error:", otherExceptions.keySet()));

			b.append("\n===================================================\n");
			b.append(  "===================== Details =====================\n");
			b.append(  "===================================================\n\n");
			for (Pair<String,List<String>> p : lists) {
				Collections.sort(p.right);
				b.append(p.left + ": \n" + p.right + "\n\n");
			}
			b.append("Others:\n");
			for (String s : otherExceptions.keySet()) {
				b.append(s + ": " +otherExceptions.get(s).getClass().getSimpleName()+"\n");
			}
			System.out.println(b.toString());

			FileUtils.writeStringToFile(reportLocation, b.toString());
		}
	}	

	private static String formatAmount(String explanation, Collection<String> list) {
		return pad(explanation + ": ", padLeft(Integer.toString(list.size()),3)) + "\n\n";
	}

	private static final int TAB_LENGTH = 32;
	private static String pad(String left, String right) {
		return padRight(left, TAB_LENGTH) + right;
	}

	private static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);  
	}
	
	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
}
