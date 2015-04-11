package playid.util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ggp.base.util.Pair;
import org.ggp.base.util.files.FileUtils;

import playid.domain.exceptions.answer.AnswerException;
import playid.domain.exceptions.fodot.FodotException;
import playid.domain.exceptions.gdl.GdlTransformationException;
import playid.domain.exceptions.idp.IdpErrorException;
import playid.domain.exceptions.idp.IdpSyntaxErrorException;
import playid.domain.exceptions.idp.NoValidModelsException;
import playid.domain.exceptions.idp.OutOfResourcesException;
import playid.domain.exceptions.idp.UnsatisfiableIdpFileException;
import playid.domain.gdl_transformers.FodotGameFactory;

public class PlayIdReportCreator {

	public interface PlayIdAction {
		public void runAction() throws Exception;
		public String getName();
	}

	private List<String> success = new ArrayList<String>();
	private List<String> gdlTransformError = new ArrayList<String>();
	private List<String> fodotTransformError = new ArrayList<String>();
	private List<String> idpSyntaxError = new ArrayList<String>();
	private List<String> idpError = new ArrayList<String>();
	private List<String> outOfResources = new ArrayList<String>();
	private List<String> unsatisfiable = new ArrayList<String>();
	private List<String> noModels = new ArrayList<String>();
	private List<String> answerError = new ArrayList<String>();
	private Map<String, Exception> otherExceptions = new HashMap<String, Exception>();;
	private List<Pair<String,List<String>>> lists;


	public PlayIdReportCreator() {		
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

	public void recordEventsOf(PlayIdAction action) throws Exception {
		String gameName = action.getName();
		try {
			action.runAction();
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

	public void writeResultsTo(File folder, String name) throws IOException {
		// Create file with name based on date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		DateFormat dateFormatMinutes = new SimpleDateFormat("mm");
		Date date = new Date();
		File reportLocation = new File(folder, name + " " + dateFormat.format(date) + "h" + dateFormatMinutes.format(date) +".txt");
		reportLocation.createNewFile();

		//Write it
		System.out.println("writing results to " + reportLocation);
		FileUtils.writeStringToFile(reportLocation, this.toString());
	}
	
	@Override
	public String toString() {

		Date date = Date.from(Instant.now());
		
		StringBuilder b = new StringBuilder();
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		b.append("REPORT FOR PROCESSIBILITY OF SINGLEPLAYER GAMES\n");
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
		
		return b.toString();
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
