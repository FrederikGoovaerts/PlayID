package playid.domain.gdl_transformers.strategy;

import java.io.File;
import java.io.IOException;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.input.IIdpCaller;
import playid.domain.communication.input.IdpCaller;
import playid.domain.communication.output.GdlAnswerCalculator;
import playid.domain.exceptions.idp.IdpConnectionException;
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
	protected String callIdp(File idpFile) throws IdpConnectionException,
			IOException {
		IIdpCaller caller = new IdpCaller(false);
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
