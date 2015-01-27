package fodot.tests.singleplayer;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.Pair;
import org.ggp.base.util.files.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import fodot.communication.PlayIdProcessor;
import fodot.exceptions.answer.AnswerException;
import fodot.exceptions.fodot.FodotException;
import fodot.exceptions.gdl.GdlTransformationException;
import fodot.exceptions.idp.IdpParseException;
import fodot.exceptions.idp.NoValidModelsException;
import fodot.exceptions.idp.OutOfResourcesException;
import fodot.exceptions.idp.UnsatisfiableIdpFileException;

public class SingleplayerIdpParseTest extends SingleplayerTransformationTest {

	private static List<String> success;
	private static List<String> gdlTransformError;
	private static List<String> fodotTransformError;
	private static List<String> idpParseError;
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
		idpParseError = new ArrayList<String>();
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
		lists.add(Pair.of("IDP syntax error", idpParseError));
		lists.add(Pair.of("Out of Resources", outOfResources));
		lists.add(Pair.of("Unsatisfiable", unsatisfiable));
		lists.add(Pair.of("No models found", noModels));
		lists.add(Pair.of("Answerer error", answerError));
	}	
	
	@Override
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		PlayIdProcessor processor = new PlayIdProcessor();
		try {
			processor.process(toParse);
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
		} catch (IdpParseException e) {
			idpParseError.add(gameName);
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
	}
	
	@AfterClass
	public static void report() throws IOException {
		// Create file with name based on date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		DateFormat dateFormatMinutes = new SimpleDateFormat("mm");
		Date date = new Date();
		File reportLocation = new File("resources/reports/singleplayer " + dateFormat.format(date) + "h" + dateFormatMinutes.format(date) +".txt");
		reportLocation.createNewFile();
		
		StringBuilder b = new StringBuilder();
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		b.append("Report for processibility of singleplayer games\n");
		b.append("------------------- created on "+dateFormat2.format(date)+"\n\n\n");
		for (Pair<String,List<String>> p : lists) {
			b.append(formatAmount(p.left, p.right));
		}
		b.append(formatAmount("Other error:", otherExceptions.keySet()));
		
		b.append("\n\n\n=====================\n=====  Details  =====\n=====================\n\n");
		for (Pair<String,List<String>> p : lists) {
			b.append(p.left + ": \n" + p.right + "\n\n");
		}
		b.append("Others:");
		for (String s : otherExceptions.keySet()) {
			b.append(s + ": " +otherExceptions.get(s).getClass().getSimpleName());
		}
        System.out.println(b.toString());
		
        FileUtils.writeStringToFile(reportLocation, b.toString());
	}	
	
	private static String formatAmount(String explanation, Collection<String> list) {
		int tabLength = 32;
		return padRight(explanation + ": " , tabLength) + list.size() + "\n\n";
	}
	
	private static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}
}
