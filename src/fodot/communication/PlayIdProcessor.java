package fodot.communication;

import java.io.File;
import java.util.List;

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
		IIdpCaller caller = null; //TODO: een klasse hiervoor maken
		String idpResult = caller.callIDP(idpFile);

		//Temporal
//		IdpResultTransformer transformer = new IdpResultTransformer(
//				(new Parser(new File("resources/games/blocks.kif"))).getParsedFodot(), "Number of models: 1"
//				+ "\nModel 1"
//				+ "\n======="
//				+ "\nstructure  : V {"
//				+ "\n  ScoreType = { 0..100 }"
//				+ "\n  Time = { 0..20 }"
//				+ "\n  C_clear = { 0,c_a; 0,c_b; 0,c_c; 1,c_a; 1,c_b; 2,c_a }"
//				+ "\n  C_on = { 1,c_b,c_c; 2,c_a,c_b; 2,c_b,c_c }"
//				+ "\n  C_step = { 0,c_2; 1,c_3; 2,c_4 }"
//				+ "\n  C_table = { 0,c_a; 0,c_b; 0,c_c; 1,c_a; 1,c_c; 2,c_c }"
//				+ "\n  I_clear = { c_b; c_c }"
//				+ "\n  I_on = { c_c,c_a }"
//				+ "\n  I_step = { c_1 }"
//				+ "\n  I_table = { c_a; c_b }"
//				+ "\n  clear = { 0,c_b; 0,c_c; 1,c_a; 1,c_b; 1,c_c; 2,c_a; 2,c_b; 3,c_a }"
//				+ "\n  do = { 0,p_robot,u(c_c,c_a); 1,p_robot,s(c_b,c_c); 2,p_robot,s(c_a,c_b) }"
//				+ "\n  on = { 0,c_c,c_a; 2,c_b,c_c; 3,c_a,c_b; 3,c_b,c_c }"
//				+ "\n  step = { 0,c_1; 1,c_2; 2,c_3; 3,c_4 }"
//				+ "\n  succ = { c_1,c_2; c_2,c_3; c_3,c_4 }"
//				+ "\n  table = { 0,c_a; 0,c_b; 1,c_a; 1,c_b; 1,c_c; 2,c_a; 2,c_c; 3,c_c }"
//				+ "\n  terminalTime = { 3 }"
//				+ "\n  Next = { 0->1; 1->2; 2->3 }"
//				+ "\n  Score = { p_robot->100 }"
//				+ "\n  Start = 0"
//				+ "\n	}");
		//Process results
		IdpResultTransformer transformer = new IdpResultTransformer(parsedFodotFile, idpResult);
		List<IdpModel> models = transformer.getModels();
		
		//Do something if we didn't find any models
		if (models.size() == 0) {
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
		System.out.println((new PlayIdProcessor()).process(null));
	}
	
	
}
