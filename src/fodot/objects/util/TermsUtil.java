package fodot.objects.util;

public class TermsUtil {
/* STATIC HELPERS */
	
	private static String validNameRegex = "^[a-zA-Z$][a-zA-Z_$0-9]*$";
	private static String alphanumericRegex = "[^a-zA-Z0-9]";
	
	/**
	 * Checks if the string is a valid name for a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static boolean isValidName(String name) {
		if (name.length() == 0) {
			return false;
		}
		return name.matches(validNameRegex);
	}
	
	/**
	 * This method will do it's best to convert a string into a string that can actually be used as a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static String convertToValidName(String name) {
		if (isValidName(name)) {
			return name;
		}
		String newName = name.replaceAll(alphanumericRegex, "");
		if (isValidName(newName)) {
			return newName;
		}
		//If everything fails, just generate a random number as variablename (with a letter in front because it's required)
		return "x" + Double.toString(Math.random()).replaceAll("0.", "").substring(0, 8);
	}
}
