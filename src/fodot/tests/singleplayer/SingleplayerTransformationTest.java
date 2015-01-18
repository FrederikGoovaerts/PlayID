package fodot.tests.singleplayer;

import java.io.File;

import org.junit.Test;

import fodot.gdl_parser.Parser;

public class SingleplayerTransformationTest {

	private static final File GAMES_LOCATION = new File("resources/games/singleplayer/");
	private static final String DEFAULT_EXTENSION = ".kif";

	/**
	 * Just parses to see if it throws an exception
	 * @param path	path to the file
	 */
	protected static void transform(String gameName) {
		File toParse = toFile(gameName);
		Parser parser = new Parser(toParse);
		parser.run();
	}

	protected static File toFile(String gameName) {
		return new File(GAMES_LOCATION, gameName);
	}


	@Test
	public void transform_aipsrovers01_test() {
		transform("aipsrovers01.kif");
	}

	@Test
	public void transform_asteroids_test() {
		transform("asteroids.kif");
	}

	@Test
	public void transform_asteroidsParallel_test() {
		transform("asteroidsParallel.kif");
	}

	@Test
	public void transform_asteroidsSerial_test() {
		transform("asteroidsSerial.kif");
	}

	@Test
	public void transform_blocks_test() {
		transform("blocks.kif");
	}

	@Test
	public void transform_blocksWorld_test() {
		transform("blocksWorld.kif");
	}

	@Test
	public void transform_blocksWorldParallel_test() {
		transform("blocksWorldParallel.kif");
	}

	@Test
	public void transform_blocksWorldSerial_test() {
		transform("blocksWorldSerial.kif");
	}

	@Test
	public void transform_brain_teaser_extended_test() {
		transform("brain_teaser_extended.kif");
	}

	@Test
	public void transform_buttons_test() {
		transform("buttons.kif");
	}

	@Test
	public void transform_chineseCheckers1_test() {
		transform("chineseCheckers1.kif");
	}

	@Test
	public void transform_circlesolitaire_test() {
		transform("circlesolitaire.kif");
	}

	@Test
	public void transform_coins_test() {
		transform("coins.kif");
	}

	@Test
	public void transform_coins_atomic_test() {
		transform("coins_atomic.kif");
	}

	@Test
	public void transform_duplicateStateLarge_test() {
		transform("duplicateStateLarge.kif");
	}

	@Test
	public void transform_duplicateStateMedium_test() {
		transform("duplicateStateMedium.kif");
	}

	@Test
	public void transform_duplicateStateSmall_test() {
		transform("duplicateStateSmall.kif");
	}

	@Test
	public void transform_eightPuzzle_test() {
		transform("eightPuzzle.kif");
	}

	@Test
	public void transform_factoringEasyTurtleBrain_test() {
		transform("factoringEasyTurtleBrain.kif");
	}

	@Test
	public void transform_factoringGeorgeForman_test() {
		transform("factoringGeorgeForman.kif");
	}

	@Test
	public void transform_factoringImpossibleTurtleBrain_test() {
		transform("factoringImpossibleTurtleBrain.kif");
	}

	@Test
	public void transform_factoringMediumTurtleBrain_test() {
		transform("factoringMediumTurtleBrain.kif");
	}

	@Test
	public void transform_factoringMutuallyAssuredDestruction_test() {
		transform("factoringMutuallyAssuredDestruction.kif");
	}

	@Test
	public void transform_firefighter_test() {
		transform("firefighter.kif");
	}

	@Test
	public void transform_god_test() {
		transform("god.kif");
	}

	@Test
	public void transform_hanoi_test() {
		transform("hanoi.kif");
	}

	@Test
	public void transform_hanoi7_bugfix_test() {
		transform("hanoi7_bugfix.kif");
	}

	@Test
	public void transform_hanoi_6_disks_test() {
		transform("hanoi_6_disks.kif");
	}

	@Test
	public void transform_haystack_test() {
		transform("haystack.kif");
	}

	@Test
	public void transform_hitori_test() {
		transform("hitori.kif");
	}

	@Test
	public void transform_incredible_test() {
		transform("incredible.kif");
	}

	@Test
	public void transform_kitten_escapes_from_fire_test() {
		transform("kitten_escapes_from_fire.kif");
	}

	@Test
	public void transform_knightmove_test() {
		transform("knightmove.kif");
	}

	@Test
	public void transform_knightsTour_test() {
		transform("knightsTour.kif");
	}

	@Test
	public void transform_knightsTourLarge_test() {
		transform("knightsTourLarge.kif");
	}

	@Test
	public void transform_lightsOn_test() {
		transform("lightsOn.kif");
	}

	@Test
	public void transform_lightsOnParallel_test() {
		transform("lightsOnParallel.kif");
	}

	@Test
	public void transform_lightsOnSimul4_test() {
		transform("lightsOnSimul4.kif");
	}

	@Test
	public void transform_lightsOnSimultaneous_test() {
		transform("lightsOnSimultaneous.kif");
	}

	@Test
	public void transform_lightsOut_test() {
		transform("lightsOut.kif");
	}

	@Test
	public void transform_max_knights_test() {
		transform("max_knights.kif");
	}

	@Test
	public void transform_maze_test() {
		transform("maze.kif");
	}

	@Test
	public void transform_mummymaze1p_test() {
		transform("mummymaze1p.kif");
	}

	@Test
	public void transform_pancakes_test() {
		transform("pancakes.kif");
	}

	@Test
	public void transform_pancakes6_test() {
		transform("pancakes6.kif");
	}

	@Test
	public void transform_pancakes88_test() {
		transform("pancakes88.kif");
	}

	@Test
	public void transform_pearls_test() {
		transform("pearls.kif");
	}

	@Test
	public void transform_peg_test() {
		transform("peg.kif");
	}

	@Test
	public void transform_pegEuro_test() {
		transform("pegEuro.kif");
	}

	@Test
	public void transform_queens_test() {
		transform("queens.kif");
	}

	@Test
	public void transform_ruleDepthExponential_test() {
		transform("ruleDepthExponential.kif");
	}

	@Test
	public void transform_ruleDepthLinear_test() {
		transform("ruleDepthLinear.kif");
	}

	@Test
	public void transform_simultaneousWin2_test() {
		transform("simultaneousWin2.kif");
	}

	@Test
	public void transform_slidingpieces_test() {
		transform("slidingpieces.kif");
	}

	@Test
	public void transform_snakeParallel_test() {
		transform("snakeParallel.kif");
	}

	@Test
	public void transform_snake_2008_test() {
		transform("snake_2008.kif");
	}

	@Test
	public void transform_snake_2008_tweaked_test() {
		transform("snake_2008_tweaked.kif");
	}

	@Test
	public void transform_snake_2009_test() {
		transform("snake_2009.kif");
	}

	@Test
	public void transform_snake_2009_big_test() {
		transform("snake_2009_big.kif");
	}

	@Test
	public void transform_stateSpaceLarge_test() {
		transform("stateSpaceLarge.kif");
	}

	@Test
	public void transform_stateSpaceMedium_test() {
		transform("stateSpaceMedium.kif");
	}

	@Test
	public void transform_stateSpaceSmall_test() {
		transform("stateSpaceSmall.kif");
	}


}
