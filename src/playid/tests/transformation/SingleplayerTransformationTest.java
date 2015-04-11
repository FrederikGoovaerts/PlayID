package playid.tests.transformation;

import java.io.File;

import org.junit.Test;

import playid.domain.gdl_transformers.GdlParser;

public class SingleplayerTransformationTest {

	private static final File GAMES_LOCATION = new File("resources/games/singleplayer/");

	/**
	 * Just parses to see if it throws an exception
	 * @param path	path to the file
	 * @throws Exception 
	 */
	protected void testFor(String gameName) throws Exception {
		File toParse = toFile(gameName);
		GdlParser parser = new GdlParser(toParse);
		parser.run();
	}

	protected static File toFile(String gameName) {
		return new File(GAMES_LOCATION, gameName);
	}


	@Test
	public void transform_aipsrovers01_test() throws Exception {
		testFor("aipsrovers01.kif");
	}

	@Test
	public void transform_asteroids_test() throws Exception {
		testFor("asteroids.kif");
	}

	@Test
	public void transform_asteroidsParallel_test() throws Exception {
		testFor("asteroidsParallel.kif");
	}

	@Test
	public void transform_asteroidsSerial_test() throws Exception {
		testFor("asteroidsSerial.kif");
	}

	@Test
	public void transform_blocks_test() throws Exception {
		testFor("blocks.kif");
	}

	@Test
	public void transform_blocksWorld_test() throws Exception {
		testFor("blocksWorld.kif");
	}

	@Test
	public void transform_blocksWorldParallel_test() throws Exception {
		testFor("blocksWorldParallel.kif");
	}

	@Test
	public void transform_blocksWorldSerial_test() throws Exception {
		testFor("blocksWorldSerial.kif");
	}

	@Test
	public void transform_brain_teaser_extended_test() throws Exception {
		testFor("brain_teaser_extended.kif");
	}

	@Test
	public void transform_buttons_test() throws Exception {
		testFor("buttons.kif");
	}

	@Test
	public void transform_chineseCheckers1_test() throws Exception {
		testFor("chineseCheckers1.kif");
	}

	@Test
	public void transform_circlesolitaire_test() throws Exception {
		testFor("circlesolitaire.kif");
	}

	@Test
	public void transform_coins_test() throws Exception {
		testFor("coins.kif");
	}

	@Test
	public void transform_coins_atomic_test() throws Exception {
		testFor("coins_atomic.kif");
	}

	@Test
	public void transform_duplicateStateLarge_test() throws Exception {
		testFor("duplicateStateLarge.kif");
	}

	@Test
	public void transform_duplicateStateMedium_test() throws Exception {
		testFor("duplicateStateMedium.kif");
	}

	@Test
	public void transform_duplicateStateSmall_test() throws Exception {
		testFor("duplicateStateSmall.kif");
	}

	@Test
	public void transform_eightPuzzle_test() throws Exception {
		testFor("eightPuzzle.kif");
	}

    @Test
    public void transform_factoringApertureScience_test() throws Exception {
        testFor("factoringApertureScience.kif");
    }

	@Test
	public void transform_factoringEasyTurtleBrain_test() throws Exception {
		testFor("factoringEasyTurtleBrain.kif");
	}

	@Test
	public void transform_factoringGeorgeForman_test() throws Exception {
		testFor("factoringGeorgeForman.kif");
	}

	@Test
	public void transform_factoringImpossibleTurtleBrain_test() throws Exception {
		testFor("factoringImpossibleTurtleBrain.kif");
	}

	@Test
	public void transform_factoringMediumTurtleBrain_test() throws Exception {
		testFor("factoringMediumTurtleBrain.kif");
	}

	@Test
	public void transform_factoringMutuallyAssuredDestruction_test() throws Exception {
		testFor("factoringMutuallyAssuredDestruction.kif");
	}

	@Test
	public void transform_firefighter_test() throws Exception {
		testFor("firefighter.kif");
	}

	@Test
	public void transform_god_test() throws Exception {
		testFor("god.kif");
	}

	@Test
	public void transform_hanoi_test() throws Exception {
		testFor("hanoi.kif");
	}

	@Test
	public void transform_hanoi7_bugfix_test() throws Exception {
		testFor("hanoi7_bugfix.kif");
	}

	@Test
	public void transform_hanoi_6_disks_test() throws Exception {
		testFor("hanoi_6_disks.kif");
	}

	@Test
	public void transform_haystack_test() throws Exception {
		testFor("haystack.kif");
	}

	@Test
	public void transform_hitori_test() throws Exception {
		testFor("hitori.kif");
	}

	@Test
	public void transform_incredible_test() throws Exception {
		testFor("incredible.kif");
	}

