package fodot.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import fodot.gdl_parser.Parser;

/**
 * This class tests if exceptions are throwed properly:
 * All testmethods starting with "noexception" should not give any troubles when parsing
 * All testmethods starting with "exception" should raise an exception when parsing
 * @author Thomas
 *
 */
@SuppressWarnings("unused")
public class ParserExceptionsTest {

	private static final String GAMES_LOCATION = "resources/games/";
	private static final String DEFAULT_EXTENSION = ".kif";

	/**
	 * Just parses to see if it throws an exception
	 * @param path	path to the file
	 */
	private void parse(String gameName) {
		File toParse = new File(GAMES_LOCATION + gameName + DEFAULT_EXTENSION);
		Parser parser = new Parser(toParse);
	}

	@Test
	public void noexception_blocks() {
		parse("blocks");
	}

	@Test
	public void noexception_choice() {
		parse("choice");
	}

	@Test
	public void noexception_connectFour() {
		parse("connectFour");
	}

	@Test
	public void noexception_maze() {
		parse("maze");
	}

	@Test
	public void noexception_simpleMutex() {
		parse("simpleMutex");
	}

	@Test
	public void noexception_case_1a() {
		parse("test_case_1a");
	}

	@Test
	public void noexception_case_1b() {
		parse("test_case_1b");
	}

	@Test
	public void noexception_case_1c() {
		parse("test_case_1c");
	}

	@Test
	public void noexception_case_2a() {
		parse("test_case_2a");
	}

	@Test
	public void noexception_case_2b() {
		parse("test_case_2b");
	}

	@Test
	public void noexception_case_2c() {
		parse("test_case_2c");
	}

	@Test
	public void noexception_case_3a() {
		parse("test_case_3a");
	}

	@Test
	public void noexception_case_3b() {
		parse("test_case_3b");
	}

	@Test
	public void noexception_case_3c() {
		parse("test_case_3c");
	}

	@Test
	public void noexception_case_3d() {
		parse("test_case_3d");
	}

	@Test
	public void noexception_case_3e() {
		parse("test_case_3e");
	}

	@Test
	public void noexception_case_3f() {
		parse("test_case_3f");
	}

	@Test
	public void noexception_case_4a() {
		parse("test_case_4a");
	}

	@Test
	public void noexception_case_5a() {
		parse("test_case_5a");
	}

	@Test
	public void noexception_case_5b() {
		parse("test_case_5b");
	}

	@Test
	public void noexception_case_5c() {
		parse("test_case_5c");
	}

	@Test
	public void noexception_clean_not_distinct() {
		parse("test_clean_not_distinct");
	}

	@Test
	public void noexception_distinct_not_beginning_rule() {
		parse("test_distinct_not_beginning_rule");
	}

	@Test(expected=Exception.class)
	public void exception_invalid_function_arities_differ() {
		parse("test_invalid_function_arities_differ");
	}

	@Test(expected=Exception.class)
	public void exception_test_invalid_sentence_arities_differ() {
		parse("test_invalid_function_arities_differ");
	}

	@Test
	public void noexception_ticTacToe() {
		parse("ticTacToe");
	}


}
