package fodot.communication.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fodot.fodot_parser.FodotStructureParser;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.FodotStructure;
import fodot.util.ParserUtil;

/**
 * This class will read the results of IDP and puts it in useful maps
 * @author Thomas
 *
 */
public class IdpResultTransformer {
	private String textualResult;
	private IFodotFile inputFodot;
	private List<FodotStructure> models = new ArrayList<FodotStructure>();
	private boolean errorOccured;

	public IdpResultTransformer(IFodotFile inputFodot, String textResult) {
		super();
		setInputFodot(inputFodot);
		setTextualResult(textResult);
		processResult();
	}

	/**********************************************
	 *  Textualresult
	 ***********************************************/

	public String getTextualResult() {
		return textualResult;
	}
	public void setTextualResult(String result) {
		this.textualResult = result;
	}	

	public IFodotFile getInputFodot() {
		return inputFodot;
	}

	public void setInputFodot(IFodotFile inputFodot) {
		this.inputFodot = inputFodot;
	}
	/**********************************************/
	

	/**********************************************
	 *  Models
	 ***********************************************/

	public List<FodotStructure> getModels() {
		return models;
	}
	
	public void addModel(FodotStructure model) {
		models.add(model);
	}

	/**********************************************/


	/**********************************************
	 *  Processing of the given text
	 ***********************************************/
	public void processResult() {
		List<String> linesToProcess = ParserUtil.trimElements(Arrays.asList(getTextualResult().split("\n")));

		Iterator<String> it = linesToProcess.iterator();
		while(it.hasNext()) {
			
			String line = it.next();
			
			if (isError(line)) {
				setErrorOccured(true);
				break;
			} else if (isThrowAwayLine(line)){
				//No-op
			} else if (declaresNewStructure(line)) {
				
				//Take all the lines that needs to be given to the structure parser
				List<String> structureToParse = new ArrayList<String>();
				while (it.hasNext() && !isEndOfStructure(line)) {
					structureToParse.add(line);
					line = it.next();
				}
				structureToParse.add(line);
				
				addModel(FodotStructureParser.parse(getInputFodot(), structureToParse));
			} else {
				throw new IllegalArgumentException("Can't process " + line);
			}
		}


	}
	/**********************************************/

	/**********************************************
	 *  Line recognizers
	 ***********************************************/
	private boolean isError(String line) {
		return line.startsWith("Error: ");
	}

	private boolean isEndOfStructure(String line) {
		return line.trim().equals("}");
	}

	private boolean declaresNewStructure(String line) {
		return line.startsWith("structure");
	}

	private boolean isThrowAwayLine(String line) {
		if (line == null)
			return true;

		line = line.trim();
		return line.equals("")
				|| line.startsWith("Warning:")
				|| line.startsWith("====")
				|| line.startsWith("Number of models")
				|| line.startsWith("Model");
	}
	/**********************************************/
 
	/**********************************************
	 *  Error checkers
	 ***********************************************/
	public boolean hasErrorOccured() {
		return errorOccured;
	}

	private void setErrorOccured(boolean errorOccured) {
		this.errorOccured = errorOccured;
	}
	/**********************************************/
}
