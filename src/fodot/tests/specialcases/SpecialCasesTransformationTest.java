package fodot.tests.specialcases;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import fodot.gdl_parser.GdlParser;

/**
 * This class tests if exceptions are throwed properly:
 * All testmethods starting with "noexception" should not give any troubles when parsing
 * All testmethods starting with "exception" should raise an exception when parsing
 * @author Thomas
 *
 */
@SuppressWarnings("unused")
public class SpecialCasesTransformationTest {

	private static final String GAMES_LOCATION = "resources/games/special/";
	private static final String DEFAULT_EXTENSION = ".kif";

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
		return new File(GAMES_LOCATION + gameName + DEFAULT_EXTENSION);
	}
	
	@Test
	public void noexception_case_1a() {
		testFor("test_case_1a");
	}

	@Test
	public void noexception_case_1b() {
		testFor("test_case_1b");
	}

	@Test
	public void noexception_case_2a() {
		testFor("test_case_2a");
	}

	@Test
	public void noexception_case_2b() {
		testFor("test_case_2b");
	}

	@Test
	public void noexception_case_2c() {
		testFor("test_case_2c");
	}

	@Test
	public void noexception_case_3a() {
		testFor("test_case_3a");
	}

	@Test
	public void noexception_case_3b() {
		testFor("test_case_3b");
	}

	@Test
	public void noexception_case_3c() {
		testFor("test_case_3c");
	}

	@Test
	public void noexception_case_3d() {
		testFor("test_case_3d");
	}

	@Test
	public void noexception_case_3e() {
		testFor("test_case_3e");
	}

	@Test
	public void noexception_case_3f() {
		testFor("test_case_3f");
	}

	@Test
	public void noexception_case_4a() {
		testFor("test_case_4a");
	}

	@Test
	public void noexception_case_5a() {
		testFor("test_case_5a");
	}

	@Test
	public void noexception_case_5b() {
		testFor("test_case_5b");
	}

	@Test
	public void noexception_case_5c() {
		testFor("test_case_5c");
	}

	@Test
	public void noexception_clean_not_distinct() {
		testFor("test_clean_not_distinct");
	}

	@Test
	public void noexception_distinct_beginning_rule() {
		testFor("test_distinct_beginning_rule");
	}

	
	@Test(expected=Exception.class)
	public void exception_invalid_function_arities_differ() {
		testFor("test_invalid_function_arities_differ");
	}

	@Test(expected=Exception.class)
	public void exception_test_invalid_sentence_arities_differ() {
		testFor("test_invalid_sentence_arities_differ");
	}
	
}
