package fodot.util;

import java.util.Arrays;
import java.util.List;

public class NameUtil {
	/* STATIC HELPERS */

	// TODO: this is crudely fixed to allow for "-"
	//private static String validNameRegex = "^[a-zA-Z$][a-zA-Z_$0-9]*$";
	private static String validNameRegex = "^[a-zA-Z$][a-zA-Z_$0-9]*$";
	private static List<String> allowedSpecialNames = Arrays.asList("-");
	private static String alphanumericRegex = "[^a-zA-Z0-9]";

	/**
	 * Checks if the string is a valid name for a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static boolean isValidName(String name) {
		if (name == null || name.length() == 0) {
			return false;
		}
		return (name.matches(validNameRegex)||allowedSpecialNames.contains("-"));
	}

	/**
	 * This method will do it's best to convert a string into a string that can actually be used as a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static String convertToValidName(String name) {
		if (name != null) {
			if (isValidName(name)) {
				return name;
			}
			String newName = name.replaceAll(alphanumericRegex, "");
			if (isValidName(newName)) {
				return newName;
			}
		}
		//If everything fails, just generate a random number as variable name (with a letter in front because it's required)
		return generateVariableName();
	}
	
	public static String generateVariableName() {
		return "x" + Double.toString(Math.random()).replaceAll("0.", "").substring(0, 8);
		
	}
}
