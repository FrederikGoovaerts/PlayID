package fodot.tests.transformation;

import java.io.File;

import org.junit.Test;

import fodot.gdl_parser.GdlParser;

public class SingleplayerTransformationTest {

	private static final File GAMES_LOCATION = new File("resources/games/singleplayer/");

	/**
	 * Just parses to see if it throws an exception
	 * @param path	path to the file
	 */
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		GdlParser parser = new GdlParser(toParse);
		parser.run();
	}

	protected static File toFile(String gameName) {
		return new File(GAMES_LOCATION, gameName);
	}


	@Test
	public void transform_aipsrovers01_test() {
		testFor("aipsrovers01.kif");
	}

	@Test
	public void transform_asteroids_test() {
		testFor("asteroids.kif");
	}

	@Test
	public void transform_asteroidsParallel_test() {
		testFor("asteroidsParallel.kif");
	}

	@Test
	public void transform_asteroidsSerial_test() {
		testFor("asteroidsSerial.kif");
	}

	@Test
	public void transform_blocks_test() {
		testFor("blocks.kif");
	}

	@Test
	public void transform_blocksWorld_test() {
		testFor("blocksWorld.kif");
	}

	@Test
	public void transform_blocksWorldParallel_test() {
		testFor("blocksWorldParallel.kif");
	}

	@Test
	public void transform_blocksWorldSerial_test() {
		testFor("blocksWorldSerial.kif");
	}

	@Test
	public void transform_brain_teaser_extended_test() {
		testFor("brain_teaser_extended.kif");
	}

	@Test
	public void transform_buttons_test() {
		testFor("buttons.kif");
	}

	@Test
	public void transform_chineseCheckers1_test() {
		testFor("chineseCheckers1.kif");
	}

	@Test
	public void transform_circlesolitaire_test() {
		testFor("circlesolitaire.kif");
	}

	@Test
	public void transform_coins_test() {
		testFor("coins.kif");
	}

	@Test
	public void transform_coins_atomic_test() {
		testFor("coins_atomic.kif");
	}

	@Test
	public void transform_duplicateStateLarge_test() {
		testFor("duplicateStateLarge.kif");
	}

	@Test
	public void transform_duplicateStateMedium_test() {
		testFor("duplicateStateMedium.kif");
	}

	@Test
	public void transform_duplicateStateSmall_test() {
		testFor("duplicateStateSmall.kif");
	}

	@Test
	public void transform_eightPuzzle_test() {
		testFor("eightPuzzle.kif");
	}

	@Test
	public void transform_factoringEasyTurtleBrain_test() {
		testFor("factoringEasyTurtleBrain.kif");
	}

	@Test
	public void transform_factoringGeorgeForman_test() {
		testFor("factoringGeorgeForman.kif");
	}

	@Test
	public void transform_factoringImpossibleTurtleBrain_test() {
		testFor("factoringImpossibleTurtleBrain.kif");
	}

	@Test
	public void transform_factoringMediumTurtleBrain_test() {
		testFor("factoringMediumTurtleBrain.kif");
	}

	@Test
	public void transform_factoringMutuallyAssuredDestruction_test() {
		testFor("factoringMutuallyAssuredDestruction.kif");
	}

	@Test
	public void transform_firefighter_test() {
		testFor("firefighter.kif");
	}

	@Test
	public void transform_god_test() {
		testFor("god.kif");
	}

	@Test
	public void transform_hanoi_test() {
		testFor("hanoi.kif");
	}

	@Test
	public void transform_hanoi7_bugfix_test() {
		testFor("hanoi7_bugfix.kif");
	}

	@Test
	public void transform_hanoi_6_disks_test() {
		testFor("hanoi_6_disks.kif");
	}

	@Test
	public void transform_haystack_test() {
		testFor("haystack.kif");
	}

	@Test
	public void transform_hitori_test() {
		testFor("hitori.kif");
	}

	@Test
	public void transform_incredible_test() {
		testFor("incredible.kif");
	}

	@Test
	public void transform_kitten_escapes_from_fire_test() {
		testFor("kitten_escapes_from_fire.kif");
	}

	@Test
	public void transform_knightmove_test() {
		testFor("knightmove.kif");
	}

	@Test
	public void transform_knightsTour_test() {
		testFor("knightsTour.kif");
	}

	@Test
	public void transform_knightsTourLarge_test() {
		testFor("knightsTourLarge.kif");
	}

	@Test
	public void transform_lightsOn_test() {
		testFor("lightsOn.kif");
	}

	@Test
	public void transform_lightsOnParallel_test() {
		testFor("lightsOnParallel.kif");
	}

	@Test
	public void transform_lightsOnSimul4_test() {
		testFor("lightsOnSimul4.kif");
	}

	@Test
	public void transform_lightsOnSimultaneous_test() {
		testFor("lightsOnSimultaneous.kif");
	}

	@Test
	public void transform_lightsOut_test() {
		testFor("lightsOut.kif");
	}

	@Test
	public void transform_max_knights_test() {
		testFor("max_knights.kif");
	}

	@Test
	public void transform_maze_test() {
		testFor("maze.kif");
	}

	@Test
	public void transform_mummymaze1p_test() {
		testFor("mummymaze1p.kif");
	}

	@Test
	public void transform_pancakes_test() {
		testFor("pancakes.kif");
	}

	@Test
	public void transform_pancakes6_test() {
		testFor("pancakes6.kif");
	}

	@Test
	public void transform_pancakes88_test() {
		testFor("pancakes88.kif");
	}

	@Test
	public void transform_pearls_test() {
		testFor("pearls.kif");
	}

	@Test
	public void transform_peg_test() {
		testFor("peg.kif");
	}

	@Test
	public void transform_pegEuro_test() {
		testFor("pegEuro.kif");
	}

	@Test
	public void transform_queens_test() {
		testFor("queens.kif");
	}

	@Test
	public void transform_ruleDepthExponential_test() {
		testFor("ruleDepthExponential.kif");
	}

	@Test
	public void transform_simultaneousWin2_test() {
		testFor("simultaneousWin2.kif");
	}

	@Test
	public void transform_slidingpieces_test() {
		testFor("slidingpieces.kif");
	}

	@Test
	public void transform_snakeParallel_test() {
		testFor("snakeParallel.kif");
	}

	@Test
	public void transform_snake_2008_test() {
		testFor("snake_2008.kif");
	}

	@Test
	public void transform_snake_2008_tweaked_test() {
		testFor("snake_2008_tweaked.kif");
	}

	@Test
	public void transform_snake_2009_test() {
		testFor("snake_2009.kif");
	}

	@Test
	public void transform_snake_2009_big_test() {
		testFor("snake_2009_big.kif");
	}

	@Test
	public void transform_stateSpaceLarge_test() {
		testFor("stateSpaceLarge.kif");
	}

	@Test
	public void transform_stateSpaceMedium_test() {
		testFor("stateSpaceMedium.kif");
	}

	@Test
	public void transform_stateSpaceSmall_test() {
		testFor("stateSpaceSmall.kif");
	}
	
	@Test
	public void transform_sudoku_test() {
		testFor("sudoku.kif");
	}
	
	@Test
	public void transform_survival_test() {
		testFor("survival.kif");
	}
	
	@Test
	public void transform_tpeg_test() {
		testFor("tpeg.kif");
	}
	
	@Test
	public void transform_troublemaker01_test() {
		testFor("troublemaker01.kif");
	}
	
	@Test
	public void transform_troublemaker02_test() {
		testFor("troublemaker02.kif");
	}
	
	@Test
	public void transform_untwistycomplex2_test() {
		testFor("untwistycomplex2.kif");
	}	
	
	@Test
	public void transform_wargame01_test() {
		testFor("wargame01.kif");
	}		
	
	@Test
	public void transform_wargame02_test() {
		testFor("wargame02.kif");
	}		
	
	@Test
	public void transform_wargame03_test() {
		testFor("wargame03.kif");
	}	
	


}
