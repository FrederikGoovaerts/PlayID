package playid.domain;

import java.io.File;
import java.io.IOException;

import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.Role;

import playid.domain.communication.input.IdpFileWriter;
import playid.domain.exceptions.idp.IdpConnectionException;
import playid.domain.exceptions.idp.IdpErrorException;
import playid.domain.exceptions.idp.NoValidModelsException;
import playid.domain.exceptions.idp.UnsatisfiableIdpFileException;
import playid.domain.exceptions.playid.PlayIdArgumentException;
import playid.domain.gdl_transformers.GdlParser;
import playid.domain.gdl_transformers.movesequence.MoveSequence;
import playid.domain.gdl_transformers.strategy.GameStrategySelector;
import playid.domain.gdl_transformers.strategy.IGameStrategy;
import playid.domain.gdl_transformers.strategy.SinglePlayerStrategy;

public class PlayIdProcessor {
	/**********************************************
	 * Constructors
	 ***********************************************/

	//ARGUMENTS
	private final Game game;
	private final Role role;
	private final IGameStrategy strategy;

	public PlayIdProcessor(Game argGame, Role argRole) {
		this.game = argGame;
		this.role = argRole != null ? argRole : GdlParser
				.findFirstRole(argGame);
		this.strategy = GameStrategySelector.selectStrategy(game, role);
	}

	public PlayIdProcessor(File gdlFile) {
		this(GdlParser.parseGame(gdlFile), null);
	}

	/**********************************************/

	/**********************************************
	 * Process
	 ***********************************************/
	public MoveSequence calculateNextMove(MoveSequence movesSoFar) {
		return strategy.calculateNextMove(movesSoFar);
	}
	
	//TODO: Remove file argument. Find way to store temporal files.
	public MoveSequence processSingleplayerGame(File outputFile) throws IOException,
			IdpConnectionException, IdpErrorException,
			UnsatisfiableIdpFileException, IllegalStateException,
			NoValidModelsException {
		return new SinglePlayerStrategy(game, role).calculateBestSolution(outputFile);
	}
	
	/**********************************************
	 * Main method
	 ***********************************************/

	public static void main(String[] args) throws IOException {
		validateArguments(args);
		File gdlFile = new File(args[0]);
		PlayIdProcessor processor = new PlayIdProcessor(gdlFile);
		
		File outputFile = IdpFileWriter.createIDPFileBasedOn(gdlFile);
		System.out.println(processor.processSingleplayerGame(outputFile));
	}
	
	public static void validateArguments(String[] args) {
		if (args.length <= 0) {
			throw new PlayIdArgumentException(
					"Please give the uri of a valid GDL file to the PlayID processor.",
					0, 1);
		}
		if (!args[0].contains(".kif")) {
			throw new PlayIdArgumentException(
					"The given uri must lead to a .kif file.");
		}
		File gdlFile = new File(args[0]);
		if (!gdlFile.exists()) {
			throw new PlayIdArgumentException(
					"The given uri must lead to a existing file.");
		}
	}

	/**********************************************/

}
