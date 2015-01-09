package fodot.communication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fodot.communication.gdloutput.GdlActionPrinter;
import fodot.communication.gdloutput.IActionOutputter;
import fodot.communication.input.IIdpCaller;
import fodot.communication.input.IdpCaller;
import fodot.communication.input.IdpFileWriter;
import fodot.communication.output.GdlAction;
import fodot.communication.output.GdlAnswerCalculator;
import fodot.communication.output.IdpResultTransformer;
import fodot.gdl_parser.Parser;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.FodotStructure;

public class PlayIdProcessor {
	
	public void process(File gdlFile) {
		
		//Convert GDL to IDP
		Parser parser = new Parser(gdlFile);
		IFodotFile parsedFodotFile = parser.getParsedFodot();
		
		
		//Create IDPfile in same location as GDL file
		File idpFile = IdpFileWriter.createIDPFileBasedOn(gdlFile);
		IdpFileWriter.writeToIDPFile(parsedFodotFile, idpFile);

		//Make IDP solve it
		IIdpCaller caller = new IdpCaller();
		String idpResult = "";
		try {
			idpResult = caller.callIDP(idpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Process results
		IdpResultTransformer transformer = new IdpResultTransformer(parsedFodotFile, idpResult);
		List<FodotStructure> models = transformer.getModels();
		
		//Do something if we didn't find any models
		if (transformer.hasErrorOccured()) {
			System.out.println("An error has occured when running IDP");
		} else if (models.size() == 0) {
			System.out.println("No models found");
		}
		
		//Transform a solution 
		GdlAnswerCalculator answerer = new GdlAnswerCalculator(models);
		List<GdlAction> actions = answerer.generateActionSequence();
		
		//Output it
		IActionOutputter outputter = new GdlActionPrinter(actions);
		outputter.output();
	}
	
	public static void main(String[] args) {
		new PlayIdProcessor().process( new File("resources/games/maze.kif"));
	}
	
	
}
