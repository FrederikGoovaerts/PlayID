package fodot.communication;

import java.io.File;
import java.util.List;

import fodot.gdl_parser.Parser;
import fodot.objects.file.IFodotFile;

public class PlayIdProcessor {
	public void process(File gdlFile) {
		
		//Convert GDL to IDP
		Parser parser = new Parser(gdlFile);
		IFodotFile parsedFodotFile = parser.getParsedFodot();
		
		
		//Create IDPfile in same location as GDL file
		File idpFile = IdpFileWriter.createIDPFileBasedOn(gdlFile);
		IdpFileWriter.writeToIDPFile(parsedFodotFile, idpFile);

		//Make IDP solve it
		IIdpCaller caller = null; //TODO: een klasse hiervoor maken
		String idpResult = caller.callIDP(idpFile);
		
		//Process results
		IdpResultTransformer transformer = new IdpResultTransformer(parsedFodotFile, idpResult);
		List<IdpModel> models = transformer.getModels();
		
	}
}
