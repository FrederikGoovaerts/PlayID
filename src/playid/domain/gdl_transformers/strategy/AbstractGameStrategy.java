package playid.domain.gdl_transformers.strategy;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.input.IdpCaller;
import playid.domain.communication.input.IdpFileWriter;
import playid.domain.communication.output.GdlAnswerCalculator;
import playid.domain.communication.output.IdpResultTransformer;
import playid.domain.exceptions.idp.IdpConnectionException;
import playid.domain.fodot.file.IFodotFile;
import playid.domain.fodot.structure.FodotStructure;
import playid.domain.gdl_transformers.GdlVocabulary;
import playid.domain.gdl_transformers.first_phase.GdlTypeIdentifier;
import playid.domain.gdl_transformers.second_phase.GdlFodotTransformer;
import playid.domain.gdl_transformers.visitor.GdlInspector;

public abstract class AbstractGameStrategy implements IGameStrategy{

	//ARGUMENTS
	private final Game game;
	private final Role role;

	//TRANSLATION DATA
	private final GdlTypeIdentifier identifier = new GdlTypeIdentifier();
	private final GdlVocabulary gdlVocabulary;
	private final GdlFodotTransformer fodotTransformer;
	private final GdlAnswerCalculator answerCalculator;

	public AbstractGameStrategy(Game game, Role role) {
		super();
		this.game = game;
		this.role = role;


		//START COLLECTING TRANSLATION DATA
		/*
		 * First phase:
		 * Create datastructure containing basic info about the game:
		 * The types, the predicates with their correct typing, sorting of
		 * the dynamic and static predicates...
		 */

		GdlInspector.inspect(game, identifier.createTransformer());
		gdlVocabulary = identifier.generateTranslationData();

		/*
		 * Second phase:
		 * Visit every rule and translate it
		 */
		fodotTransformer = new GdlFodotTransformer(gdlVocabulary);
		GdlInspector.inspect( game, fodotTransformer );

		answerCalculator = new GdlAnswerCalculator(role, gdlVocabulary);

	}


	//GETTERS

	public Game getGame() {
		return game;
	}

	public Role getRole() {
		return role;
	}

	public GdlTypeIdentifier getIdentifier() {
		return identifier;
	}

	public GdlVocabulary getGdlVocabulary() {
		return gdlVocabulary;
	}

	public GdlFodotTransformer getFodotTransformer() {
		return fodotTransformer;
	}

	public GdlAnswerCalculator getAnswerCalculator() {
		return answerCalculator;
	}



	//IDP CALLS
	protected List<FodotStructure> generateModels(IFodotFile fodotFile, File outputFile) throws IdpConnectionException, IOException {
		// Create IDPfile in same location as GDL file
		IdpFileWriter.writeToIDPFile(fodotFile, outputFile);

		// Make IDP solve it
		String idpResult = callIdp(outputFile);

		// TEMPORAL IDP BUG FIX TODO delete me when warning is fixed
		idpResult = fixResult(idpResult);
		
		IdpResultTransformer resultTransformer = new IdpResultTransformer(
				fodotFile, idpResult);
		List<FodotStructure> models = resultTransformer.getModels();


		return models;
	}

	protected Collection<FodotStructure> generateModels(IFodotFile fodotFile) throws IOException {
		File tempFile = File.createTempFile("playid", ".idp").getAbsoluteFile();
		return generateModels(fodotFile, tempFile);
	}

	protected String callIdp(File idpFile) throws IdpConnectionException,
	IOException {
		IdpCaller caller = new IdpCaller(false);
		String idpResult = caller.callIDP(idpFile);
		return idpResult;
	}

	protected String fixResult(String idpResult) {
		String stupidWarning = "Warning: XSB support is not available. Option xsb is ignored.\n\n";
		if (idpResult.contains(stupidWarning)) {
			idpResult = idpResult.replaceAll(stupidWarning, "");
		}
		return idpResult;
	}
}
