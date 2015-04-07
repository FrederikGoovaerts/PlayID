package playid.tests.transformation;

import org.junit.Test;

import playid.domain.gdl_transformers.GdlParser;

import java.io.File;

public class MultiplayerTransformationTest {

	private static final File GAMES_LOCATION = new File(
			"resources/games/multiplayer/");

	/**
	 * Just parses to see if it throws an exception
	 * 
	 * @param path
	 *            path to the file
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
	public void process_2pffa_test() {
		testFor("2pffa.kif");
	}

	@Test
	public void process_2pffa_zerosum_test() {
		testFor("2pffa_zerosum.kif");
	}

	@Test
	public void process_2pttc_test() {
		testFor("2pttc.kif");
	}

	@Test
	public void process_3pConnectFour_test() {
		testFor("3pConnectFour.kif");
	}

	@Test
	public void process_3pffa_test() {
		testFor("3pffa.kif");
	}

	@Test
	public void process_3pttc_test() {
		testFor("3pttc.kif");
	}

	@Test
	public void process_4pffa_test() {
		testFor("4pffa.kif");
	}

	@Test
	public void process_4pttc_test() {
		testFor("4pttc.kif");
	}

	@Test
	public void process_ad_game_2x2_test() {
		testFor("ad_game_2x2.kif");
	}

	@Test
	public void process_alexChess_test() {
		testFor("alexChess.kif");
	}

	@Test
	public void process_amazons_test() {
		testFor("amazons.kif");
	}

	@Test
	public void process_amazonsSuicide_test() {
		testFor("amazonsSuicide.kif");
	}

	@Test
	public void process_amazonsTorus_test() {
		testFor("amazonsTorus.kif");
	}

	@Test
	public void process_battle_test() {
		testFor("battle.kif");
	}

	@Test
	public void process_beatMania_test() {
		testFor("beatMania.kif");
	}

	@Test
	public void process_biddingTicTacToe_test() {
		testFor("biddingTicTacToe.kif");
	}

	@Test
	public void process_biddingTicTacToe_10coins_test() {
		testFor("biddingTicTacToe_10coins.kif");
	}

	@Test
	public void process_blobwars_test() {
		testFor("blobwars.kif");
	}

	@Test
	public void process_blocker_test() {
		testFor("blocker.kif");
	}

	@Test
	public void process_blockerParallel_test() {
		testFor("blockerParallel.kif");
	}

	@Test
	public void process_blockerSerial_test() {
		testFor("blockerSerial.kif");
	}

	@Test
	public void process_blocks2player_test() {
		testFor("blocks2player.kif");
	}

	@Test
	public void process_blokbox_simple_test() {
		testFor("blokbox_simple.kif");
	}

	@Test
	public void process_bomberman2p_test() {
		testFor("bomberman2p.kif");
	}

	@Test
	public void process_brawl_test() {
		testFor("brawl.kif");
	}

	@Test
	public void process_breakthrough_test() {
		testFor("breakthrough.kif");
	}

	@Test
	public void process_breakthroughHoles_test() {
		testFor("breakthroughHoles.kif");
	}

	@Test
	public void process_breakthroughSmall_test() {
		testFor("breakthroughSmall.kif");
	}

	@Test
	public void process_breakthroughSmallHoles_test() {
		testFor("breakthroughSmallHoles.kif");
	}

	@Test
	public void process_breakthroughSuicide_test() {
		testFor("breakthroughSuicide.kif");
	}

	@Test
	public void process_breakthroughWalls_test() {
		testFor("breakthroughWalls.kif");
	}

	@Test
	public void process_bunk_t_test() {
		testFor("bunk_t.kif");
	}

	@Test
	public void process_catcha_mouse_test() {
		testFor("catcha_mouse.kif");
	}

	@Test
	public void process_cephalopodMicro_test() {
		testFor("cephalopodMicro.kif");
	}

	@Test
	public void process_checkLines_test() {
		testFor("checkLines.kif");
	}

	@Test
	public void process_checkers_test() {
		testFor("checkers.kif");
	}

	@Test
	public void process_checkers_cylinder_mustjump_test() {
		testFor("checkers-cylinder-mustjump.kif");
	}

	@Test
	public void process_checkers_mustjump_test() {
		testFor("checkers-mustjump.kif");
	}

	@Test
	public void process_checkers_mustjump_torus_test() {
		testFor("checkers-mustjump-torus.kif");
	}

	@Test
	public void process_checkers_newgoals_test() {
		testFor("checkers-newgoals.kif");
	}

	@Test
	public void process_checkers_suicide_cylinder_mustjump_test() {
		testFor("checkers-suicide-cylinder-mustjump.kif");
	}

	@Test
	public void process_checkersBarrelNoKings_test() {
		testFor("checkersBarrelNoKings.kif");
	}

	@Test
	public void process_checkersSmall_test() {
		testFor("checkersSmall.kif");
	}

	@Test
	public void process_checkersTiny_test() {
		testFor("checkersTiny.kif");
	}

	@Test
	public void process_checkersTorus_test() {
		testFor("checkersTorus.kif");
	}

	@Test
	public void process_checkersTorusNoKings_test() {
		testFor("checkersTorusNoKings.kif");
	}

	@Test
	public void process_chess_test() {
		testFor("chess.kif");
	}

	@Test
	public void process_chickentictactoe_test() {
		testFor("chickentictactoe.kif");
	}

	@Test
	public void process_chickentoetictac_test() {
		testFor("chickentoetictac.kif");
	}

	@Test
	public void process_chineseCheckers2_test() {
		testFor("chineseCheckers2.kif");
	}

	@Test
	public void process_chineseCheckers3_test() {
		testFor("chineseCheckers3.kif");
	}

	@Test
	public void process_chineseCheckers4_test() {
		testFor("chineseCheckers4.kif");
	}

	@Test
	public void process_chineseCheckers6_test() {
		testFor("chineseCheckers6.kif");
	}

	@Test
	public void process_chinook_test() {
		testFor("chinook.kif");
	}

	@Test
	public void process_choicethroughalt_test() {
		testFor("choicethroughalt.kif");
	}

	@Test
	public void process_chomp_test() {
		testFor("chomp.kif");
	}

	@Test
	public void process_cittaceot_test() {
		testFor("cittaceot.kif");
	}

	@Test
	public void process_colonelBlotto_test() {
		testFor("colonelBlotto.kif");
	}

	@Test
	public void process_colonelBlottoVariant_test() {
		testFor("colonelBlottoVariant.kif");
	}

	@Test
	public void process_colonelBlottoVariant2_test() {
		testFor("colonelBlottoVariant2.kif");
	}

	@Test
	public void process_coloredtrails_test() {
		testFor("coloredtrails.kif");
	}

	@Test
	public void process_conn4_test() {
		testFor("conn4.kif");
	}

	@Test
	public void process_connect4_test() {
		testFor("connect4.kif");
	}

	@Test
	public void process_connect5_test() {
		testFor("connect5.kif");
	}

	@Test
	public void process_connectFour_test() {
		testFor("connectFour.kif");
	}

	@Test
	public void process_connectFourLarge_test() {
		testFor("connectFourLarge.kif");
	}

	@Test
	public void process_connectFourLarger_test() {
		testFor("connectFourLarger.kif");
	}

	@Test
	public void process_connectFourSimultaneous_test() {
		testFor("connectFourSimultaneous.kif");
	}

	@Test
	public void process_connectFourSuicide_test() {
		testFor("connectFourSuicide.kif");
	}

	@Test
	public void process_crisscross_test() {
		testFor("crisscross.kif");
	}

	@Test
	public void process_crissrace_test() {
		testFor("crissrace.kif");
	}

	@Test
	public void process_crossers3_test() {
		testFor("crossers3.kif");
	}

	@Test
	public void process_cubicup_test() {
		testFor("cubicup.kif");
	}

	@Test
	public void process_cubicup_3player_test() {
		testFor("cubicup_3player.kif");
	}

	@Test
	public void process_cylinder_checkers_test() {
		testFor("cylinder-checkers.kif");
	}

	@Test
	public void process_dotsAndBoxes_test() {
		testFor("dotsAndBoxes.kif");
	}

	@Test
	public void process_dotsAndBoxesSuicide_test() {
		testFor("dotsAndBoxesSuicide.kif");
	}

	@Test
	public void process_double_tictactoe_dengji_test() {
		testFor("double_tictactoe_dengji.kif");
	}

	@Test
	public void process_doubletictactoe_test() {
		testFor("doubletictactoe.kif");
	}

	@Test
	public void process_doubletoetictac_test() {
		testFor("doubletoetictac.kif");
	}

	@Test
	public void process_dualConnect4_test() {
		testFor("dualConnect4.kif");
	}

	@Test
	public void process_endgame_test() {
		testFor("endgame.kif");
	}

	@Test
	public void process_englishDraughts_test() {
		testFor("englishDraughts.kif");
	}

	@Test
	public void process_eotcatcit_test() {
		testFor("eotcatcit.kif");
	}

	@Test
	public void process_eotcitcit_test() {
		testFor("eotcitcit.kif");
	}

	@Test
	public void process_escortLatch_test() {
		testFor("escortLatch.kif");
	}

	@Test
	public void process_four_way_battle_test() {
		testFor("four_way_battle.kif");
	}

	@Test
	public void process_ghostMaze2p_test() {
		testFor("ghostMaze2p.kif");
	}

	@Test
	public void process_golden_rectangle_test() {
		testFor("golden_rectangle.kif");
	}

	@Test
	public void process_gt_attrition_test() {
		testFor("gt_attrition.kif");
	}

	@Test
	public void process_gt_centipede_test() {
		testFor("gt_centipede.kif");
	}

	@Test
	public void process_gt_chicken_test() {
		testFor("gt_chicken.kif");
	}

	@Test
	public void process_gt_coordination_test() {
		testFor("gt_coordination.kif");
	}

	@Test
	public void process_gt_dollar_test() {
		testFor("gt_dollar.kif");
	}

	@Test
	public void process_gt_prisoner_test() {
		testFor("gt_prisoner.kif");
	}

	@Test
	public void process_gt_staghunt_test() {
		testFor("gt_staghunt.kif");
	}

	@Test
	public void process_gt_ultimatum_test() {
		testFor("gt_ultimatum.kif");
	}

	@Test
	public void process_guess_test() {
		testFor("guess.kif");
	}

	@Test
	public void process_hallway_test() {
		testFor("hallway.kif");
	}

	@Test
	public void process_hex_test() {
		testFor("hex.kif");
	}
	
	@Test
	public void process_hodgepodge_test() {
		testFor("hodgepodge.kif");
	}

	@Test
	public void process_kalaha_2009_test() {
		testFor("kalaha_2009.kif");
	}

	@Test
	public void process_knightThrough_test() {
		testFor("knightThrough.kif");
	}

	@Test
	public void process_knightazons_test() {
		testFor("knightazons.kif");
	}

	@Test
	public void process_knightfight_test() {
		testFor("knightfight.kif");
	}

	@Test
	public void process_knightwar_test() {
		testFor("knightwar.kif");
	}

	@Test
	public void process_laikLee_hex_test() {
		testFor("laikLee_hex.kif");
	}

	@Test
	public void process_merrills_test() {
		testFor("merrills.kif");
	}

	@Test
	public void process_minichess_test() {
		testFor("minichess.kif");
	}

	@Test
	public void process_minichess_evilconjuncts_test() {
		testFor("minichess-evilconjuncts.kif");
	}

	@Test
	public void process_mummyMaze2p_2007_test() {
		testFor("mummyMaze2p_2007.kif");
	}

	@Test
	public void process_mummymaze2p_test() {
		testFor("mummymaze2p.kif");
	}

	@Test
	public void process_nim1_test() {
		testFor("nim1.kif");
	}

	@Test
	public void process_nim2_test() {
		testFor("nim2.kif");
	}

	@Test
	public void process_nim3_test() {
		testFor("nim3.kif");
	}

	@Test
	public void process_nim4_test() {
		testFor("nim4.kif");
	}

	@Test
	public void process_nineBoardTicTacToe_test() {
		testFor("nineBoardTicTacToe.kif");
	}

	@Test
	public void process_numbertictactoe_test() {
		testFor("numbertictactoe.kif");
	}

	@Test
	public void process_onestep_test() {
		testFor("onestep.kif");
	}

	@Test
	public void process_othello_comp2007_test() {
		testFor("othello-comp2007.kif");
	}

	@Test
	public void process_othelloHoles_test() {
		testFor("othelloHoles.kif");
	}

	@Test
	public void process_othelloSuicide_test() {
		testFor("othelloSuicide.kif");
	}

	@Test
	public void process_pacman2p_test() {
		testFor("pacman2p.kif");
	}

	@Test
	public void process_pacman3p_test() {
		testFor("pacman3p.kif");
	}

	@Test
	public void process_pawnToQueen_test() {
		testFor("pawnToQueen.kif");
	}

	@Test
	public void process_pawnWhopping_test() {
		testFor("pawnWhopping.kif");
	}

	@Test
	public void process_pentago_test() {
		testFor("pentago.kif");
	}

	@Test
	public void process_pentagoSuicide_test() {
		testFor("pentagoSuicide.kif");
	}

	@Test
	public void process_point_grab_test() {
		testFor("point_grab.kif");
	}

	@Test
	public void process_quad_test() {
		testFor("quad.kif");
	}

	@Test
	public void process_quad_5x5_test() {
		testFor("quad_5x5.kif");
	}

	@Test
	public void process_quad_7x7_test() {
		testFor("quad_7x7.kif");
	}

	@Test
	public void process_quarto_test() {
		testFor("quarto.kif");
	}

	@Test
	public void process_quartoSuicide_test() {
		testFor("quartoSuicide.kif");
	}

	@Test
	public void process_qyshinsu_test() {
		testFor("qyshinsu.kif");
	}

	@Test
	public void process_racer_test() {
		testFor("racer.kif");
	}

	@Test
	public void process_racer4_test() {
		testFor("racer4.kif");
	}

	@Test
	public void process_racetrackcorridor_test() {
		testFor("racetrackcorridor.kif");
	}

	@Test
	public void process_reversi_test() {
		testFor("reversi.kif");
	}

	@Test
	public void process_roshambo2_test() {
		testFor("roshambo2.kif");
	}

	@Test
	public void process_sheepAndWolf_test() {
		testFor("sheepAndWolf.kif");
	}

	@Test
	public void process_skirmish_test() {
		testFor("skirmish.kif");
	}

	@Test
	public void process_skirmishFinal1_test() {
		testFor("skirmishFinal1.kif");
	}

	@Test
	public void process_skirmishFinal2_test() {
		testFor("skirmishFinal2.kif");
	}

	@Test
	public void process_skirmishFinal3_test() {
		testFor("skirmishFinal3.kif");
	}

	@Test
	public void process_skirmishHoles_test() {
		testFor("skirmishHoles.kif");
	}

	@Test
	public void process_skirmishNew_test() {
		testFor("skirmishNew.kif");
	}

	@Test
	public void process_skirmishZeroSum_test() {
		testFor("skirmishZeroSum.kif");
	}

	@Test
	public void process_slaughter_test() {
		testFor("slaughter.kif");
	}

	@Test
	public void process_smallest_4player_test() {
		testFor("smallest_4player.kif");
	}

	@Test
	public void process_snake2p_test() {
		testFor("snake2p.kif");
	}

	@Test
	public void process_snakeAssemblit_test() {
		testFor("snakeAssemblit.kif");
	}

	@Test
	public void process_speedChess_test() {
		testFor("speedChess.kif");
	}

	@Test
	public void process_strangeSkirmish_test() {
		testFor("strangeSkirmish.kif");
	}

	@Test
	public void process_sum15_test() {
		testFor("sum15.kif");
	}

	@Test
	public void process_switches_test() {
		testFor("switches.kif");
	}

	@Test
	public void process_ticTacHeaven_test() {
		testFor("ticTacHeaven.kif");
	}

	@Test
	public void process_ticTacHeavenFC_test() {
		testFor("ticTacHeavenFC.kif");
	}

	@Test
	public void process_ticTacToe_test() {
		testFor("ticTacToe.kif");
	}

	@Test
	public void process_ticTacToeLarge_test() {
		testFor("ticTacToeLarge.kif");
	}

	@Test
	public void process_ticTacToeLargeSuicide_test() {
		testFor("ticTacToeLargeSuicide.kif");
	}

	@Test
	public void process_ticTacToeNoVars_test() {
		testFor("ticTacToeNoVars.kif");
	}

	@Test
	public void process_ticTacToeParallel_test() {
		testFor("ticTacToeParallel.kif");
	}

	@Test
	public void process_ticTacToeSerial_test() {
		testFor("ticTacToeSerial.kif");
	}

	@Test
	public void process_ticTicToe_test() {
		testFor("ticTicToe.kif");
	}

	@Test
	public void process_ticblock_test() {
		testFor("ticblock.kif");
	}

	@Test
	public void process_tictactoe_init1_test() {
		testFor("tictactoe-init1.kif");
	}

	@Test
	public void process_tictactoe_test() {
		testFor("tictactoe.kif");
	}

	@Test
	public void process_tictactoe_3d_2player_test() {
		testFor("tictactoe_3d_2player.kif");
	}

	@Test
	public void process_tictactoe_3d_6player_test() {
		testFor("tictactoe_3d_6player.kif");
	}

	@Test
	public void process_tictactoe_3d_small_2player_test() {
		testFor("tictactoe_3d_small_2player.kif");
	}

	@Test
	public void process_tictactoe_3d_small_6player_test() {
		testFor("tictactoe_3d_small_6player.kif");
	}

	@Test
	public void process_tictactoe_3player_test() {
		testFor("tictactoe_3player.kif");
	}

	@Test
	public void process_tictactoe_orthogonal_test() {
		testFor("tictactoe_orthogonal.kif");
	}

	@Test
	public void process_tictactoex9_test() {
		testFor("tictactoex9.kif");
	}

	@Test
	public void process_toetictac_test() {
		testFor("toetictac.kif");
	}

	@Test
	public void process_ttcc4_test() {
		testFor("ttcc4.kif");
	}

	@Test
	public void process_ttcc4_2player_test() {
		testFor("ttcc4_2player.kif");
	}

	@Test
	public void process_ttcc4_2player_alt_test() {
		testFor("ttcc4_2player_alt.kif");
	}

	@Test
	public void process_ttcc4_2player_small_test() {
		testFor("ttcc4_2player_small.kif");
	}

	@Test
	public void process_tttcc4_test() {
		testFor("tttcc4.kif");
	}

	@Test
	public void process_wallmaze_test() {
		testFor("wallmaze.kif");
	}

	@Test
	public void process_withConviction_test() {
		testFor("withConviction.kif");
	}

	@Test
	public void process_zhadu_test() {
		testFor("zhadu.kif");
	}

}
