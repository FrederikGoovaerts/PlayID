package fodot.tests.idp;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import fodot.communication.PlayIdProcessor;

public class SuccessfulGamesIdpParseText {

	private static final String GAMES_LOCATION = "resources/games/singleplayer/";
	
	protected void testFor(String gameName) {
		File toParse = new File(GAMES_LOCATION + gameName);
		PlayIdProcessor processor = new PlayIdProcessor();
		try {
			processor.process(toParse);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public void transform_blocks_test() {
		testFor("blocks.kif");
	}

	@Test
	public void transform_blocksWorld_test() {
		testFor("blocksWorld.kif");
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
	public void transform_duplicateStateMedium_test() {
		testFor("duplicateStateMedium.kif");
	}

	@Test
	public void transform_duplicateStateSmall_test() {
		testFor("duplicateStateSmall.kif");
	}

	@Test
	public void transform_factoringGeorgeForman_test() {
		testFor("factoringGeorgeForman.kif");
	}

	@Test
	public void transform_haystack_test() {
		testFor("haystack.kif");
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
	public void transform_lightsOn_test() {
		testFor("lightsOn.kif");
	}

	@Test
	public void transform_lightsOnParallel_test() {
		testFor("lightsOnParallel.kif");
	}

	@Test
	public void transform_lightsOnSimultaneous_test() {
		testFor("lightsOnSimultaneous.kif");
	}

	@Test
	public void transform_maze_test() {
		testFor("maze.kif");
	}

	@Test
	public void transform_pearls_test() {
		testFor("pearls.kif");
	}

	@Test
	public void transform_simultaneousWin2_test() {
		testFor("simultaneousWin2.kif");
	}

	@Test
	public void transform_snake_2008_tweaked_test() {
		testFor("snake_2008_tweaked.kif");
	}

	@Test
	public void transform_stateSpaceMedium_test() {
		testFor("stateSpaceMedium.kif");
	}

	@Test
	public void transform_stateSpaceSmall_test() {
		testFor("stateSpaceSmall.kif");
	}


}
