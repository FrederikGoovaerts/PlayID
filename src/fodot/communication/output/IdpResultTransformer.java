package fodot.communication.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fodot.exceptions.idp.*;
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
			throws IdpErrorException, UnsatisfiableIdpFileException, IllegalStateException {
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
	public void processResult() throws IdpErrorException, UnsatisfiableIdpFileException, IllegalStateException {
		List<String> linesToProcess = ParserUtil.trimElements(Arrays.asList(getTextualResult().split("\n")));

		Iterator<String> it = linesToProcess.iterator();
        String fullAnswer = "";
		while(it.hasNext()) {
			
			String line = it.next();
			fullAnswer += line;

			if (isNotOptimalSolution(line)) {
				throw new IdpNonOptimalSolutionException(line);
			} else if (isOutOfResources(line)){
				throw new OutOfResourcesException(line);
			} else if (isSyntaxError(line)) {
				throw new IdpSyntaxErrorException(line);
			} else if (isUnsatisfiable(line)) {
				throw new UnsatisfiableIdpFileException();
			} else if (isError(line)) {
				throw new IdpErrorException(line);
			} else if (isThrowAwayLine(line)){
				//No-op
			} else if (declaresNewStructure(line)) {
				
				//Take all the lines that needs to be given to the structure parser
				List<String> structureToParse = new ArrayList<String>();
				while (it.hasNext() && !isEndOfStructure(line)) {
					structureToParse.add(line);
					line = it.next();
					if (isOutOfResources(line)) {
						throw new OutOfResourcesException(line);
					}
				}
				structureToParse.add(line);
				
				addModel(FodotStructureParser.parse(getInputFodot(), structureToParse));
			} else {
				throw new IdpErrorException(line);
			}
		}
        if(fullAnswer == ""){
            throw new NoRealResultException();
        }


	}
	/**********************************************/

	private static final String NON_OPTIMAL_SOLUTION_REGEX = 
			"Warning: Verifying and/or autocompleting structure [0-9]+";
	private static boolean isNotOptimalSolution(String line) {
		return line.trim().matches(NON_OPTIMAL_SOLUTION_REGEX);
	}
	
	/**********************************************
	 *  Line recognizers
	 ***********************************************/
	private boolean isSyntaxError(String line) {
		String trimmed = line.trim();
		return trimmed.startsWith("Error: ");
	}
	
	private boolean isError(String line) {
		String trimmed = line.trim();
		return trimmed.startsWith("This application has requested the Runtime to terminate it in an unusual way.");
	}

	private static final String OUT_OF_RESOURCES_MESSAGE = "Out of resources";
	private boolean isOutOfResources(String line) {
		String trimmed = line.trim();
		return trimmed.startsWith(OUT_OF_RESOURCES_MESSAGE)
				|| trimmed.endsWith(OUT_OF_RESOURCES_MESSAGE)
				|| trimmed.startsWith("Error: memory exhausted");
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
