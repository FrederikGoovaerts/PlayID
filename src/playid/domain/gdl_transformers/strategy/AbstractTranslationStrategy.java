package playid.domain.gdl_transformers.strategy;

import java.io.File;
import java.io.IOException;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.input.IIdpCaller;
import playid.domain.communication.input.IdpCaller;
import playid.domain.exceptions.idp.IdpConnectionException;

public abstract class AbstractTranslationStrategy implements IGameTranslationStrategy{

	private final Game game;
	private final Role role;
	
	public AbstractTranslationStrategy(Game game, Role role) {
		super();
		this.game = game;
		this.role = role;
	}

		
	
	public Game getGame() {
		return game;
	}
	
	public Role getRole() {
		return role;
	}



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