	@Test
	public void transform_kitten_escapes_from_fire_test() throws Exception {
		testFor("kitten_escapes_from_fire.kif");
	}

	@Test
	public void transform_knightmove_test() throws Exception {
		testFor("knightmove.kif");
	}

	@Test
	public void transform_knightsTour_test() throws Exception {
		testFor("knightsTour.kif");
	}

	@Test
	public void transform_knightsTourLarge_test() throws Exception {
		testFor("knightsTourLarge.kif");
	}

	@Test
	public void transform_lightsOn_test() throws Exception {
		testFor("lightsOn.kif");
	}

	@Test
	public void transform_lightsOnParallel_test() throws Exception {
		testFor("lightsOnParallel.kif");
	}

	@Test
	public void transform_lightsOnSimul4_test() throws Exception {
		testFor("lightsOnSimul4.kif");
	}

	@Test
	public void transform_lightsOnSimultaneous_test() throws Exception {
		testFor("lightsOnSimultaneous.kif");
	}

	@Test
	public void transform_lightsOut_test() throws Exception {
		testFor("lightsOut.kif");
	}

	@Test
	public void transform_max_knights_test() throws Exception {
		testFor("max_knights.kif");
	}

	@Test
	public void transform_maze_test() throws Exception {
		testFor("maze.kif");
	}

	@Test
	public void transform_mummymaze1p_test() throws Exception {
		testFor("mummymaze1p.kif");
	}

	@Test
	public void transform_pancakes_test() throws Exception {
		testFor("pancakes.kif");
	}

	@Test
	public void transform_pancakes6_test() throws Exception {
		testFor("pancakes6.kif");
	}

	@Test
	public void transform_pancakes88_test() throws Exception {
		testFor("pancakes88.kif");
	}

	@Test
	public void transform_pearls_test() throws Exception {
		testFor("pearls.kif");
	}

	@Test
	public void transform_peg_test() throws Exception {
		testFor("peg.kif");
	}

	@Test
	public void transform_pegEuro_test() throws Exception {
		testFor("pegEuro.kif");
	}

	@Test
	public void transform_queens_test() throws Exception {
		testFor("queens.kif");
	}

	@Test
	public void transform_ruleDepthExponential_test() throws Exception {
		testFor("ruleDepthExponential.kif");
	}

	@Test
	public void transform_simultaneousWin2_test() throws Exception {
		testFor("simultaneousWin2.kif");
	}

	@Test
	public void transform_slidingpieces_test() throws Exception {
		testFor("slidingpieces.kif");
	}

	@Test
	public void transform_snakeParallel_test() throws Exception {
		testFor("snakeParallel.kif");
	}

	@Test
	public void transform_snake_2008_test() throws Exception {
		testFor("snake_2008.kif");
	}

	@Test
	public void transform_snake_2008_tweaked_test() throws Exception {
		testFor("snake_2008_tweaked.kif");
	}

	@Test
	public void transform_snake_2009_test() throws Exception {
		testFor("snake_2009.kif");
	}

	@Test
	public void transform_snake_2009_big_test() throws Exception {
		testFor("snake_2009_big.kif");
	}

	@Test
	public void transform_stateSpaceLarge_test() throws Exception {
		testFor("stateSpaceLarge.kif");
	}

	@Test
	public void transform_stateSpaceMedium_test() throws Exception {
		testFor("stateSpaceMedium.kif");
	}

	@Test
	public void transform_stateSpaceSmall_test() throws Exception {
		testFor("stateSpaceSmall.kif");
	}
	
	@Test
	public void transform_sudoku_test() throws Exception {
		testFor("sudoku.kif");
	}
	
	@Test
	public void transform_survival_test() throws Exception {
		testFor("survival.kif");
	}
	
	@Test
	public void transform_tpeg_test() throws Exception {
		testFor("tpeg.kif");
	}
	
	@Test
	public void transform_troublemaker01_test() throws Exception {
		testFor("troublemaker01.kif");
	}
	
	@Test
	public void transform_troublemaker02_test() throws Exception {
		testFor("troublemaker02.kif");
	}
	
	@Test
	public void transform_twisty_passages_test() throws Exception {
		testFor("twisty-passages.kif");
	}
	
	@Test
	public void transform_untwistycomplex2_test() throws Exception {
		testFor("untwistycomplex2.kif");
	}	
	
	@Test
	public void transform_wargame01_test() throws Exception {
		testFor("wargame01.kif");
	}		
	
	@Test
	public void transform_wargame02_test() throws Exception {
		testFor("wargame02.kif");
	}		
	
	@Test
	public void transform_wargame03_test() throws Exception {
		testFor("wargame03.kif");
	}	
	


}
