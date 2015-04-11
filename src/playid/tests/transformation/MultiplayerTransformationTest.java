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
	public void process_2pffa_test() throws Exception {
		testFor("2pffa.kif");
	}

	@Test
	public void process_2pffa_zerosum_test() throws Exception {
		testFor("2pffa_zerosum.kif");
	}

	@Test
	public void process_2pttc_test() throws Exception {
		testFor("2pttc.kif");
	}

	@Test
	public void process_3pConnectFour_test() throws Exception {
		testFor("3pConnectFour.kif");
	}

	@Test
	public void process_3pffa_test() throws Exception {
		testFor("3pffa.kif");
	}

	@Test
	public void process_3pttc_test() throws Exception {
		testFor("3pttc.kif");
	}

	@Test
	public void process_4pffa_test() throws Exception {
		testFor("4pffa.kif");
	}

	@Test
	public void process_4pttc_test() throws Exception {
		testFor("4pttc.kif");
	}

	@Test
	public void process_ad_game_2x2_test() throws Exception {
		testFor("ad_game_2x2.kif");
	}

	@Test
	public void process_alexChess_test() throws Exception {
		testFor("alexChess.kif");
	}

	@Test
	public void process_amazons_test() throws Exception {
		testFor("amazons.kif");
	}

	@Test
	public void process_amazonsSuicide_test() throws Exception {
		testFor("amazonsSuicide.kif");
	}

	@Test
	public void process_amazonsTorus_test() throws Exception {
		testFor("amazonsTorus.kif");
	}

	@Test
	public void process_battle_test() throws Exception {
		testFor("battle.kif");
	}

	@Test
	public void process_beatMania_test() throws Exception {
		testFor("beatMania.kif");
	}

	@Test
	public void process_biddingTicTacToe_test() throws Exception {
		testFor("biddingTicTacToe.kif");
	}

	@Test
	public void process_biddingTicTacToe_10coins_test() throws Exception {
		testFor("biddingTicTacToe_10coins.kif");
	}

	@Test
	public void process_blobwars_test() throws Exception {
		testFor("blobwars.kif");
	}

	@Test
	public void process_blocker_test() throws Exception {
		testFor("blocker.kif");
	}

	@Test
	public void process_blockerParallel_test() throws Exception {
		testFor("blockerParallel.kif");
	}

	@Test
	public void process_blockerSerial_test() throws Exception {
		testFor("blockerSerial.kif");
	}

	@Test
	public void process_blocks2player_test() throws Exception {
		testFor("blocks2player.kif");
	}

	@Test
	public void process_blokbox_simple_test() throws Exception {
		testFor("blokbox_simple.kif");
	}

	@Test
	public void process_bomberman2p_test() throws Exception {
		testFor("bomberman2p.kif");
	}

	@Test
	public void process_brawl_test() throws Exception {
		testFor("brawl.kif");
	}

	@Test
	public void process_breakthrough_test() throws Exception {
		testFor("breakthrough.kif");
	}

	@Test
	public void process_breakthroughHoles_test() throws Exception {
		testFor("breakthroughHoles.kif");
	}

	@Test
	public void process_breakthroughSmall_test() throws Exception {
		testFor("breakthroughSmall.kif");
	}

	@Test
	public void process_breakthroughSmallHoles_test() throws Exception {
		testFor("breakthroughSmallHoles.kif");
	}

	@Test
	public void process_breakthroughSuicide_test() throws Exception {
		testFor("breakthroughSuicide.kif");
	}

	@Test
	public void process_breakthroughWalls_test() throws Exception {
		testFor("breakthroughWalls.kif");
	}

	@Test
	public void process_bunk_t_test() throws Exception {
		testFor("bunk_t.kif");
	}

	@Test
	public void process_catcha_mouse_test() throws Exception {
		testFor("catcha_mouse.kif");
	}

	@Test
	public void process_cephalopodMicro_test() throws Exception {
		testFor("cephalopodMicro.kif");
	}

	@Test
	public void process_checkLines_test() throws Exception {
		testFor("checkLines.kif");
	}

	@Test
	public void process_checkers_test() throws Exception {
		testFor("checkers.kif");
	}

	@Test
	public void process_checkers_cylinder_mustjump_test() throws Exception {
		testFor("checkers-cylinder-mustjump.kif");
	}

	@Test
	public void process_checkers_mustjump_test() throws Exception {
		testFor("checkers-mustjump.kif");
	}

	@Test
	public void process_checkers_mustjump_torus_test() throws Exception {
		testFor("checkers-mustjump-torus.kif");
	}

	@Test
	public void process_checkers_newgoals_test() throws Exception {
		testFor("checkers-newgoals.kif");
	}

	@Test
	public void process_checkers_suicide_cylinder_mustjump_test()
			throws Exception {
		testFor("checkers-suicide-cylinder-mustjump.kif");
	}

	@Test
	public void process_checkersBarrelNoKings_test() throws Exception {
		testFor("checkersBarrelNoKings.kif");
	}

	@Test
	public void process_checkersSmall_test() throws Exception {
		testFor("checkersSmall.kif");
	}

	@Test
	public void process_checkersTiny_test() throws Exception {
		testFor("checkersTiny.kif");
	}

	@Test
	public void process_checkersTorus_test() throws Exception {
		testFor("checkersTorus.kif");
	}

	@Test
	public void process_checkersTorusNoKings_test() throws Exception {
		testFor("checkersTorusNoKings.kif");
	}

	@Test
	public void process_chess_test() throws Exception {
		testFor("chess.kif");
	}

	@Test
	public void process_chickentictactoe_test() throws Exception {
		testFor("chickentictactoe.kif");
	}

	@Test
	public void process_chickentoetictac_test() throws Exception {
		testFor("chickentoetictac.kif");
	}

	@Test
	public void process_chineseCheckers2_test() throws Exception {
		testFor("chineseCheckers2.kif");
	}

	@Test
	public void process_chineseCheckers3_test() throws Exception {
		testFor("chineseCheckers3.kif");
	}

	@Test
	public void process_chineseCheckers4_test() throws Exception {
		testFor("chineseCheckers4.kif");
	}

	@Test
	public void process_chineseCheckers6_test() throws Exception {
		testFor("chineseCheckers6.kif");
	}

	@Test
	public void process_chinook_test() throws Exception {
		testFor("chinook.kif");
	}

	@Test
	public void process_choicethroughalt_test() throws Exception {
		testFor("choicethroughalt.kif");
	}

	@Test
	public void process_chomp_test() throws Exception {
		testFor("chomp.kif");
	}

	@Test
	public void process_cittaceot_test() throws Exception {
		testFor("cittaceot.kif");
	}

	@Test
	public void process_colonelBlotto_test() throws Exception {
		testFor("colonelBlotto.kif");
	}

	@Test
	public void process_colonelBlottoVariant_test() throws Exception {
		testFor("colonelBlottoVariant.kif");
	}

	@Test
	public void process_colonelBlottoVariant2_test() throws Exception {
		testFor("colonelBlottoVariant2.kif");
	}

	@Test
	public void process_coloredtrails_test() throws Exception {
		testFor("coloredtrails.kif");
	}

	@Test
	public void process_conn4_test() throws Exception {
		testFor("conn4.kif");
	}

	@Test
	public void process_connect4_test() throws Exception {
		testFor("connect4.kif");
	}

	@Test
	public void process_connect5_test() throws Exception {
		testFor("connect5.kif");
	}

	@Test
	public void process_connectFour_test() throws Exception {
		testFor("connectFour.kif");
	}

	@Test
	public void process_connectFourLarge_test() throws Exception {
		testFor("connectFourLarge.kif");
	}

	@Test
	public void process_connectFourLarger_test() throws Exception {
		testFor("connectFourLarger.kif");
	}

	@Test
	public void process_connectFourSimultaneous_test() throws Exception {
		testFor("connectFourSimultaneous.kif");
	}

	@Test
	public void process_connectFourSuicide_test() throws Exception {
		testFor("connectFourSuicide.kif");
	}

	@Test
	public void process_crisscross_test() throws Exception {
		testFor("crisscross.kif");
	}

	@Test
	public void process_crissrace_test() throws Exception {
		testFor("crissrace.kif");
	}

	@Test
	public void process_crossers3_test() throws Exception {
		testFor("crossers3.kif");
	}

	@Test
	public void process_cubicup_test() throws Exception {
		testFor("cubicup.kif");
	}

	@Test
	public void process_cubicup_3player_test() throws Exception {
		testFor("cubicup_3player.kif");
	}

	@Test
	public void process_cylinder_checkers_test() throws Exception {
		testFor("cylinder-checkers.kif");
	}

	@Test
	public void process_dotsAndBoxes_test() throws Exception {
		testFor("dotsAndBoxes.kif");
	}

	@Test
	public void process_dotsAndBoxesSuicide_test() throws Exception {
		testFor("dotsAndBoxesSuicide.kif");
	}

	@Test
	public void process_double_tictactoe_dengji_test() throws Exception {
		testFor("double_tictactoe_dengji.kif");
	}

	@Test
	public void process_doubletictactoe_test() throws Exception {
		testFor("doubletictactoe.kif");
	}

	@Test
	public void process_doubletoetictac_test() throws Exception {
		testFor("doubletoetictac.kif");
	}

	@Test
	public void process_dualConnect4_test() throws Exception {
		testFor("dualConnect4.kif");
	}

	@Test
	public void process_endgame_test() throws Exception {
		testFor("endgame.kif");
	}

	@Test
	public void process_englishDraughts_test() throws Exception {
		testFor("englishDraughts.kif");
	}

	@Test
	public void process_eotcatcit_test() throws Exception {
		testFor("eotcatcit.kif");
	}

	@Test
	public void process_eotcitcit_test() throws Exception {
		testFor("eotcitcit.kif");
	}

	@Test
	public void process_escortLatch_test() throws Exception {
		testFor("escortLatch.kif");
	}

	@Test
	public void process_four_way_battle_test() throws Exception {
		testFor("four_way_battle.kif");
	}

	@Test
	public void process_ghostMaze2p_test() throws Exception {
		testFor("ghostMaze2p.kif");
	}

	@Test
	public void process_golden_rectangle_test() throws Exception {
		testFor("golden_rectangle.kif");
	}

	@Test
	public void process_gt_attrition_test() throws Exception {
		testFor("gt_attrition.kif");
	}

	@Test
	public void process_gt_centipede_test() throws Exception {
		testFor("gt_centipede.kif");
	}

	@Test
	public void process_gt_chicken_test() throws Exception {
		testFor("gt_chicken.kif");
	}

	@Test
	public void process_gt_coordination_test() throws Exception {
		testFor("gt_coordination.kif");
	}

	@Test
	public void process_gt_dollar_test() throws Exception {
		testFor("gt_dollar.kif");
	}

	@Test
	public void process_gt_prisoner_test() throws Exception {
		testFor("gt_prisoner.kif");
	}

	@Test
	public void process_gt_staghunt_test() throws Exception {
		testFor("gt_staghunt.kif");
	}

	@Test
	public void process_gt_ultimatum_test() throws Exception {
		testFor("gt_ultimatum.kif");
	}

	@Test
	public void process_guess_test() throws Exception {
		testFor("guess.kif");
	}

	@Test
	public void process_hallway_test() throws Exception {
		testFor("hallway.kif");
	}

	@Test
	public void process_hex_test() throws Exception {
		testFor("hex.kif");
	}

	@Test
	public void process_hodgepodge_test() throws Exception {
		testFor("hodgepodge.kif");
	}

	@Test
	public void process_kalaha_2009_test() throws Exception {
		testFor("kalaha_2009.kif");
	}

	@Test
	public void process_knightThrough_test() throws Exception {
		testFor("knightThrough.kif");
	}

	@Test
	public void process_knightazons_test() throws Exception {
		testFor("knightazons.kif");
	}

	@Test
	public void process_knightfight_test() throws Exception {
		testFor("knightfight.kif");
	}

	@Test
	public void process_knightwar_test() throws Exception {
		testFor("knightwar.kif");
	}

	@Test
	public void process_laikLee_hex_test() throws Exception {
		testFor("laikLee_hex.kif");
	}

	@Test
	public void process_merrills_test() throws Exception {
		testFor("merrills.kif");
	}

	@Test
	public void process_minichess_test() throws Exception {
		testFor("minichess.kif");
	}

	@Test
	public void process_minichess_evilconjuncts_test() throws Exception {
		testFor("minichess-evilconjuncts.kif");
	}

	@Test
	public void process_mummyMaze2p_2007_test() throws Exception {
		testFor("mummyMaze2p_2007.kif");
	}

	@Test
	public void process_mummymaze2p_test() throws Exception {
		testFor("mummymaze2p.kif");
	}

	@Test
	public void process_nim1_test() throws Exception {
		testFor("nim1.kif");
	}

	@Test
	public void process_nim2_test() throws Exception {
		testFor("nim2.kif");
	}

	@Test
	public void process_nim3_test() throws Exception {
		testFor("nim3.kif");
	}

	@Test
	public void process_nim4_test() throws Exception {
		testFor("nim4.kif");
	}

	@Test
	public void process_nineBoardTicTacToe_test() throws Exception {
		testFor("nineBoardTicTacToe.kif");
	}

	@Test
	public void process_numbertictactoe_test() throws Exception {
		testFor("numbertictactoe.kif");
	}

	@Test
	public void process_onestep_test() throws Exception {
		testFor("onestep.kif");
	}

	@Test
	public void process_othello_comp2007_test() throws Exception {
		testFor("othello-comp2007.kif");
	}

	@Test
	public void process_othelloHoles_test() throws Exception {
		testFor("othelloHoles.kif");
	}

	@Test
	public void process_othelloSuicide_test() throws Exception {
		testFor("othelloSuicide.kif");
	}

	@Test
	public void process_pacman2p_test() throws Exception {
		testFor("pacman2p.kif");
	}

	@Test
	public void process_pacman3p_test() throws Exception {
		testFor("pacman3p.kif");
	}

	@Test
	public void process_pawnToQueen_test() throws Exception {
		testFor("pawnToQueen.kif");
	}

	@Test
	public void process_pawnWhopping_test() throws Exception {
		testFor("pawnWhopping.kif");
	}

	@Test
	public void process_pentago_test() throws Exception {
		testFor("pentago.kif");
	}

	@Test
	public void process_pentagoSuicide_test() throws Exception {
		testFor("pentagoSuicide.kif");
	}

	@Test
	public void process_point_grab_test() throws Exception {
		testFor("point_grab.kif");
	}

	@Test
	public void process_quad_test() throws Exception {
		testFor("quad.kif");
	}

	@Test
	public void process_quad_5x5_test() throws Exception {
		testFor("quad_5x5.kif");
	}

	@Test
	public void process_quad_7x7_test() throws Exception {
		testFor("quad_7x7.kif");
	}

	@Test
	public void process_quarto_test() throws Exception {
		testFor("quarto.kif");
	}

	@Test
	public void process_quartoSuicide_test() throws Exception {
		testFor("quartoSuicide.kif");
	}

	@Test
	public void process_qyshinsu_test() throws Exception {
		testFor("qyshinsu.kif");
	}

	@Test
	public void process_racer_test() throws Exception {
		testFor("racer.kif");
	}

	@Test
	public void process_racer4_test() throws Exception {
		testFor("racer4.kif");
	}

	@Test
	public void process_racetrackcorridor_test() throws Exception {
		testFor("racetrackcorridor.kif");
	}

	@Test
	public void process_reversi_test() throws Exception {
		testFor("reversi.kif");
	}

	@Test
	public void process_roshambo2_test() throws Exception {
		testFor("roshambo2.kif");
	}

	@Test
	public void process_sheepAndWolf_test() throws Exception {
		testFor("sheepAndWolf.kif");
	}

	@Test
	public void process_skirmish_test() throws Exception {
		testFor("skirmish.kif");
	}

	@Test
	public void process_skirmishFinal1_test() throws Exception {
		testFor("skirmishFinal1.kif");
	}

	@Test
	public void process_skirmishFinal2_test() throws Exception {
		testFor("skirmishFinal2.kif");
	}

	@Test
	public void process_skirmishFinal3_test() throws Exception {
		testFor("skirmishFinal3.kif");
	}

	@Test
	public void process_skirmishHoles_test() throws Exception {
		testFor("skirmishHoles.kif");
	}

	@Test
	public void process_skirmishNew_test() throws Exception {
		testFor("skirmishNew.kif");
	}

	@Test
	public void process_skirmishZeroSum_test() throws Exception {
		testFor("skirmishZeroSum.kif");
	}

	@Test
	public void process_slaughter_test() throws Exception {
		testFor("slaughter.kif");
	}

	@Test
	public void process_smallest_4player_test() throws Exception {
		testFor("smallest_4player.kif");
	}

	@Test
	public void process_snake2p_test() throws Exception {
		testFor("snake2p.kif");
	}

	@Test
	public void process_snakeAssemblit_test() throws Exception {
		testFor("snakeAssemblit.kif");
	}

	@Test
	public void process_speedChess_test() throws Exception {
		testFor("speedChess.kif");
	}

	@Test
	public void process_strangeSkirmish_test() throws Exception {
		testFor("strangeSkirmish.kif");
	}

	@Test
	public void process_sum15_test() throws Exception {
		testFor("sum15.kif");
	}

	@Test
	public void process_switches_test() throws Exception {
		testFor("switches.kif");
	}

	@Test
	public void process_ticTacHeaven_test() throws Exception {
		testFor("ticTacHeaven.kif");
	}

	@Test
	public void process_ticTacHeavenFC_test() throws Exception {
		testFor("ticTacHeavenFC.kif");
	}

	@Test
	public void process_ticTacToe_test() throws Exception {
		testFor("ticTacToe.kif");
	}

	@Test
	public void process_ticTacToeLarge_test() throws Exception {
		testFor("ticTacToeLarge.kif");
	}

	@Test
	public void process_ticTacToeLargeSuicide_test() throws Exception {
		testFor("ticTacToeLargeSuicide.kif");
	}

	@Test
	public void process_ticTacToeNoVars_test() throws Exception {
		testFor("ticTacToeNoVars.kif");
	}

	@Test
	public void process_ticTacToeParallel_test() throws Exception {
		testFor("ticTacToeParallel.kif");
	}

	@Test
	public void process_ticTacToeSerial_test() throws Exception {
		testFor("ticTacToeSerial.kif");
	}

	@Test
	public void process_ticTicToe_test() throws Exception {
		testFor("ticTicToe.kif");
	}

	@Test
	public void process_ticblock_test() throws Exception {
		testFor("ticblock.kif");
	}

	@Test
	public void process_tictactoe_init1_test() throws Exception {
		testFor("tictactoe-init1.kif");
	}

	@Test
	public void process_tictactoe_test() throws Exception {
		testFor("tictactoe.kif");
	}

	@Test
	public void process_tictactoe_3d_2player_test() throws Exception {
		testFor("tictactoe_3d_2player.kif");
	}

	@Test
	public void process_tictactoe_3d_6player_test() throws Exception {
		testFor("tictactoe_3d_6player.kif");
	}

	@Test
	public void process_tictactoe_3d_small_2player_test() throws Exception {
		testFor("tictactoe_3d_small_2player.kif");
	}

	@Test
	public void process_tictactoe_3d_small_6player_test() throws Exception {
		testFor("tictactoe_3d_small_6player.kif");
	}

	@Test
	public void process_tictactoe_3player_test() throws Exception {
		testFor("tictactoe_3player.kif");
	}

	@Test
	public void process_tictactoe_orthogonal_test() throws Exception {
		testFor("tictactoe_orthogonal.kif");
	}

	@Test
	public void process_tictactoex9_test() throws Exception {
		testFor("tictactoex9.kif");
	}

	@Test
	public void process_toetictac_test() throws Exception {
		testFor("toetictac.kif");
	}

	@Test
	public void process_ttcc4_test() throws Exception {
		testFor("ttcc4.kif");
	}

	@Test
	public void process_ttcc4_2player_test() throws Exception {
		testFor("ttcc4_2player.kif");
	}

	@Test
	public void process_ttcc4_2player_alt_test() throws Exception {
		testFor("ttcc4_2player_alt.kif");
	}

	@Test
	public void process_ttcc4_2player_small_test() throws Exception {
		testFor("ttcc4_2player_small.kif");
	}

	@Test
	public void process_tttcc4_test() throws Exception {
		testFor("tttcc4.kif");
	}

	@Test
	public void process_wallmaze_test() throws Exception {
		testFor("wallmaze.kif");
	}

	@Test
	public void process_withConviction_test() throws Exception {
		testFor("withConviction.kif");
	}

	@Test
	public void process_zhadu_test() throws Exception {
		testFor("zhadu.kif");
	}

}
