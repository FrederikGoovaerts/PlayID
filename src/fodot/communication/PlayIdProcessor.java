package fodot.communication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fodot.communication.input.IIdpCaller;
import fodot.communication.input.IdpCaller;
import fodot.communication.input.IdpFileWriter;
import fodot.communication.output.GdlAction;
import fodot.communication.output.GdlAnswerer;
import fodot.communication.output.IdpModel;
import fodot.communication.output.IdpResultTransformer;
import fodot.gdl_parser.Parser;
import fodot.objects.file.IFodotFile;

public class PlayIdProcessor {
	
	public String process(File gdlFile) {
		
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
		List<IdpModel> models = transformer.getModels();
		
		//Do something if we didn't find any models
		if (transformer.hasErrorOccured()) {
			System.out.println("An error has occured when running IDP");
		} else if (models.size() == 0) {
			System.out.println("No models found");
		}
		
		//Transform a solution 
		GdlAnswerer answerer = new GdlAnswerer(models);
		List<GdlAction> actions = answerer.generateActionSequence();
		
		StringBuilder builder = new StringBuilder();
		for(GdlAction ac : actions) {
			builder.append(ac.toString() + "\n");
		}
		return builder.toString();
	}
	
	public static void main(String[] args) {
		System.out.println((new PlayIdProcessor()).process( new File("resources/games/blocks.kif")));
	}
	
	
}
