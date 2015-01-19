package fodot.communication.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fodot.exceptions.idp.IdpParseException;
import fodot.exceptions.idp.OutOfResourcesException;
import fodot.exceptions.idp.UnsatisfiableIdpFileException;
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

	public IdpResultTransformer(IFodotFile inputFodot, String textResult)
			throws IdpParseException, UnsatisfiableIdpFileException, IllegalStateException {
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
	private void setTextualResult(String result) {
		this.textualResult = result;
	}	

	public IFodotFile getInputFodot() {
		return inputFodot;
	}

	private void setInputFodot(IFodotFile inputFodot) {
		this.inputFodot = inputFodot;
	}
	/**********************************************/
	

	/**********************************************
	 *  Models
	 ***********************************************/

	public List<FodotStructure> getModels() {
		return models;
	}
	
	private void addModel(FodotStructure model) {
		models.add(model);
	}

	/**********************************************/


	/**********************************************
	 *  Processing of the given text
	 ***********************************************/
	public void processResult() throws IdpParseException, UnsatisfiableIdpFileException, IllegalStateException {
		List<String> linesToProcess = ParserUtil.trimElements(Arrays.asList(getTextualResult().split("\n")));

		Iterator<String> it = linesToProcess.iterator();
		while(it.hasNext()) {
			
			String line = it.next();
			
			if (isParseError(line)) {
				throw new IdpParseException(line);
			} else if (isUnsatisfiable(line)) {
				throw new UnsatisfiableIdpFileException();
			} else if (isOutOfResources(line)){
				throw new OutOfResourcesException(line);
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
				throw new IllegalStateException("Can't process " + line);
			}
		}


	}
	/**********************************************/

	/**********************************************
	 *  Line recognizers
	 ***********************************************/
	private boolean isParseError(String line) {
		return line.startsWith("Error: ");
	}

	private boolean isOutOfResources(String line) {
		return line.trim().startsWith("Out of resources");
	}

	private boolean isUnsatisfiable(String line) {
		return line.trim().startsWith("Unsatisfiable");
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
}
